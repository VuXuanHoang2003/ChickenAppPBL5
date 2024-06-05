package com.example.chickenapppbl5.model;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChickenApi {
    @GET("api/image/getbytime")
    Single<List<ChickenBreed>> getChickens(@Query("from_time") int from_time, @Query("to_time") int to_time);
    @GET("api/image/getbytime")
    Single<List<ChickenBreed>> getAll();
    @DELETE("api/image/delete")
    Single<String> deleteChicken(String uuid);
    @GET("api/sensor/")
    Single<List<ChickenSensor>> getSensors();
    @GET("api/image/getbytime")
    Single<List<ChickenBreed>> getHighTemp(@Query("minimum_temp") float temp);
}
