package promact.akansh.com.newsapp.Network

import android.util.Log
import retrofit2.Response
import java.io.IOException

abstract class ApiCallback<T> {
    val tag = this.javaClass.simpleName!!
    private fun parseResponse(response: Response<*>): ApiResponse {
        return ApiResponse(response.code(), response.message())
    }


    internal fun onResponse(response: Response<T>) {
        val apiResponse = parseResponse(response)
        if (apiResponse.isResponseOk()) {
            onSuccess(response.body())
        } else {
            try {
                apiResponse.errorBody = response.errorBody().string()
            } catch (e: IOException) {
                Log.e(tag, e.toString())
            }
            onError(apiResponse)
        }
    }


    internal fun onFailure(throwable: Throwable) {
        onException(throwable)
    }

    abstract fun onSuccess(responseBody: T)

    abstract fun onError(errorBody: ApiResponse)

    abstract fun onException(error: Throwable)


}