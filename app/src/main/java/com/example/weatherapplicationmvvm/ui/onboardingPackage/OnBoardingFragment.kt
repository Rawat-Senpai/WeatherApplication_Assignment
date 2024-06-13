package com.example.weatherapplicationmvvm.ui.onboardingPackage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.weatherapplicationmvvm.R
import com.example.weatherapplicationmvvm.databinding.FragmentOnBoardingBinding
import com.example.weatherapplicationmvvm.utils.LocalManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {

    private var _binding :FragmentOnBoardingBinding?= null
    private val binding get() = _binding!!

    @Inject
     lateinit var localManager:LocalManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOnBoardingBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            getStarted.setOnClickListener(){
                localManager.setUserLoginStatus(true)
                findNavController().navigate(R.id.action_onBoardingFragment_to_homeFragment)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding= null
    }
// a8c958fe935405055cc2826bf92f1126   https://api.openweathermap.org/data/2.5/forecast
    //https://pro.openweathermap.org/data/2.5/forecast/hourly?lat={lat}&lon={lon}&appid={API key}
//   https://api.openweathermap.org/data/2.5/forecast?lat=44.34&lon=10.99&appid=26a51d79524440a6da150745aae90574
// https://api.openweathermap.org/data/2.5/forecast/hourly?q=110092&appid=a8c958fe935405055cc2826bf92f1126
//  https://api.openweathermap.org/data/2.5/weather?q=Rishikesh&appid=a8c958fe935405055cc2826bf92f1126
// https://api.openweathermap.org/data/2.5/weather?lat=30.9661&lon=76.5231&appid=a8c958fe935405055cc2826bf92f1126

}