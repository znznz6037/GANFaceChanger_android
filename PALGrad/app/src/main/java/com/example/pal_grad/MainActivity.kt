package com.example.pal_grad


import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pal_grad.api.StarGANAPI

import com.example.pal_grad.api.StarGANResult
import com.example.pal_grad.api.response.ApiConfig
import com.example.pal_grad.api.response.Default
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import com.example.pal_grad.fragment.ResourceStore
import okhttp3.MultipartBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        setViewPager()
        setTab()


        this.window.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = Color.WHITE
        }
    }
    override fun onBackPressed() {
        finish()
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

    public fun upload(imagename: MultipartBody.Part){
        // init retrofit
        val call = ApiConfig().instance().upload(imagename)

        // membaut request ke api
        call.enqueue(object : retrofit2.Callback<Default>{

            // handling request saat fail
            override fun onFailure(call: Call<Default>?, t: Throwable?) {
                Toast.makeText(applicationContext,"Connection error", Toast.LENGTH_SHORT).show()
                Log.d("ONFAILURE",t.toString())
                println("실패")
            }
            // handling request saat response.
            override fun onResponse(call: Call<Default>?, response: Response<Default>?) {
                // menampilkan pesan yang diambil dari response.
                Toast.makeText(applicationContext,response?.body()?.message, Toast.LENGTH_SHORT).show()
                println("성공")
                // saat pesan mempunyai kata 'success' maka tutup/akhiri activity ini.
            }
        })
    }
}

