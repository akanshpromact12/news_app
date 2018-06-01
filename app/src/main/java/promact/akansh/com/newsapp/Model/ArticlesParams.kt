package promact.akansh.com.newsapp.Model

/**
 * Created by Akansh on 01-06-2018.
 */
data class ArticlesParams (
    val source: SourceParams,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String)