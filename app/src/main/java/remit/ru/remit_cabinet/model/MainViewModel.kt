package remit.ru.remit_cabinet.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.OkHttpClient
import remit.ru.remit_cabinet.SecretConstants
import remit.ru.remit_cabinet.api.ApiInterface
import remit.ru.remit_cabinet.autentification.BasicAuthInterceptor
import remit.ru.remit_cabinet.otherClass.Authorization
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {
    //переменные для отслеживания изменения и подписки на них в активити
    val resultAuthorization = MutableLiveData<Authorization>()
    val errorAuthorization = MutableLiveData<String>()
    val tokenFirebase = MutableLiveData<String>()

    //подготавливаем стандартный набор для работы с https, перед подготовкой запроса
    private fun prepareTransfer(): ApiInterface {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(BasicAuthInterceptor(SecretConstants.login, SecretConstants.password))
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(SecretConstants.BASE_URL)
            .client(client)
            .build()
            .create(ApiInterface::class.java)
    }

    //авторизуемся но номеру телефона, передаваю новый токен в 1С
    fun onClickAuthorization(phoneNumber: String, tokenFirebase: String) {
        val retrofitBuilder = prepareTransfer()

        val retrofitData = retrofitBuilder.getData(phoneNumber, tokenFirebase)

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

    //авторизуемся по токену, сравнив его с данными в 1С
    fun loadAuthorizedUser(tokenFirebase: String) {
        val retrofitBuilder = prepareTransfer()

        val retrofitData = retrofitBuilder.getAuthorizedUser(tokenFirebase)

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

    //для отслеживания и подписки на токен
    fun getToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            tokenFirebase.value = it
            Log.e("TAG", "Token -> $it")
        }
    }

}