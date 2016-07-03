package com.example.shoji.itunesmusicresearch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import retrofit.MoshiConverterFactory
import retrofit.Retrofit
import java.io.IOException
import java.lang.reflect.Type
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG: String = "Main"
    internal val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "start okhttp")

        val button = findViewById(R.id.button) as Button
        button.setOnClickListener {
            object: MyAsyncTask() {
                override fun doInBackground(vararg params: Void): String? {
                    var res: String = ""
                    try {
                        res = run("http://api.openweathermap.org/data/2.5/weather?APPID=e5d03dd005670970bc5ce63e424d31fa&q=Tokyo")
                        val resJson = JSONObject(res)
                        val weathers = resJson.getJSONArray("weather")
                        val weather = weathers.getJSONObject(0)
                        val description = weather.getString("description")
                        Log.i("MainActivity", description)
                        res = description
                    } catch(e: IOException) {
                        e.printStackTrace()
                    } catch(e: JSONException) {
                        e.printStackTrace()
                    }
                    return res
                }
            }.execute()
        }


        // Moshi
//        val json = "[{'FirstName': 'Shoji', 'LastName': 'Kunitomo', 'Age': 28}, {'FirstName': 'Chihiro', 'LastName': 'Kunitomo', 'Age': 25}]"
//        val moshi = Moshi.Builder().build()
//        val listOfPeople = Types.newParameterizedType(List::class.java, Person::class.java)
//        val adapter = moshi.adapter(listOfPeople)
//        val people = adapter.fromJson(json)
//        val person = people[0]

//        val moshi = Moshi.Builder().build()
//        val adapter = moshi.adapter(Articles::class.java)
//
//        val articles = Articles.of(
//                Article("12", "Moshi1", Author("1", "droibit")),
//                Article("13", "Moshi2", Author("2", "droibit2")),
//                Article("14", "Moshi3", Author("3", "droibit3"))
//        )
//        val json = adapter.toJson(articles)
//        val restoreArticles = adapter.fromJson(json)
//        restoreArticles.articles[0].author
//        Log.d(TAG, restoreArticles.toString())


        //
        val moshi = Moshi.Builder()
                .add(UriAdapter.FACTORY)
                .build()
        val retrofit = Retrofit.Builder()
                .baseUrl("http://weather.livedoor.com")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        val service = retrofit.create(WeatherService::class.java)

        try {
            val response = service.weather("130010").execute()
            val weather = response?.body()
            // 何かする
        } catch (e: Exception) {
            // エラー処理
        }
    }

    fun run(url: String): String {
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        return response.body().string()
    }
}
