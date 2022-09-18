package com.example.messagingtest

import android.content.Context
import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.apollographql.apollo3.network.ws.GraphQLWsProtocol
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

private var instance: ApolloClient? = null

fun apolloClient(context: Context): ApolloClient {
    val token = User.getToken(context)


    if (instance != null) {
        return instance!!
    }

    Log.d("tokenToken", token!!)

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthorizationInterceptor(context))
        .build()

    instance = ApolloClient.Builder()
        .httpServerUrl("https://dev-api.univerlive.com/graphql")
        .webSocketServerUrl("wss://dev-api.univerlive.com/graphql")
        .wsProtocol(GraphQLWsProtocol.Factory(
            connectionPayload = {
                Log.d("connectionPayload", "connectionPayload")
                mapOf("headers" to mapOf("authorization" to "Bearer $token"))
            }
        ))
        .okHttpClient(okHttpClient)
        .build()

    return instance!!
}

private class AuthorizationInterceptor(val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = User.getToken(context)
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}