package com.example.pal_grad.api.response

import com.example.pal_grad.api.AnimePost
import com.example.pal_grad.api.StarGANPost
import com.example.pal_grad.api.StarGANResult
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.concurrent.TimeUnit

class ApiConfig{

    companion object {
        // base url dari end point.
        const val BASE_URL = "https://psbgrad.duckdns.org:5000/"
    }
    // ini retrofit
    private fun retrofit() : Retrofit{
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()

    }
    // buat sebuah instance untuk call sebuah interface dari retrofit.
    fun instance() : ApiInterface {
        return retrofit().create(ApiInterface::class.java)
    }
}

var okHttpClient: OkHttpClient? = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .build()

val gson : Gson = GsonBuilder()
    .setLenient()
    .create()

interface ApiInterface{
    @Multipart
    @POST("upload") // end point dari upload
    fun upload(@Part("style") style: String,
               @Part file: MultipartBody.Part) : Call<StarGANPost>

    @GET("result")
    fun getResult() : Call<StarGANResult>

    @Multipart
    @POST("uploadAnime")
    fun animeupload(@Part file: MultipartBody.Part) : Call<AnimePost>

    @GET("anime")
    fun getAnimeResult() : Call<StarGANResult>
}