package com.example.climasync.view

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.climasync.R
import com.example.climasync.databinding.FragmentLayoutPermissionBinding
import com.example.climasync.viewModel.LayoutPermisionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LayoutPermission : Fragment() {

    private var _binding: FragmentLayoutPermissionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LayoutPermisionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentLayoutPermissionBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.permissionContinuar.setOnClickListener {
            viewModel.requestLayoutPermision()

        }
        binding.permissionContinuar.setOnClickListener {
            findNavController().navigate(R.id.action_layoutPermission_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}