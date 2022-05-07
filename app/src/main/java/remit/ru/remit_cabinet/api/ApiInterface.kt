package remit.ru.remit_cabinet.api

import remit.ru.remit_cabinet.otherClass.Authorization
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {

    @GET("erp/hs/ExchangeKotlin/export/data/GET{phoneNumber}")
    fun getData(
        @Query("phoneNumber") id: String
    ): Call<Authorization>

}