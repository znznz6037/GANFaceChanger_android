package com.example.pal_grad.api.response

import com.example.pal_grad.api.StarGANResult
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

class ApiConfig{

    companion object {
        // base url dari end point.
        const val BASE_URL = "https://550ea0286ce3a5d13349ac2d6e4e9446.m.pipedream.net/"
        const val IMAGE_URL = BASE_URL + "image/"
    }

    // ini retrofit
    private fun retrofit() : Retrofit{
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    // buat sebuah instance untuk call sebuah interface dari retrofit.
    fun instance() : ApiInterface {
        return retrofit().create(ApiInterface::class.java)
    }

}


interface ApiInterface{
    @Multipart
    @POST("https://550ea0286ce3a5d13349ac2d6e4e9446.m.pipedream.net") // end point dari upload
    fun upload(@Part imagename: MultipartBody.Part) : Call<Default>

    @GET("https://550ea0286ce3a5d13349ac2d6e4e9446.m.pipedream.net")
    fun getResult() : Call<StarGANResult>
}