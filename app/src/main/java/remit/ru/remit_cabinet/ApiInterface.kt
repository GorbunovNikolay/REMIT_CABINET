package remit.ru.remit_cabinet

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {

    @GET("erp/hs/ExchangeKotlin/export/data/GET{ID}")
    fun getData(
        @Query("ID") id: String
    ): Call<MyData>

}