package com.example.gonet_prueba;

import android.app.Application;

import androidx.room.Room;

import com.example.gonet_prueba.database.DetailDao;
import com.example.gonet_prueba.database.MovieDao;
import com.example.gonet_prueba.database.MoviesDb;
import com.example.gonet_prueba.movies.http.MoviesServicesApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {

    @Singleton
    @Provides
    MoviesServicesApi provideGithubService(){
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MoviesServicesApi.class);
    }

    @Singleton
    @Provides
    MoviesDb provideDb(Application app){
        return Room.databaseBuilder(app, MoviesDb.class, "movies.db").build();
    }

    @Singleton
    @Provides
    MovieDao provideMovieDao(MoviesDb db){
        return db.movieDao();
    }

    @Singleton
    @Provides
    DetailDao provideDetailDao(MoviesDb db){
        return db.detailDao();
    }
}
