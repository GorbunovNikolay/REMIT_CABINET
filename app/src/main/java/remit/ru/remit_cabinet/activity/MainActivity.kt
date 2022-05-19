package remit.ru.remit_cabinet.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.GsonBuilder
import remit.ru.remit_cabinet.databinding.ActivityMainBinding
import remit.ru.remit_cabinet.model.MainViewModel
import remit.ru.remit_cabinet.utils.AndroidUtils


class MainActivity : AppCompatActivity() {

    private lateinit var mvmodel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mvmodel = ViewModelProvider(this)[MainViewModel::class.java]

        //генерим изменение токена в vmodel - tokenFirebase
        mvmodel.getToken()

        //подписка на изменение vmodel tokenFirebase, вызывается для авторизации по токену при открытии
        mvmodel.tokenFirebase.observe(this) { token ->
            //если ранее была авторизация по телефону, пытаемся загрузить сотрудника по токену в 1С
            mvmodel.loadAuthorizedUser(token)
        }

        //подписка на изменение vmodel resultAuthorization, вызывается в случае успеха авторизации
        mvmodel.resultAuthorization.observe(this) { authorization ->
            val intent = Intent(this@MainActivity, VerificationActivity::class.java)
            intent.putExtra("smsKey", authorization.randomNumber)
            if (binding.phoneNumber.text.isNotEmpty()){
                intent.putExtra("phoneNumber", binding.phoneNumber.text.toString())
            }
            else intent.putExtra("phoneNumber", authorization.employee.phoneNumber)

            val employeeJson = GsonBuilder().create().toJson(authorization.employee).toString()
            intent.putExtra("employee", employeeJson)
            startActivity(intent)
        }

        //подписка на изменение vmodel errorAuthorization, вызывается в случае отказа авторизации
        mvmodel.errorAuthorization.observe(this) { errorAuthorizationText ->
            if (errorAuthorizationText.equals("Сотрудник с данным токеном не найден в 1С")) {
                binding.textView.text = ""
            }
            else {
                binding.textView.text = errorAuthorizationText
                AndroidUtils.vibratePhone(binding.root)
            }

        }

        //слушатель нажатия авторизации по номеру телефона
        binding.button.setOnClickListener {
            val editnumber = binding.phoneNumber.text.toString()

            var counter = 1
            val sb = StringBuilder()
            for (char in editnumber) {
                if (counter == 1 && !char.equals('9')) {
                    sb.append("")
                    counter++
                    continue
                }
                sb.append(char.toString())
                counter++
            }

            val phoneNumber = "7" + sb.toString()

            if (phoneNumber.length != 11) {
                AndroidUtils.vibratePhone(binding.root)
                return@setOnClickListener
            }

            AndroidUtils.hideKeyboard(binding.root)

            FirebaseMessaging.getInstance().token.addOnSuccessListener {
                //новый токен, поэтому авторизация доступна только по телефону, сохраняем новый токен в 1С
                mvmodel.onClickAuthorization(binding.phoneNumber.text.toString(), it)
            }
        }

        //проверка на поддержку устройством гугл сервисов
        checkGoogleApiAvailability()
    }

    private fun checkGoogleApiAvailability() {
        with(GoogleApiAvailability.getInstance()) {
            val code = isGooglePlayServicesAvailable(this@MainActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }
            if (isUserResolvableError(code)) {
                getErrorDialog(this@MainActivity, code, 9000)?.show()
                return
            }
            Toast.makeText(this@MainActivity, "Google сервисы не работают на данном устройстве", Toast.LENGTH_LONG)
                .show()
        }

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.e("TAG", "Token -> $it")
        }
    }
}





