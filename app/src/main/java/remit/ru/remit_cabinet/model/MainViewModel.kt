package remit.ru.remit_cabinet.model

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.OkHttpClient
import remit.ru.remit_cabinet.SecretConstants
import remit.ru.remit_cabinet.activity.VerificationActivity
import remit.ru.remit_cabinet.api.ApiInterface
import remit.ru.remit_cabinet.autentification.BasicAuthInterceptor
import remit.ru.remit_cabinet.otherClass.Authorization
import remit.ru.remit_cabinet.utils.AndroidUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {

    val resultAuthorization = MutableLiveData<Authorization>()
    val errorAuthorization = MutableLiveData<String>()

    fun onClickAuthorization(phoneNumber: String) {
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

        val retrofitData = retrofitBuilder.getData(phoneNumber)

        retrofitData.enqueue(object : Callback<Authorization> {
            override fun onResponse(call: Call<Authorization>, response: Response<Authorization>) {
                if (response.isSuccessful) {
                    val authorization = response.body() ?: throw RuntimeException("body is null")
                    resultAuthorization.value = authorization
                }
                else {
                    val errorBody = response.errorBody()!!
                    errorAuthorization.value = errorBody.string()
                }
            }

            override fun onFailure(call: Call<Authorization>, t: Throwable) {
                errorAuthorization.value = t.toString()
            }
        })
    }
}