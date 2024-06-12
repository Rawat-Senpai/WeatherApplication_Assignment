package com.example.weatherapplicationmvvm.ui.onboardingPackage

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapplicationmvvm.R
import com.example.weatherapplicationmvvm.databinding.FragmentSplashScreenBinding
import com.example.weatherapplicationmvvm.utils.LocalManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SplashscreenFragment : Fragment() {

    private var _binding : FragmentSplashScreenBinding ?= null
    private val binding get() = _binding!!

    @Inject
    lateinit var localManager:LocalManager


    private val splashScreenDuration = 3000L
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashScreenBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)

        binding.imageView.startAnimation(fadeIn)


        if(localManager.getUserLoginStatus()){
            Handler(Looper.getMainLooper()).postDelayed({
                val navigator = findNavController()
//                navigator.popBackStack()
                navigator.navigate(R.id.action_splashScreen_to_homeFragment)

            }, splashScreenDuration)
        }else{
            Handler(Looper.getMainLooper()).postDelayed({
                val navigator = findNavController()
//                navigator.popBackStack()
                navigator.navigate(R.id.action_splashScreen_to_onBoardingFragment)

            }, splashScreenDuration)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}