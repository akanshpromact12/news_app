package promact.akansh.com.newsapp.Network

import promact.akansh.com.newsapp.Model.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("everything")
    fun getLatestNews(@Query("sources") sources: String, @Query("pageSize") pageSize: Int, @Query("page") page: Int, @Query("apiKey") apiKey: String): Call<News>
}
