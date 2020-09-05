package com.example.pal_grad.fragment

import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pal_grad.R
import com.example.pal_grad.api.AnimePost
import com.example.pal_grad.api.StarGANPost
import com.example.pal_grad.api.StarGANResult
import com.example.pal_grad.api.response.ApiConfig
import kotlinx.android.synthetic.main.face_change_fragment.*
import kotlinx.android.synthetic.main.face_change_fragment.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

class Selfie2Anime : Fragment() {
    lateinit var file2: MultipartBody.Part
    private var str : String? = null
    private var uriPath : String?  = ""
    private val OPEN_GALLERY = 1
    lateinit var base64: String


    fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, OPEN_GALLERY)
    }

    private fun getRealPathFromURI(uri: Uri?): String? {
        val projection =
            arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = activity?.managedQuery(uri, projection, null, null, null)
        val column_index = cursor
            ?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return column_index?.let { cursor?.getString(it) }
    }

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.selfie2anime_change_fragment, container, false)
        view.face_upload_button.setOnClickListener {
            openGalleryForImage()
        }
        view.sendButton.setOnClickListener{
            upload(file2)
        }
        return view
    }

    companion object {
        fun create(): Selfie2Anime {
            return Selfie2Anime()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            faceImageView.setImageURI(data?.data) // handle chosen image
            val selectedImageUri = data!!.data
            uriPath = getRealPathFromURI(selectedImageUri)
            var file = File(uriPath)
            /*val pickedImg = data.getParcelableArrayListExtra<ImageFile>(RESULT_PICK_IMAGE)[0]?.path*/
            val requestBody = RequestBody.create(MediaType.parse("CONTENT_TYPE"), file)
            file2 = MultipartBody.Part.createFormData("file", file.name, requestBody)
            /*Glide.with(this).load(pickedImg).into(faceImageView)*/
        }
    }

    fun apiTest(){
        val call = ApiConfig().instance().getAnimeResult()
        call.enqueue(object : retrofit2.Callback<StarGANResult> {

            override fun onResponse(
                call: retrofit2.Call<StarGANResult>,
                response: Response<StarGANResult>
            ) {
                Log.d("결과:", "성공 : ${response.body().toString()}")
                var base64_All = response?.body().toString()
                base64 = base64_All.replace("StarGANResult(img=", "")
            }

            override fun onFailure(call: retrofit2.Call<StarGANResult>, t: Throwable) {
                Log.d("결과:", "실패 : $t")
            }
        })
    }

    fun base64ForImage() {
        faceImageView.setImageBitmap(convertString64ToImage(resizeBase64Image(base64)))
    }

    fun resizeBase64Image(base64image: String): String {
        val encodeByte: ByteArray = Base64.decode(base64image.toByteArray(), Base64.DEFAULT)
        val options = BitmapFactory.Options()
        var image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size, options)
        image = Bitmap.createScaledBitmap(image, 1024, 600, false)
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        System.gc()
        return Base64.encodeToString(b, Base64.NO_WRAP)
    }

    private fun convertString64ToImage(base64String: String): Bitmap {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    fun upload(file: MultipartBody.Part){
        // init retrofit
        val call = ApiConfig().instance().animeupload(file)

        // membaut request ke api
        call.enqueue(object : retrofit2.Callback<AnimePost>{

            // handling request saat fail
            override fun onFailure(call: retrofit2.Call<AnimePost>?, t: Throwable?) {
                Toast.makeText(activity,"Connection error", Toast.LENGTH_SHORT).show()
                Log.d("ONFAILURE",t.toString())

            }
            // handling request saat response.
            override fun onResponse(call: retrofit2.Call<AnimePost>?, response: Response<AnimePost>?) {
                // menampilkan pesan yang diambil dari response.
                Toast.makeText(activity, "로딩이 완료되었습니다", Toast.LENGTH_SHORT).show()
                apiTest()
                base64ForImage()
            }
        })
    }
}