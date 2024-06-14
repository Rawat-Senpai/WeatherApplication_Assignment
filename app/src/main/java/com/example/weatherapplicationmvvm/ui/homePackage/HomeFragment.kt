package com.example.weatherapplicationmvvm.ui.homePackage


import android.Manifest
import android.app.AlertDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.weatherapplicationmvvm.databinding.FragmentHomeBinding
import com.example.weatherapplicationmvvm.models.WeatherData
import com.example.weatherapplicationmvvm.models.WeatherHourlyForcast
import com.example.weatherapplicationmvvm.utils.NetworkResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.log


@AndroidEntryPoint
class HomeFragment : Fragment() {

    var count = 0;
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<WeatherViewModel>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        requestLocationPermission()


        setInitialState()
        bindHandler()
        bindObservers()


    }



    private fun bindObservers() {

        viewLifecycleOwner.lifecycleScope.launch {

            launch {
                viewModel.getWeatherData.collect{

                    when(it){
                        is NetworkResult.Error -> {
                            binding.permissionText.text= it.message.toString()
                            Log.d("CheckingDataResponse error ",it.toString())
                        }
                        is NetworkResult.Loading -> {

                            Log.d("CheckingDataResponse loading",it.toString())
                        }
                        is NetworkResult.Success -> {

                            setUI(it.data)
                            Log.d("CheckingDataResponse success",it.toString())
                        }
                        null -> {

                        }
                    }

                }
            }

            launch {
                viewModel.getWeatherLatLong.collect{


                    when(it){
                        is NetworkResult.Error -> {
                            binding.permissionText.text= it.message.toString()
                            Log.d("CheckingDataResponse error ",it.toString())
                        }
                        is NetworkResult.Loading -> {
                            setVisibility(false,true)
                            Log.d("CheckingDataResponse loading ",it.toString())
                        }
                        is NetworkResult.Success -> {

                            setUI(it.data)

                            Log.d("CheckingDataResponse success ",it.toString())
                        }
                        null -> {

                        }
                    }
                }
            }

            launch {
                viewModel.getWeatherForecast.collect{

                    when(it){
                        is NetworkResult.Error -> {
                            binding.permissionText.text= it.message.toString()
                            Log.d("CheckingDataResponse error ",it.toString())

                        }
                        is NetworkResult.Loading -> {

                            Log.d("CheckingDataResponse loading ",it.toString())
                        }
                        is NetworkResult.Success -> {

                            setUiForAdapter(it.data)
                            Log.d("CheckingDataResponse success ",it.toString())
                        }
                        null -> {

                        }
                    }
                }
            }

            launch {
                viewModel.getWeatherHourlyForecastLatLong.collect{

                    when(it){
                        is NetworkResult.Error -> {
                            binding.permissionText.text= it.message.toString()
                            Log.d("CheckingDataResponse error ",it.toString())
                        }
                        is NetworkResult.Loading -> {
                            setVisibility(false,true)
                            Log.d("CheckingDataResponse loading ",it.toString())
                        }
                        is NetworkResult.Success -> {
                            setVisibility(false,false)
                            Log.d("CheckingDataResponse success ",it.toString())
                        }
                        null -> {

                        }
                    }
                }
            }


        }

    }

    private fun setUiForAdapter(data: WeatherHourlyForcast?) {

        val arrayList = ArrayList<WeatherHourlyForcast.WeatherData>()
        val currentDate = getCurrentDateInFormat()

        for (value in data?.list!!){

            Log.d("arrayListValue",value.dt_txt.toString())
            if(currentDate == value.dt_txt.substringBefore(" ")){
                arrayList.add(value)
            }


        }

        Log.d("arrayListValue",arrayList.toString())


    }

    private fun setUI(data: WeatherData?) {

        binding.apply {
            locProvided.isVisible=false
            permissionText.isVisible=false

            temprature.text= data?.main?.temp.toString()+" F"
            perceptions.text = data?.weather?.get(0)?.description.toString()
            maxMinTemp.text  =  "Max : ${data?.main?.temp_max.toString()} F ${data?.main?.temp_min.toString()} F "

            Today.text = "Todays ${data?.name.toString()} Temperature "

            todayDate.text = getCurrentDate().toString()


        }



    }

    private fun setVisibility(permission: Boolean, progress : Boolean) {
        binding.apply{
//            locProvided.isVisible = permission
//            permissionText.isVisible = permission
//
//            progressBar.isVisible= progress

        }
    }

    private fun bindHandler() {

        binding.apply {

            searchEdt.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    binding.apply {
                        locProvided.isVisible=false
                        permissionText.isVisible=false
                        currentWeather.isVisible = true
                    }

                    // Perform the action when the search button is click
                    viewModel.getWeather(searchEdt.text.toString())
                    viewModel.getWeatherForecast(searchEdt.text.toString())

                    // Hide the keyboard
                    val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(requireView().windowToken, 0)

                    return@OnEditorActionListener true
                }
                false
            })


            locProvided.setOnClickListener(){
                requestLocationPermission()
            }


        }


    }

    private fun setInitialState() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun requestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is already granted
                getLastLocation()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // Show an explanation to the user why the permission is needed
                showPermissionExplanation()
            }

            else -> {
                // Directly request for permission
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
                )
            }
        }
    }

    private fun showPermissionExplanation() {
        // Show a dialog or some explanation to the user
        AlertDialog.Builder(requireContext())
            .setTitle("Location Permission Needed")
            .setMessage("This app needs the Location permission, please accept to use location functionality")
            .setPositiveButton("OK") { _, _ ->
                // Prompt the user once explanation has been shown
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
                )
            }
            .create()
            .show()
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, do nothing
            setVisibility(true,false)
            binding.locProvided.isVisible=true
            binding.permissionText.text="Location Permission Denied"
            binding.permissionText.isVisible= true
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
//                    Toast.makeText(context, "Lat: $lat, Lon: $lon", Toast.LENGTH_LONG).show()
                    binding.locProvided.isVisible=false
                    binding.permissionText.isVisible= false

                    viewModel.getWeatherLatLong(lat.toString(),lon.toString())
                } else {
                    Toast.makeText(context, "Location not available", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error getting location: ${e.message}", Toast.LENGTH_LONG)
                    .show()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                binding.locProvided.isVisible=true
                binding.permissionText.text="Location Permission Denied"
                binding.permissionText.isVisible= true
//                Toast.makeText(context, "Permission denied", Toast.LENGTH_LONG).show()
                if (count > 0) {
                    openAppSettings()
                }
                count++
            }
        }
    }


    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivity(intent)
    }


    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
    fun getCurrentDateInFormat(): String {
        val dateFormat = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }



}
