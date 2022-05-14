package remit.ru.remit_cabinet.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import okhttp3.*
import remit.ru.remit_cabinet.*
import remit.ru.remit_cabinet.api.ApiInterface
import remit.ru.remit_cabinet.autentification.BasicAuthInterceptor
import remit.ru.remit_cabinet.databinding.ActivityMainBinding
import remit.ru.remit_cabinet.model.MainViewModel
import remit.ru.remit_cabinet.otherClass.Authorization
import remit.ru.remit_cabinet.utils.AndroidUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var mvmodel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mvmodel = ViewModelProvider(this)[MainViewModel::class.java]

        mvmodel.resultAuthorization.observe(this, ) { authorization ->
            val intent = Intent(this@MainActivity, VerificationActivity::class.java)
            intent.putExtra("smsKey", authorization.randomNumber)
            intent.putExtra("phoneNumber", binding.phoneNumber.text.toString())
            intent.putExtra("employee", authorization.employee.fullName)
            startActivity(intent)
        }

        mvmodel.errorAuthorization.observe(this, ) { errorAuthorizationText ->
            binding.textView.text = errorAuthorizationText
        }

        binding.button.setOnClickListener {
            val phoneNumber = "7" + binding.phoneNumber.text.toString()

            if (phoneNumber.length != 11) {
                return@setOnClickListener
            }

            AndroidUtils.hideKeyboard(binding.root)
            mvmodel.onClickAuthorization(binding.phoneNumber.text.toString())
        }
    }
}





