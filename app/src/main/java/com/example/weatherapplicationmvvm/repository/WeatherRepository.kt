package com.example.weatherapplicationmvvm.repository

import android.util.Log
import com.example.weatherapplicationmvvm.api.WeatherApi
import com.example.weatherapplicationmvvm.models.WeatherData
import com.example.weatherapplicationmvvm.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


import javax.inject.Inject

class WeatherRepository @Inject constructor(private val weatherApi: WeatherApi) {

    private val _weatherResult = MutableStateFlow<NetworkResult<WeatherData>?>(NetworkResult.Loading())
    val weatherResult: StateFlow<NetworkResult<WeatherData>?> get() = _weatherResult

    private val _weatherResultLatLong = MutableStateFlow<NetworkResult<WeatherData>?>(NetworkResult.Loading())
    val weatherResultLatLong:StateFlow<NetworkResult<WeatherData>?> get () = _weatherResultLatLong


    suspend fun getCityData(city: String, apiKey: String) {
        val response = weatherApi.getCurrentWeather(city, apiKey)

        if(response.isSuccessful && response.body()!= null){
            _weatherResult.emit(NetworkResult.Success(response.body()))
        }else if (response.errorBody()!=null){
            val errorObj = response.message()
            _weatherResult.emit(NetworkResult.Error(errorObj))
        }else{
            _weatherResult.emit(NetworkResult.Error("something went wrong"))
        }

        Log.d("checkingData",response.toString())
        Log.d("checkingData",response.body().toString())

    }

    suspend fun getLatLongData(lat:String,long:String,apiKey:String) {

        val response = weatherApi.getCurrentWeatherLatLong(lat,long,apiKey)

        if(response.isSuccessful && response.body()!= null){
            _weatherResultLatLong.emit(NetworkResult.Success(response.body()))
        }else if (response.errorBody()!=null){
            val errorObj = response.message()
            _weatherResultLatLong.emit(NetworkResult.Error(errorObj))
        }else{
            _weatherResultLatLong.emit(NetworkResult.Error("something went wrong"))
        }

        Log.d("checkingData",response.toString())
        Log.d("checkingData",response.body().toString())

    }



}