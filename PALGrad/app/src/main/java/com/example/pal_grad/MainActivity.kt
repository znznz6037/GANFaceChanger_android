package com.example.pal_grad

import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pal_grad.api.StarGANAPI

import com.example.pal_grad.api.StarGANResult
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import com.example.pal_grad.fragment.ResourceStore

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var imageData: ByteArray? = null
    //private val postURL: String = "https://550ea0286ce3a5d13349ac2d6e4e9446.m.pipedream.net" // remember to use your own api
    private val postURL: String = "https://psbgrad.duckdns.org:5000/upload" // remember to use your own api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        setViewPager()
        setTab()
        apiTest()

        this.window.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = Color.WHITE
        }
    }
    override fun onBackPressed() {
        finish()
    }
    fun checkPermission(permissions: Array<out String>, flag: Int): Boolean {
        for(permission in permissions) {
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, permissions, flag)
                return false
            }
        }
        return true
    }
    private fun setViewPager() {
        viewpager.adapter = object : FragmentStateAdapter(this) {

            override fun createFragment(position: Int): Fragment {
                return ResourceStore.pagerFragments[position]
            }

            override fun getItemCount(): Int {
                return ResourceStore.tabList.size
            }
        }
    }
    private fun setTab() {
        TabLayoutMediator(tab_layout, viewpager) { tab : TabLayout.Tab, position ->
            tab.text = ResourceStore.tabList[position]
            main_title.setOnClickListener(){
                viewpager.setCurrentItem(TabLayout.Tab.INVALID_POSITION, true)
            }
        }.attach()
    }

/*    fun uploadImage() {
        imageData?: return
        val request = object : VolleyFileUploadRequest(
                Method.POST,
                postURL,
                Response.Listener {
                    println("response is: $it")
                },
                Response.ErrorListener {
                    println("error is: $it")
                }
        ) {
            override fun getByteData(): MutableMap<String, FileDataPart> {
                var params = HashMap<String, FileDataPart>()
                params["imageFile"] = FileDataPart("image", imageData!!, "jpeg")
                return params
            }
        }
        Volley.newRequestQueue(this).add(request)
    }

    @Throws(IOException::class)
    fun createImageData(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        inputStream?.buffered()?.use {
            imageData = it.readBytes()
        }
    }*/
fun apiTest(){
    val url = "https://psbgrad.duckdns.org:5000"

    val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val api = retrofit.create(StarGANAPI::class.java)
    val test = api.getResult()

    test.enqueue(object : Callback<StarGANResult> {
        override fun onResponse(
                call: Call<StarGANResult>,
                response: Response<StarGANResult>
        ) {
            Log.d("결과", "성공 : ${response.body().toString()}")
        }

        override fun onFailure(call: Call<StarGANResult>, t: Throwable) {
            Log.d("결과:", "실패 : $t")
        }
    })
}
}


}