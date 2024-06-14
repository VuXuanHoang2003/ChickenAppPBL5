package com.example.chickenapppbl5.model;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChickenApi {
    @GET("image/search")
    Single<List<ChickenBreed>> getChickens(@Query("from_time") int from_time, @Query("to_time") int to_time);
    @GET("image/search")
    Single<List<ChickenBreed>> getAll(@Query("limit") int limit);
    @DELETE("image/delete")
    Single<String> deleteChicken(String uuid);
    @GET("sensor")
    Single<List<ChickenSensor>> getSensors();
    @GET("sensor")
    Single<List<ChickenSensor>> getSensorsTime(@Query("from_time") int from_time, @Query("to_time") int to_time);
    @GET("analyze/consumed")
    Single<ChickenAnalyze> getAnalyzeTime(@Query("from_time") int from_time, @Query("to_time") int to_time);
    @GET("image/search")
    Single<List<ChickenBreed>> getHighTemp(@Query("minimum_temp") float temp);
}
