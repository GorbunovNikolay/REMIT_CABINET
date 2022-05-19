package remit.ru.remit_cabinet.otherClass

import com.google.gson.GsonBuilder

//Сотрудник
data class Employee (
    val surname: String,                            //Фамилия
    val name: String,                               //Имя
    val patronymic: String,                         //Отчество
    val fullName: String,                           //Полное ФИО
    val dateOfBirth: String,                        //Дата рождения
    val dismissed: Boolean,                         //Флаг уволен
    val post:String,                                //Должность
    val department:String,                          //Подразделение
    val years_of_experience:String,                 //Стаж Лет
    val months_of_experience:String,                //Стаж Месяцев
    val director:String,                            //Руководитель Подразделения
    val gender: String,                             //Пол
    val id_full_1C: String,                         //значение из строки внутр 1С
    val loyaltyCardNumber: String,                  //карта лояльности
    val phoneNumber: String,                        //номер телефона
    val tokenFirebase: String,                      //токен firebase
    val photoJson: String                           //фото в Json
)

