package com.example.climasync.view

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.climasync.databinding.FragmentWeatherBinding
import com.example.climasync.model.WeatherResponse
import com.example.climasync.utils.FragmentCommunicator
import com.example.climasync.viewModel.WeatherViewModel
import java.util.Locale
import com.example.climasync.R


/**
 * Fragment that displays weather information for a given location.
 * Uses [WeatherViewModel] to fetch and manage weather data.
 */
class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var communicator: FragmentCommunicator
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        communicator = requireActivity() as HomeActivity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        setupObservers()
        val coordinates = getUserCoordinates()
        viewModel.requestAPIInformation(coordinates)
    }

    private fun setupObservers() {
        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)
        }

        viewModel.mensajeweather.observe(viewLifecycleOwner) { weatherResponse ->
            weatherResponse?.let {
                showWeatherData(it)
            } ?: run {
                showErrorState()
            }
        }


    }

    private fun showWeatherData(weatherResponse: WeatherResponse) {
        with(binding) {
            txtNomCiudad.text = weatherResponse.location.name.ifEmpty { getString(R.string.unknown_location) }
            txtDiaHora.text = formatDateTime(weatherResponse.location.localTime)
            txtClima.text = getString(R.string.temperature_format, weatherResponse.current.tempC)
            txtVelocidadV.text = getString(R.string.wind_speed_format, weatherResponse.current.windKph)
            txtTemperatura.text = getString(R.string.temperature_format, weatherResponse.current.tempC)

            weatherResponse.current.condition.text?.let { condition ->
                txtSaludo.text = getWeatherGreeting(condition)
            }

            weatherResponse.current.condition.icon?.let { iconPath ->
                Glide.with(this@WeatherFragment)
                    .load("https:$iconPath")
                    .placeholder(R.drawable.ic_weather_placeholder)
                    .error(R.drawable.ic_weather_error)
                    .into(imageView)
            }
        }
    }

    private fun showErrorState() {
        with(binding) {
            txtNomCiudad.text = getString(R.string.error_loading_data)

        }
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun getUserCoordinates(): String {
        // TODO: Implementar lógica real para obtener coordenadas
        // Por ahora devuelve coordenadas por defecto (CDMX)
        return "19.42847,-99.12766"
    }

    // Extension functions for formatting
    private fun formatDateTime(dateTimeString: String?): String {
        if (dateTimeString.isNullOrEmpty()) return getString(R.string.unknown_time)
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val date = inputFormat.parse(dateTimeString)
            val outputFormat = SimpleDateFormat("EEEE, d MMMM - HH:mm", Locale.getDefault())
            outputFormat.format(date!!)
        } catch (e: Exception) {
            dateTimeString
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

        // Buscar traducción o usar el texto original si no existe
        val displayCondition = weatherTranslations[lowerCondition] ?: condition

        return "$emoji $displayCondition"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Factory method to create a new instance of this fragment.
         */
        fun newInstance() = WeatherFragment()
    }
}