package remit.ru.remit_cabinet.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import remit.ru.remit_cabinet.R
import remit.ru.remit_cabinet.databinding.ActivityVerificationBinding
import remit.ru.remit_cabinet.utils.AndroidUtils

class VerificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        val binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val smsKey = intent.getStringExtra("smsKey")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val employee = intent.getStringExtra("employee")

        if (smsKey == "") {
            binding.descriptionOfActions.text = ""
            binding.OK.visibility = View.INVISIBLE

            AndroidUtils.hideKeyboard(binding.root)
            binding.descriptionOfActions.text = "Добро пожаловать $employee!"
            binding.enteredCode.visibility = View.INVISIBLE
        }
        else {
            binding.descriptionOfActions.text = "На указанный Вами номер $phoneNumber был отправлен 6 значный код, введите его:"
        }


        binding.OK.setOnClickListener {
            val enteredCode = binding.enteredCode.text.toString()
            if (smsKey.equals(enteredCode)) {
                AndroidUtils.hideKeyboard(binding.root)
                binding.descriptionOfActions.text = "Добро пожаловать $employee!"
                binding.OK.visibility = View.INVISIBLE
                binding.enteredCode.visibility = View.INVISIBLE
            }
            else {
                val intent = Intent(this@VerificationActivity, MainActivity::class.java)
                intent.putExtra("errorKey", "Ошибка. Некорректный код")
                startActivity(intent)
            }
        }


    }
}