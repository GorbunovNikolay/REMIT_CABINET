package remit.ru.remit_cabinet.otherClass

//класс авторизации
data class Authorization(
    val employee: Employee,                     //класс сотрудника
    val actionName: String,                     //выполняемое действие
    val randomNumber: String                   //случайный номер, получаемый из SMS
)