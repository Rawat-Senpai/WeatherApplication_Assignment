package com.example.weatherapplicationmvvm.api

import com.example.weatherapplicationmvvm.models.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): Response<WeatherData>

    @GET("weather")
    suspend fun getCurrentWeatherLatLong(
        @Query("lat") lat: String,
        @Query("lon") long: String,
        @Query("appid") apiKey: String
    ) : Response<WeatherData>


}