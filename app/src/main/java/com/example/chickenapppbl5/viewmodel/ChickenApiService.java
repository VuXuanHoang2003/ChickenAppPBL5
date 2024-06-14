package com.example.chickenapppbl5.viewmodel;

import com.example.chickenapppbl5.model.ChickenAnalyze;
import com.example.chickenapppbl5.model.ChickenApi;
import com.example.chickenapppbl5.model.ChickenBreed;
import com.example.chickenapppbl5.model.ChickenSensor;

import java.util.List;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChickenApiService  {
    private static final String BASE_URL="http://nglam.xyz/api/v2/";
    private ChickenApi api;
    public  ChickenApiService(){
        api= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(ChickenApi.class);
    }
    public Single<List<ChickenBreed>> getChickens(int from_time, int to_time){
        return api.getChickens(from_time, to_time);
    }

    public Single<List<ChickenSensor>> getSensors(){
        return api.getSensors();
    }
    public Single<List<ChickenSensor>> getSensorsTime(int from_time, int to_time){
        return api.getSensorsTime(from_time, to_time);
    }
    public Single<ChickenAnalyze> getAnalyzeTime(int from_time, int to_time){
        return api.getAnalyzeTime(from_time, to_time);
    }
    public Single<String> deleteChicken(String uuid){
        return api.deleteChicken(uuid);
    }

    public Single<List<ChickenBreed>> getAll(int limit){
        return api.getAll(limit);
    }

    public Single<List<ChickenBreed>> getHighTemp(float temp) {
        return api.getHighTemp(temp);
    }

}
