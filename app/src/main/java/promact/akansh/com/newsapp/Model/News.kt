package promact.akansh.com.newsapp.Model

/**
 * Created by Akansh on 01-06-2018.
 */
data class News(
    val status: String,
    val totalResults: Int,
    val articles: ArrayList<ArticlesParams>)