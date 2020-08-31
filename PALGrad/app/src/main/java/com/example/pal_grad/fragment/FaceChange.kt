package com.example.pal_grad.fragment


import android.content.Intent
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.pal_grad.MainActivity
import com.example.pal_grad.R
import com.vincent.filepicker.Constant
import com.vincent.filepicker.Constant.REQUEST_CODE_PICK_IMAGE
import com.vincent.filepicker.Constant.RESULT_PICK_IMAGE
import com.vincent.filepicker.activity.ImagePickActivity
import com.vincent.filepicker.filter.entity.ImageFile

import kotlinx.android.synthetic.main.face_change_fragment.*
import kotlinx.android.synthetic.main.face_change_fragment.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File



class FaceChange : Fragment() {
    lateinit var imagename: MultipartBody.Part

    fun openGalleryForImage() {
            val i = Intent(activity, ImagePickActivity::class.java)
            i.putExtra(Constant.MAX_NUMBER, 1)
            startActivityForResult(i, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.face_change_fragment, container, false)
        view.face_upload_button.setOnClickListener {
            openGalleryForImage()
        }
        view.sendButton.setOnClickListener{
            (activity as MainActivity).upload(imagename)
        }
        view.getImage.setOnClickListener{
            /*glideOpenForImage()*/
        }
/*        view.api_test.setOnClickListener {
            (activity as MainActivity).apiTest()
        }*/
        return view
    }

    companion object {
        fun create(): FaceChange {
            return FaceChange()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            val pickedImg = data.getParcelableArrayListExtra<ImageFile>(RESULT_PICK_IMAGE)[0]?.path
            val requestBody = RequestBody.create(MediaType.parse("multipart"), File(pickedImg))
            imagename = MultipartBody.Part.createFormData("imagename", File(pickedImg)?.name, requestBody)
            Glide.with(this).load(pickedImg).into(faceImageView)
        }
    }

/*    fun glideOpenForImage() {
        val url = "https://pipedream-catcher-bodies.s3.amazonaws.com/88770bc5-e5d0-406b-b363-d804ad620ebb?AWSAccessKeyId=ASIA5F5AGIEAVPQ55KLU&Expires=1598867649&Signature=fMlcozEqdPVhjFdLgu3%2FpaPLR4I%3D&x-amz-security-token=FwoGZXIvYXdzEHMaDCn1eOWmgFMLOQMGqSKNBF87VfHF0HIL%2BuWPGC5WCFsiCM2Wg4NGIMJFPdwf0SspYMFyfGBoMYoJ5quzUhwLe6rc%2BZt%2B7EsIb%2BSVos3H3M7P7YUDurvXFrXt0GXzNDQDe9NQbfksxfKxiWMRhTtq64zwYQ4Oda7fH%2BNRbHW9WgPRgyBg1%2Byc7uQ41bYyuPfPZuxhlyeV0VbXXCadAhQu3zHZ7LzWSwNVKlNwqYLXZSohwF9zZ%2FyWqX2CSVk0RAukBpO16xIRWSyCZfWVevSQWnKD61%2FkoDHNBoynswZUTbT3ag7G7h0peF15PrL1ozY%2FKHmY%2BuDut0xabcbpvaTr338NFemDZL1n3yGIVhqyITXN7YusUWuhwkcen%2BIwU3glA4wrlIrylk%2F%2BvFt18JLGwCu4MKQ53EluZN2WdOPQlewrtyOlkiJ1cAs2mZjmMpYT2VDrysQt5vLgwp0raQxvErRN9PgbuoyQz%2BYUAbR9FUqqlYhixryYw%2FQ2lEXTFVQB0ZJ1ZYKXvfZjNuV7311YRTUbosc9rY8YDFSRn3veMczMnTrqNrGZuiGzG%2B%2BHn4AZLbTl%2BVwQPxryz16H%2BcJqzGy7IhH8aBrndka0fRjLIs0i6Z538VtDv140OXBAgEVloVA1EhHaLkOb0h0XChMOEk0Z8WUmjNo17%2FD1OGoe%2BrTApOU7Vffh6P5dqvJRiNre2%2BpSxEaKisAOYACDSii5g7P6BTIqyPsKKAC8VfLlmhCVnEK4Q48aB4%2FWRCgCAJ60lci17opqi6uGSyeMnHBn"
        Glide.with(this).load(url).into(faceChangeView)

    }*/
}