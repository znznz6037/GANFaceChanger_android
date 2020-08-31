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
            println("what is i" + i)
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
            println("picked Image?" + pickedImg)
        }
    }
}