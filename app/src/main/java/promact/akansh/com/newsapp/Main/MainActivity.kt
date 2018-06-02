package promact.akansh.com.newsapp.Main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ProgressBar
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
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private var articles = ArrayList<ArticlesParams>()
    private lateinit var progress: ProgressBar
    private val TAG = MainActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newsFrontRecycler = findViewById(R.id.recyclerNewsFront)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        progress = findViewById(R.id.progress)
        newsFrontRecycler.layoutManager = LinearLayoutManager(this@MainActivity)
        progress.visibility = View.VISIBLE
        getNews(false)
        swipeRefresh.setOnRefreshListener {
            refreshItems()
        }
    }

    private fun refreshItems() {
        progress.visibility = View.INVISIBLE
        getNews(true)
    }

    private fun onItemsLoadComplete(isRefreshed: Boolean, articles: ArrayList<ArticlesParams>) {
        val adapter = NewsAdapter(this@MainActivity, articles)
        adapter.notifyDataSetChanged()

        newsFrontRecycler.adapter = adapter
        if (isRefreshed)
            swipeRefresh.isRefreshing = false
    }

    private fun getNews(isRefreshed: Boolean) {
        val apiInterface = ApiClient().getClient()
                .create(ApiInterface::class.java)
        val call = apiInterface.getLatestNews(getString(R.string.source), getString(R.string.api_key))
        call.enqueue(object : Callback<News> {
            override fun onFailure(call: Call<News>?, t: Throwable?) {
                Log.e(TAG, "Some error occurred: \n${t!!.message}")
            }

            override fun onResponse(call: Call<News>?, response: Response<News>?) {
                Log.d(TAG, "Total results: ${response!!.body().totalResults}")
                progress.visibility = View.INVISIBLE
                articles.clear()
                articles.addAll(response.body().articles)

                onItemsLoadComplete(isRefreshed, articles)
            }
        })
    }
}
