package com.tasty.recipesapp.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Clone the original request
        val request = chain.request().newBuilder()
            // Add Authorization header with Bearer token
            .addHeader("Authorization", "Bearer $token")
            .build()

        // Proceed with the modified request
        return chain.proceed(request)
    }
}