package com.example.weatherapplication

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onClick()
    }

    private fun onClick(){
        searchButton.setOnClickListener {
            weatherTask().execute()
        }
    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=00d7209b7ffda34cbe6e05e6f6746448").readText(Charsets.UTF_8)
                Log.d(response, "cityInfo")
            }catch (e: Exception){
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

                Log.d(main.toString(), "main")
                Log.d(sys.toString(), "sys")
                Log.d(weather.toString(), "weather")

                val address = jsonObj.getString("name")+", "+sys.getString("country")
                findViewById<TextView>(R.id.cityName).text = address

            } catch (e: Exception) {

            }
        }
    }
}

