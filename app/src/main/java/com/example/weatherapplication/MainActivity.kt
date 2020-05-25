package com.example.weatherapplication

import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock.elapsedRealtimeNanos
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

var start : Long = 0
var end : Long = 0
var iteration = 0

class MainActivity : AppCompatActivity() {
    val cityList = arrayListOf("Alingsås", "Arboga", "Arvika", "Askersund", "Boden", "Bollnäs", "Borgholm", "Borlänge", "Borås",
        "Djursholm", "Eksjö", "Enköping", "Eskilstuna", "Eslöv", "Fagersta", "Falkenberg", "Falköping", "Falun", "Filipstad", "Flen",
        "Gränna", "Gävle", "Göteborg", "Hagfors", "Halmstad", "Haparanda", "Hedemora", "Helsingborg", "Hjo", "Hudiksvall", "Huskvarna",
        "Härnösand", "Hässleholm", "Jönköping", "Kalmar", "Karlshamn", "Karlskoga", "Karlskrona", "Karlstad", "Katrineholm", "Kiruna",
        "Kramfors", "Kristianstad", "Kristinehamn", "Kumla", "Kungsbacka", "Kungälv", "Köping", "Laholm", "Landskrona", "Lidingö",
        "Lidköping", "Lindesberg", "Linköping", "Ljungby", "Ludvika", "Luleå", "Lund", "Lycksele", "Lysekil", "Malmö", "Mariefred",
        "Mariestad", "Marstrand", "Mjölby", "Motala", "Mölndal", "Nacka", "Nora", "Norrköping", "Nybro", "Nyköping", "Nynäshamn",
        "Nässjö", "Oskarshamn", "Oxelösund", "Piteå", "Ronneby", "Sala", "Sandviken", "Sigtuna", "Simrishamn", "Skara", "Skellefteå",
        "Skänninge", "Skövde", "Sollefteå", "Solna", "Stockholm", "Strängnäs", "Strömstad", "Sundbyberg", "Sundsvall", "Säffle",
        "Säter", "Sävsjö", "Söderhamn", "Söderköping", "Södertälje", "Sölvesborg", "Tidaholm", "Torshälla", "Tranås", "Trelleborg",
        "Trollhättan", "Trosa", "Uddevalla", "Ulricehamn", "Umeå", "Uppsala", "Vadstena", "Varberg", "Vaxholm", "Vetlanda", "Vimmerby",
        "Visby", "Vänersborg", "Värnamo", "Västervik", "Västerås", "Växjö", "Ystad", "Åmål", "Ängelholm", "Örebro", "Öregrund",
        "Örnsköldsvik", "Östhammar")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val random = Random()
        val randomCity = random.nextInt(cityList.count())
        val s = findViewById<TextView>(R.id.searchBox)
        s.text = cityList[randomCity]

        if(iteration < 1502){
            start = elapsedRealtimeNanos()
            weatherTask().execute()
            iteration++
            Log.d("iteration", iteration.toString())
        }
        else {
            super.onStop()
        }
    }



    inner class weatherTask : AsyncTask<String, Void, String>() {
            private val searchbox = findViewById<TextView>(R.id.searchBox).text
            override fun doInBackground(vararg params: String?): String? {
                var weather:String?
                try{
                    weather = URL("https://api.openweathermap.org/data/2.5/weather?q=$searchbox" +
                                        "&appid=00d7209b7ffda34cbe6e05e6f6746448").readText(Charsets.UTF_8)
                }catch (e: Exception){
                    weather = null
                }
                return weather
            }

        override fun onPostExecute(weather: String?) {
            super.onPostExecute(weather)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(weather)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                /* Extrating City and Country name fom API*/
                val address = jsonObj.getString("name")+", "+sys.getString("country")
                findViewById<TextView>(R.id.cityName).text = address

                val pattern = "yyyy-MM-dd"
                val simpleDateFormat = SimpleDateFormat(pattern)
                val date: String = simpleDateFormat.format(Date())
                findViewById<TextView>(R.id.date).text = date

                val temp = (main.getInt("temp") - 273.15).roundToInt().toString() + "℃"
                findViewById<TextView>(R.id.temp).text = temp

                val desc = weather.getString("description")
                findViewById<TextView>(R.id.desc).text = desc

                val icon = weather.getString("icon")
                val iconUrl = "http://openweathermap.org/img/wn/$icon.png"
                Picasso.get().load(iconUrl).into(iconView)

                end  = elapsedRealtimeNanos()
                var logData = (end - start) / 1000000.0
                Log.i("geh" , "$logData,")

                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);


            } catch (e: Exception) {

            }

        }
    }
}




