package com.example.apiconnectapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var searchInput: EditText
    private lateinit var searchButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var resultText: TextView
    private lateinit var cityNameText: TextView
    private lateinit var temperatureText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var humidityText: TextView
    private lateinit var windSpeedText: TextView

    private val API_KEY = "6e613b9d524034e7c732a8f86df4877e"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchInput = findViewById(R.id.searchInput)
        searchButton = findViewById(R.id.searchButton)
        progressBar = findViewById(R.id.progressBar)
        resultText = findViewById(R.id.resultText)
        cityNameText = findViewById(R.id.cityNameText)
        temperatureText = findViewById(R.id.temperatureText)
        descriptionText = findViewById(R.id.descriptionText)
        humidityText = findViewById(R.id.humidityText)
        windSpeedText = findViewById(R.id.windSpeedText)

        searchButton.setOnClickListener {
            val city = searchInput.text.toString().trim()
            if (city.isNotEmpty()) {
                fetchWeatherData(city)
            } else {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchWeatherData(city: String) {
        // Show loading indicator
        progressBar.visibility = View.VISIBLE
        resultText.visibility = View.GONE
        hideWeatherDetails()

        val apiService = RetrofitClient.instance
        val call = apiService.getWeather(city, API_KEY, "metric")

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    val weather = response.body()!!
                    displayWeatherData(weather)
                } else {
                    when (response.code()) {
                        404 -> showError("City not found. Please check the name and try again.")
                        401 -> showError("API key invalid. Please check your configuration.")
                        else -> showError("Error: ${response.code()} - ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                progressBar.visibility = View.GONE

                val errorMessage = when {
                    t.message?.contains("Unable to resolve host") == true ->
                        "No internet connection. Please check your network."
                    t.message?.contains("timeout") == true ->
                        "Request timed out. Please try again."
                    else ->
                        "Network error: ${t.message}"
                }
                showError(errorMessage)
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun displayWeatherData(weather: WeatherResponse) {
        resultText.visibility = View.GONE
        showWeatherDetails()

        cityNameText.text = "${weather.name}, ${weather.sys.country}"
        temperatureText.text = "${weather.main.temp.toInt()}°C"
        descriptionText.text = weather.weather[0].description.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }
        humidityText.text = "Humidity: ${weather.main.humidity}%"
        windSpeedText.text = "Wind Speed: ${weather.wind.speed} m/s"
    }

    private fun showError(message: String) {
        resultText.visibility = View.VISIBLE
        resultText.text = message
        hideWeatherDetails()
    }

    private fun showWeatherDetails() {
        cityNameText.visibility = View.VISIBLE
        temperatureText.visibility = View.VISIBLE
        descriptionText.visibility = View.VISIBLE
        humidityText.visibility = View.VISIBLE
        windSpeedText.visibility = View.VISIBLE
    }

    private fun hideWeatherDetails() {
        cityNameText.visibility = View.GONE
        temperatureText.visibility = View.GONE
        descriptionText.visibility = View.GONE
        humidityText.visibility = View.GONE
        windSpeedText.visibility = View.GONE
    }
}