package com.example.chickenapppbl5.model;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.DELETE;
import retrofit2.http.GET;

public interface ChickenApi {
    @GET("api/image/getall")
    Single<List<ChickenBreed>> getChickens();
    @GET("api/image/getbytime")
    Single<List<Day>> getDay();
}
