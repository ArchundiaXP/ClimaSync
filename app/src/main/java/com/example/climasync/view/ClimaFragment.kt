package com.example.climasync.view

import android.Manifest
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.climasync.R
import com.example.climasync.core.LocationProvider
import com.example.climasync.databinding.FragmentClimaBinding
import com.example.climasync.model.WeatherResponse
import com.example.climasync.utils.FragmentCommunicator
import com.example.climasync.view.adapters.ForecastAdapter
import com.example.climasync.viewModel.ForecastViewModel
import kotlinx.coroutines.launch
import java.util.*

class ClimaFragment : Fragment() {

    private var _binding: FragmentClimaBinding? = null
    private val binding get() = _binding!!
    private lateinit var communicator: FragmentCommunicator
    private val viewModel: ForecastViewModel by viewModels()
    private lateinit var forecastAdapter: ForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClimaBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        checkLocationPermissionAndFetch()
    }


    private fun setupRecyclerView() {
        forecastAdapter = ForecastAdapter()
        binding.recyclerForecast.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = forecastAdapter
        }
        Log.d("DEBUG_RECYCLER", "Adapter asignado correctamente")
    }
    private fun setupObservers() {
        viewModel.loaderState.observe(viewLifecycleOwner) {
            communicator.showLoader(it)
        }

        viewModel.mensajeweather.observe(viewLifecycleOwner) { weather ->
            weather?.let { showWeatherData(it) } ?: showErrorState()
        }

        viewModel.forecast.observe(viewLifecycleOwner) { forecastList ->
            Log.d("DEBUG_FORECAST", "Forecast recibido con ${forecastList.size} elementos")
            forecastAdapter.updateForecast(forecastList)
            val sunrise = forecastList.firstOrNull()?.astro?.sunrise
            binding.txtHoraAmanecer.text = sunrise ?: getString(R.string.unknown_time)
        }
    }

    private fun checkLocationPermissionAndFetch() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            getUserLocationAndWeather()
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1001)
        }
    }

    private fun getUserLocationAndWeather() {
        lifecycleScope.launch {
            try {
                val location = LocationProvider.getInstance(requireContext()).getCurrentLocation()
                if (location != null) {
                    viewModel.fetchWeatherByCoordinates(location.latitude, location.longitude)
                } else {
                    showDefaultLocation()
                }
            } catch (e: Exception) {
                showDefaultLocation()
            }
        }
    }

    private fun showDefaultLocation() {
        Toast.makeText(requireContext(), "Usando ubicación por defecto", Toast.LENGTH_SHORT).show()
        viewModel.fetchWeatherByCoordinates(40.7128, -74.0060) // Nueva York por defecto
    }

    private fun showWeatherData(weather: WeatherResponse) = with(binding) {
        txtClima.text = getString(R.string.temperature_format, weather.current.tempC)
        txtNomCiudad.text = weather.location.name.ifBlank { getString(R.string.unknown_location) }

        txtVelocidadV.text = getString(R.string.wind_speed_format, weather.current.windKph)
        txtTemperatura.text = getString(R.string.temperature_format, weather.current.tempC)

        txtSaludo.text = getWeatherGreeting(weather.current.condition.text ?: "")

        Glide.with(this@ClimaFragment)
            .load("https:${weather.current.condition.icon}")
            .placeholder(R.drawable.ic_weather_placeholder)
            .error(R.drawable.ic_weather_error)
            .into(imageView)
    }

    private fun formatDateTime(dateTime: String?): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val date = inputFormat.parse(dateTime ?: "") ?: return getString(R.string.unknown_time)
            val outputFormat = SimpleDateFormat("EEEE, d 'de' MMMM - HH:mm", Locale("es", "MX"))
            outputFormat.format(date)
        } catch (e: Exception) {
            getString(R.string.unknown_time)
        }
    }

    private val weatherTranslations = mapOf(
        // Condiciones despejadas/soleadas
        "sunny" to "soleado",
        "clear" to "despejado",

        // Condiciones nubladas
        "partly cloudy" to "parcialmente nublado",
        "cloudy" to "nublado",
        "overcast" to "muy nublado",

        // Condiciones de lluvia
        "rain" to "lluvia",
        "light rain" to "lluvia ligera",
        "moderate rain" to "lluvia moderada",
        "heavy rain" to "lluvia intensa",
        "patchy rain" to "lluvia dispersa",
        "drizzle" to "llovizna",
        "freezing drizzle" to "llovizna helada",

        // Condiciones de tormenta
        "thunderstorm" to "tormenta",
        "thunderstorm with light rain" to "tormenta con lluvia ligera",
        "thunderstorm with rain" to "tormenta con lluvia",
        "thunderstorm with heavy rain" to "tormenta con lluvia intensa",

        // Condiciones de nieve
        "snow" to "nieve",
        "light snow" to "nieve ligera",
        "heavy snow" to "nieve intensa",
        "sleet" to "aguanieve",
        "light sleet" to "aguanieve ligera",
        "freezing rain" to "lluvia helada",

        // Condiciones especiales
        "fog" to "niebla",
        "mist" to "neblina",
        "haze" to "bruma",
        "sand" to "tormenta de arena",
        "dust" to "tormenta de polvo"
    )

    private fun getWeatherGreeting(condition: String): String {
        val lowerCondition = condition.lowercase(Locale.getDefault())

        val emoji = when {
            lowerCondition.contains("sunny") || lowerCondition.contains("soleado") ||
                    lowerCondition.contains("clear") || lowerCondition.contains("despejado") -> "☀️"

            lowerCondition.contains("partly cloudy") || lowerCondition.contains("parcialmente nublado") -> "⛅"

            lowerCondition.contains("cloudy") || lowerCondition.contains("nublado") ||
                    lowerCondition.contains("overcast") || lowerCondition.contains("muy nublado") -> "☁️"

            lowerCondition.contains("rain") || lowerCondition.contains("lluvia") ||
                    lowerCondition.contains("drizzle") || lowerCondition.contains("llovizna") -> "🌧️"

            lowerCondition.contains("thunderstorm") || lowerCondition.contains("tormenta") -> "⛈️"

            lowerCondition.contains("snow") || lowerCondition.contains("nieve") ||
                    lowerCondition.contains("sleet") || lowerCondition.contains("aguanieve") -> "❄️"

            lowerCondition.contains("fog") || lowerCondition.contains("niebla") ||
                    lowerCondition.contains("mist") || lowerCondition.contains("neblina") -> "🌫️"

            else -> ""
        }


        val displayCondition = weatherTranslations[lowerCondition] ?: condition

        return "$emoji $displayCondition"
    }

    private fun showErrorState() {
        binding.txtNomCiudad.text = getString(R.string.error_loading_data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == 1001 && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            getUserLocationAndWeather()
        } else {
            showDefaultLocation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ClimaFragment()
    }
}
