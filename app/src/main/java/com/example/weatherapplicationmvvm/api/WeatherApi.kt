package com.example.weatherapplicationmvvm.api

import com.example.weatherapplicationmvvm.models.WeatherData
import com.example.weatherapplicationmvvm.repository.WeatherHourlyForcast
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


    @GET("forecast")
    suspend fun getWeatherForecast(
    @Query("q") city:String,
    @Query("appid") apiKey: String
    ):Response<WeatherHourlyForcast>


    @GET("weather")
    suspend fun getWeatherForecastLatLong(
        @Query("lat") lat: String,
        @Query("lon") long: String,
        @Query("appid") apiKey: String
    ) : Response<WeatherHourlyForcast>

}