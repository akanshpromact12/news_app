package promact.akansh.com.newsapp.Main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.google.gson.Gson
import promact.akansh.com.newsapp.Adapter.NewsAdapter
import promact.akansh.com.newsapp.Model.ArticlesParams
import promact.akansh.com.newsapp.Model.News
import promact.akansh.com.newsapp.Network.ApiClient
import promact.akansh.com.newsapp.Network.ApiInterface
import promact.akansh.com.newsapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var newsFrontRecycler: RecyclerView
    private var articles = ArrayList<ArticlesParams>()
    private val TAG = MainActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newsFrontRecycler = findViewById(R.id.recyclerNewsFront)
        newsFrontRecycler.layoutManager = LinearLayoutManager(this@MainActivity)
        getNews()
    }

    private fun getNews() {
        val apiInterface = ApiClient().getClient()
                .create(ApiInterface::class.java)
        val call = apiInterface.getLatestNews(getString(R.string.source), getString(R.string.api_key))
        call.enqueue(object : Callback<News> {
            override fun onFailure(call: Call<News>?, t: Throwable?) {
                Log.e(TAG, "Some error occurred: \n${t!!.message}")
            }

            override fun onResponse(call: Call<News>?, response: Response<News>?) {
                Log.d(TAG, "Total results: ${response!!.body().totalResults}")

                articles.addAll(response.body().articles)

                val adapter = NewsAdapter(this@MainActivity, articles)
                adapter.notifyDataSetChanged()

                newsFrontRecycler.adapter = adapter
            }
        })
    }
}
