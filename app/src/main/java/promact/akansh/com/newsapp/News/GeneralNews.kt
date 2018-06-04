package promact.akansh.com.newsapp.News

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import promact.akansh.com.newsapp.Adapter.NewsAdapter
import promact.akansh.com.newsapp.Model.ArticlesParams
import promact.akansh.com.newsapp.Model.News
import promact.akansh.com.newsapp.Network.ApiClient
import promact.akansh.com.newsapp.Network.ApiInterface
import promact.akansh.com.newsapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GeneralNews : android.support.v4.app.Fragment() {
    private lateinit var root: View
    private lateinit var newsFrontRecycler: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private var articles = ArrayList<ArticlesParams>()
    private lateinit var progress: ProgressBar
    private val TAG = GeneralNews::class.java.name
    private lateinit var progressRecycler: ProgressBar
    private lateinit var adapter: NewsAdapter
    var news: News? = null
    private var loading = false
    private var pageNo = 1
    private var src = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_news_gen, container, false)

        newsFrontRecycler = root.findViewById(R.id.recyclerNewsFront)
        swipeRefresh = root.findViewById(R.id.swipeRefresh)
        progress = root.findViewById(R.id.progress)
        progressRecycler = root.findViewById(R.id.progressRecycler)
        newsFrontRecycler.layoutManager = LinearLayoutManager(root.context)
        progress.visibility = View.VISIBLE
        adapter = NewsAdapter(root.context, articles)
        newsFrontRecycler.adapter = adapter
        getNews(false, 1)
        swipeRefresh.setOnRefreshListener {
            refreshItems()
        }
        newsFrontRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val itemCount = recyclerView!!.layoutManager.itemCount
                //loading = false
                val currentPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() + 1

                //Toast.makeText(this@MyDealsActivity, "Last element - change -> item count: $itemCount currentPosition :$currentPosition", Toast.LENGTH_SHORT).show()

                if (!loading && itemCount == currentPosition && news!= null && itemCount < news!!.totalResults) {
                    //Toast.makeText(this@MyDealsActivity, "Last element - change(compare)", Toast.LENGTH_SHORT).show()
                    progressRecycler.visibility = View.VISIBLE
                    pageNo += 1
                    getNews(false, pageNo)

                    loading = true
                }
            }
        })

        return root
    }

    private fun refreshItems() {
        progress.visibility = View.INVISIBLE
        getFreshNews()
    }

    private fun getNews(isRefreshed: Boolean, page: Int) {
        val apiInterface = ApiClient().getClient()
                .create(ApiInterface::class.java)
        val call: Call<News>
        if (src.equals(""))
            call = apiInterface.getLatestNews(getString(R.string.source), 10, page, getString(R.string.api_key))
        else
            call = apiInterface.getLatestNews(src, 10, page, getString(R.string.api_key))
        call.enqueue(object : Callback<News> {
            override fun onFailure(call: Call<News>?, t: Throwable?) {
                loading = false
                if (progressRecycler.visibility == View.VISIBLE)
                    progressRecycler.visibility = View.GONE
                Log.e(TAG, "Some error occurred: \n${t!!.message}")
                progress.visibility = View.INVISIBLE
                Toast.makeText(root.context, "There was some issue. Please try again later....",
                        Toast.LENGTH_SHORT).show()
                if (isRefreshed)
                    swipeRefresh.isRefreshing = false
            }

            override fun onResponse(call: Call<News>?, response: Response<News>?) {
                loading = false
                if (progressRecycler.visibility == View.VISIBLE)
                    progressRecycler.visibility = View.GONE
                Log.d(TAG, "Total results: ${response!!.body().totalResults}")

                news = response.body()
                progress.visibility = View.INVISIBLE
                articles.addAll(response.body().articles)
                if (isRefreshed)
                    swipeRefresh.isRefreshing = false
                adapter.notifyDataSetChanged()
            }
        })
    }

    fun setSource(src: String) {
        this.src = src
    }

    private fun getFreshNews() {
        val apiInterface = ApiClient().getClient()
                .create(ApiInterface::class.java)
        val call = apiInterface.getLatestNews(getString(R.string.source), 10, 1, getString(R.string.api_key))
        call.enqueue(object : Callback<News> {
            override fun onFailure(call: Call<News>?, t: Throwable?) {
                swipeRefresh.isRefreshing = false
                Log.e(TAG, "Some error occurred: \n${t!!.message}")
                progress.visibility = View.INVISIBLE
                Toast.makeText(root.context, "There was some issue. Please try again later....",
                        Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<News>?, response: Response<News>?) {
                swipeRefresh.isRefreshing = false
                news = response!!.body()
                Log.d(TAG, "Total results: ${response.body().totalResults}")
                progress.visibility = View.INVISIBLE
                articles.clear()
                articles.addAll(response.body().articles)
                adapter.notifyDataSetChanged()
            }
        })
    }
}
