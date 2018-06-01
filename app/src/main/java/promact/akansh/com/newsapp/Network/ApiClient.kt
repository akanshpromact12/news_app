package promact.akansh.com.newsapp.Network
import android.content.ContentValues.TAG
import android.util.Log
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private val BASE_URL = "https://newsapi.org/v2/"
    /*private var serviceGenerator = ServiceGenerator()
    private val TAG = this.javaClass.simpleName

    fun getNews(context: Context, apiCallback: ApiCallback<News>) {
        val service = serviceGenerator.createService(ApiInterface::class.java)
        val call = service.getLatestNews(context.getString(R.string.source), context.getString(R.string.api_key))

        call.enqueue(object: Callback<News> {
            override fun onResponse(call: Call<News>?, response: Response<News>) {
                apiCallback.onResponse(response)
            }

            override fun onFailure(call: Call<News>?, t: Throwable?) {

            }
        })
    }*/
    fun getClient(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build()
        Log.d(TAG, "getClient timeout: " + client
                .readTimeoutMillis())

        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
    }
}
