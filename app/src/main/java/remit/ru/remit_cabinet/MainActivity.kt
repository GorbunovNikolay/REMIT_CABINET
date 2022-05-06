package remit.ru.remit_cabinet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import remit.ru.remit_cabinet.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {

            val client =  OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(BasicAuthInterceptor(SecretConstants.login, SecretConstants.password))
                .build()

            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SecretConstants.BASE_URL)
                .client(client)
                .build()
                .create(ApiInterface::class.java)

            val id = binding.editTextNumber.text.toString()

            val retrofitData = retrofitBuilder.getData(id)

            retrofitData.enqueue(object : Callback<MyData?> {
                override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body() ?: throw RuntimeException("body is null")
                        binding.textView.text = responseBody.serverResponse
                    }
                    else {
                        val errorBody = response.errorBody()!!
                        binding.textView.text = errorBody.string()
                    }
                }

                override fun onFailure(call: Call<MyData?>, t: Throwable) {

                }
            })
        }
    }
}




