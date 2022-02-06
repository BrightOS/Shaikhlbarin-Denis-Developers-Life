package ru.followy.util.followy_extensions.api

import android.content.Context
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import ru.brightos.shaikhlbarindenisdeveloperslife.util.api.NoConnectionInterceptor
import ru.brightos.shaikhlbarindenisdeveloperslife.util.api.Response
import java.util.concurrent.TimeUnit

/**
 * APIUtils is a class that simplifies the process of sending requests.
 * @author BrightOS
 * @since February 2022
 */
class APIUtils {
    companion object {

        /**
         * This is a method to send a Post request using JSON or "x-www-form-urlencoded" schemas
         * @param url = url link
         * @param jsonObject = a JSON object that contains all the required arguments
         * @param auth = Bearer token. May be null
         * @param contentType = request schema type
         */
        internal fun post(
            context: Context,
            url: String,
            jsonObject: JSONObject,
            auth: String?,
            contentType: String
        ): Response =
            runBlocking {
                val response = GlobalScope.async {
                    val request = async { preparePostRequest(url, jsonObject, auth, contentType) }
                    val response = async { request.await()?.let { sendRequest(context, it) } }

                    return@async response.await()!!
                }.await()

                val code = response.code
                val body = response.body?.string()

                response.close()

                return@runBlocking Response(code, body!!)
            }

        /**
         * This is a method to send a Get request to the server using a URL and a Bearer token
         * @param url = url link with all Query arguments
         * @param auth = Bearer token
         */
        internal fun get(
            context: Context,
            url: String,
            auth: String
        ): Response =
            runBlocking {
                val response = GlobalScope.async {
                    val request = async { prepareGetRequest(url, auth) }
                    val response = async { sendRequest(context, request.await()) }

                    return@async response.await()
                }.await()

                val code = response.code
                val body = response.body?.string()

                response.close()

                return@runBlocking Response(code, body!!)
            }

        /**
         * This is a method to send a Delete request to the server using a URL and a Bearer token
         * @param url = url link with all Query arguments
         * @param auth = Bearer token
         */
        internal fun delete(
            context: Context,
            url: String,
            auth: String
        ): Response =
            runBlocking {
                val response = GlobalScope.async {
                    val request = async { prepareDeleteRequest(url, auth) }
                    val response = async { sendRequest(context, request.await()) }

                    return@async response.await()
                }.await()

                val code = response.code
                val body = response.body?.string()

                response.close()

                return@runBlocking Response(code, body!!)
            }

        /**
         * This is a method to send a Put request to the server using a URL and a Bearer token
         * @param url = url link with all Query arguments
         * @param auth = Bearer token
         */
        internal fun put(
            context: Context,
            url: String,
            jsonObject: JSONObject,
            auth: String
        ): Response =
            runBlocking {
                val response = GlobalScope.async {
                    val request = async { preparePutRequest(url, jsonObject, auth) }
                    val response = async { sendRequest(context, request.await()) }

                    return@async response.await()
                }.await()

                val code = response.code
                val body = response.body?.string()

                response.close()

                return@runBlocking Response(code, body!!)
            }

        private fun jsonToRequestBody(jsonObject: JSONObject) =
            "$jsonObject".toRequestBody("application/json; charset=utf-8".toMediaType())

        private fun jsonToFormRequestBody(jsonObject: JSONObject): FormBody {
            val requestBody = FormBody.Builder()
            val temp: Iterator<String> = jsonObject.keys()
            var key: String
            while (temp.hasNext()) {
                key = temp.next()
                val value: Any = jsonObject.get(key)
                requestBody.add(key, value as String)
            }
            return requestBody.build()
        }

        private fun preparePostRequest(
            url: String,
            jsonObject: JSONObject,
            auth: String?,
            contentType: String
        ): Request? {
            when (contentType) {
                JSON ->
                    if (auth == null)
                        return Request.Builder()
                            .url("${APIConfig.url}$url")
                            .post(jsonToRequestBody(jsonObject))
                            .build()
                    else
                        return Request.Builder()
                            .url("${APIConfig.url}$url")
                            .addHeader("Authorization", "Bearer $auth")
                            .post(jsonToRequestBody(jsonObject))
                            .build()

                URL_ENCODED -> {
                    if (auth == null)
                        return Request.Builder()
                            .url("${APIConfig.url}$url")
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .post(jsonToFormRequestBody(jsonObject))
                            .build()
                    else
                        return Request.Builder()
                            .url("${APIConfig.url}$url")
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .addHeader("Authorization", "Bearer $auth")
                            .post(jsonToFormRequestBody(jsonObject))
                            .build()
                }

                else -> return null
            }
        }

        private fun prepareGetRequest(url: String, auth: String) =
            Request.Builder()
                .url("${APIConfig.url}$url")
                .addHeader("Authorization", "Bearer $auth")
                .addHeader("charset", "utf-8")
                .get()
                .build()

        private fun prepareDeleteRequest(url: String, auth: String) =
            Request.Builder()
                .url("${APIConfig.url}$url")
                .addHeader("Authorization", "Bearer $auth")
                .addHeader("charset", "utf-8")
                .delete()
                .build()

        private fun preparePutRequest(url: String, jsonObject: JSONObject, auth: String) =
            Request.Builder()
                .url("${APIConfig.url}$url")
                .addHeader("Authorization", "Bearer $auth")
                .put(jsonToRequestBody(jsonObject))
                .build()

        private fun sendRequest(context: Context, request: Request) = OkHttpClient
            .Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .callTimeout(2, TimeUnit.MINUTES)
//            .addInterceptor(NoConnectionInterceptor(context))
            .retryOnConnectionFailure(true)
            .build()
            .newCall(request)
            .execute()

        const val JSON = "application/json"
        const val URL_ENCODED = "application/x-www-form-urlencoded"
    }
}
