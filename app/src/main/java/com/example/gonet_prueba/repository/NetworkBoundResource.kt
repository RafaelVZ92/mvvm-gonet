package com.example.gonet_prueba.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.gonet_prueba.AppExecutors
import com.example.gonet_prueba.movies.http.ApiEmptyResponse
import com.example.gonet_prueba.movies.http.ApiErrorResponse
import com.example.gonet_prueba.movies.http.ApiResponse
import com.example.gonet_prueba.movies.http.ApiSuccessResponse


abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors){
    private val result = MediatorLiveData<ResourceStatus<ResultType>>()

    init {
        result.value = ResourceStatus.loading(null)
        val dbSource = loadFromDb()
        /**Aqui se agrega los devuelto por la base de datos al MediatorLiveData*/
        result.addSource(dbSource){
            /**Este codigo se ejecutara cada que ocurra un cambio */
                data->
            /**Paramos de observar esa fuente de datos por que nos ha llegado un nuevo dato*/
            result.removeSource(dbSource)
            /**Hay datos en la base de datos*/
            if(shouldFetch(data)){
                /**Cargar los datos desde la red*/
                fetchFromNetwork(dbSource)
            } else{
                /**Leerlos de la base de datos local*/
                result.addSource(dbSource){newData->
                    /**Cuando de cargaron los datos correctamente de la base de datos*/
                    setValue(ResourceStatus.success(newData))
                }
            }
        }
    }

    /**Agregamos el valor al mediatorlivedata*/
    @MainThread
    private fun setValue(newValue: ResourceStatus<ResultType>){
        /**Si los datos ya no son iguales entoces lo agrega*/
        if(result.value != newValue){
            result.value = newValue
        }
    }

    /**Cargar los datos desde la red */
    private fun fetchFromNetwork(dbSource: LiveData<ResultType>){
        val apiResponse = createCall()
        /**Hace una llamada ala base de datos por mientras se cargan los datos del webservice para mostrar algo por el momento*/
        result.addSource(dbSource){newData->
            setValue(ResourceStatus.loading(newData))
        }
        /**Agregamos una fuente de datos del webservice*/
        result.addSource(apiResponse){response->
            /**Removemos ambas fuentes de datos*/
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            /**Analisamos la respueta*/
            when(response){
                /**Si la respuesta es exitosa*/
                is ApiSuccessResponse ->{
                    /**Ejecutamos nuestro thread de estrada/salida para guardar el dato en bd*/
                    appExecutors.diskIO().execute{
                        saveCallResult(processResponse(response))
                        /**Agregamos o cargamos los datos de la base de datos en el hilo principal*/
                        appExecutors.mainThread().execute{
                            /**Agregamos una nueva fuente de dato al mediatolivedata*/
                            result.addSource(loadFromDb()){newData->
                                setValue(ResourceStatus.success(newData))
                            }
                        }
                    }
                }
                is ApiEmptyResponse ->{
                    appExecutors.mainThread().execute{
                        result.addSource(loadFromDb()){newData->
                            setValue(ResourceStatus.success(newData))
                        }
                    }
                }
                is ApiErrorResponse->{
                    onFetchFailed()
                    result.addSource(dbSource){newData->
                        setValue(ResourceStatus.error((response).errorMessage, newData))
                    }
                }
            }

        }
    }
    /**Este metodo se ejecutara por si ha fallado nuestra peticion de datos*/
    protected open fun onFetchFailed(){}

    /**convierte de mediatorlivedata a livedata*/
    fun coverterLiveData() = result as LiveData<ResourceStatus<ResultType>>
    /**Procesamos la respuetsa para poder guardarla correctamente*/
    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body
    /**Para guardar los datos que nos proporciona el websevice*/
    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    /**Este metodo decide si debemos solicitar datos de un webservice*/
    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    /**Este metodo carga los datos de la base de datos una vez se inicia la clase*/
    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    /**Encargado de realizar la peticion al webservice*/
    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}