package com.example.climasync.view.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.example.climasync.databinding.ActivityOnboardingBinding
import com.example.climasync.utils.FragmentCommunicator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Onboarding : AppCompatActivity(), FragmentCommunicator {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    override fun showLoader(value: Boolean) {
        binding.loaderContainerView.visibility = if (value) View.VISIBLE else View.GONE
    }
}