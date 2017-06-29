package com.lv.gankio.net

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject

/**
 * Date: 2017-06-21
 * Time: 15:30
 * Description:
 */
class ProtocolInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        chain ?: throw RuntimeException("this Interceptor.Chain is empty")
        val response = chain.proceed(chain.request())
        val medizType = MediaType.parse("application/json; chartset='utf-8'")
        val data = parseDataFromBody(response.body()?.string())
        return response.newBuilder().body(ResponseBody.create(medizType, data)).build()
    }

    private fun parseDataFromBody(body: String?): String {
        try {
            val json = JSONObject(body)
            val error = json.optBoolean("error", false)
            if (!error)
                return json.optString("results")
            throw HttpException(-1, json.optString("msg"))
        } catch (e: Exception) {
            throw  HttpException(-2, if (e is HttpException) e.message else "数据解析错误,请稍后重试!")
        }
    }

}