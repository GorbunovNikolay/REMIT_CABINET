package remit.ru.remit_cabinet.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import remit.ru.remit_cabinet.*
import remit.ru.remit_cabinet.api.ApiInterface
import remit.ru.remit_cabinet.autentification.BasicAuthInterceptor
import remit.ru.remit_cabinet.databinding.ActivityMainBinding
import remit.ru.remit_cabinet.otherClass.Authorization
import remit.ru.remit_cabinet.utils.AndroidUtils
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

        val errorKey = intent.getStringExtra("errorKey")

        if (errorKey != null) {
            binding.textView.text = errorKey
        }

        var smsKey = ""
        var employee = ""

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

            val phoneNumber = "7" + binding.phoneNumber.text.toString()

            if (phoneNumber.length != 11) {
                return@setOnClickListener
            }

            AndroidUtils.hideKeyboard(binding.root)

            val retrofitData = retrofitBuilder.getData(phoneNumber)

            retrofitData.enqueue(object : Callback<Authorization?> {
                override fun onResponse(call: Call<Authorization?>, response: Response<Authorization?>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body() ?: throw RuntimeException("body is null")
                        employee = responseBody.authorization.employee.toString()
                        smsKey = responseBody.authorization.randomNumber.toString()

                        val intent = Intent(this@MainActivity, VerificationActivity::class.java)
                        intent.putExtra("smsKey", smsKey)
                        intent.putExtra("phoneNumber", phoneNumber)
                        intent.putExtra("employee", employee)
                        startActivity(intent)
                    }
                    else {
                        val errorBody = response.errorBody()!!
                        binding.textView.text = errorBody.string()
                    }
                }

                override fun onFailure(call: Call<Authorization?>, t: Throwable) {

                }
            })
        }
    }
}




