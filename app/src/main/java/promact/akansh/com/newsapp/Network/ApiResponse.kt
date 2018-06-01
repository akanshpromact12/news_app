package promact.akansh.com.newsapp.Network

import java.net.HttpURLConnection

/**
 * Created by Akansh on 01-06-2018.
 */
data class ApiResponse(val statusCode: Int, val message: String) {

    lateinit var errorBody: String

    fun isResponseOk(): Boolean {
        return statusCode == HttpURLConnection.HTTP_OK
    }
}