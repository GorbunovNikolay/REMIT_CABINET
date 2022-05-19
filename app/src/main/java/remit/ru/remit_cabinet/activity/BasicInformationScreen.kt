package remit.ru.remit_cabinet.activity


import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import remit.ru.remit_cabinet.R
import remit.ru.remit_cabinet.databinding.ActivityBasicInformationScreenBinding
import remit.ru.remit_cabinet.otherClass.Employee


class BasicInformationScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBasicInformationScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val employeeJson = intent.getStringExtra("employee")
        val employee = GsonBuilder().create().fromJson(employeeJson, Employee::class.java)

        binding.surname.text = employee.surname.toUpperCase()
        binding.namePatronymic.text = "${employee.name} ${employee.patronymic}"
        binding.post.text = employee.post
        binding.experience.text = "cтаж: ${employee.years_of_experience},${employee.months_of_experience}"

        val imageBytes = Base64.decode(employee.photoJson, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.lastIndex)
        binding.avatar.setImageBitmap(decodedImage)

    }
}