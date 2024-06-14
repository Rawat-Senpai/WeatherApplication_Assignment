package com.example.weatherapplicationmvvm.ui.homePackage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplicationmvvm.models.WeatherData
import com.example.weatherapplicationmvvm.models.WeatherHourlyForcast
import com.example.weatherapplicationmvvm.repository.WeatherRepository
import com.example.weatherapplicationmvvm.utils.Constants
import com.example.weatherapplicationmvvm.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository):ViewModel() {

    fun getWeather(location:String){
        viewModelScope.launch {
            repository.getCityData(location,Constants.API_KEY)
        }
    }
    val getWeatherData :StateFlow<NetworkResult<WeatherData>?> get()= repository.weatherResult



    fun getWeatherLatLong(lat:String,long:String){
        viewModelScope.launch {
            repository.getLatLongData(lat,long,Constants.API_KEY)
        }
    }
    val getWeatherLatLong:StateFlow<NetworkResult<WeatherData>?> get() = repository.weatherResultLatLong


    fun getWeatherForecast(location:String){
        viewModelScope.launch {
            repository.getHourlyForecast(location,Constants.API_KEY)
        }
    }
    val getWeatherForecast:StateFlow<NetworkResult<WeatherHourlyForcast>?> get () = repository.weatherHourlyForecast

    fun getWeatherForecastLatLong(lat:String,long:String){
        viewModelScope.launch {
            repository.getHourlyForecastLatLong(lat,long,Constants.API_KEY)
        }
    }
    val getWeatherHourlyForecastLatLong:StateFlow<NetworkResult<WeatherHourlyForcast>?> get () = repository.weatherHourlyForecastLatLong


}