package com.example.weatherapplication

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getWeather().execute()
    }

    inner class getWeather() : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=00d7209b7ffda34cbe6e05e6f6746448").readText(Charsets.UTF_8)
                Log.d(response ,"cityInfo").toString()
            }catch (e:Exception){
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val city = jsonObj.getString("name")+", "+sys.getString("country")
                val temp = (main.getInt("temp") - 273.15).roundToInt().toString() + "℃"
                val desc = weather.getString("description")

                val pattern = "yyyy-MM-dd"
                val simpleDateFormat = SimpleDateFormat(pattern)
                val date: String = simpleDateFormat.format(Date())


                findViewById<TextView>(R.id.cityName).text = city
                findViewById<TextView>(R.id.weatherType).text = desc
                findViewById<TextView>(R.id.temp).text = temp.toString()
                findViewById<TextView>(R.id.date).text = date



            } catch (e: Exception) {

            }
        }
    }
}

