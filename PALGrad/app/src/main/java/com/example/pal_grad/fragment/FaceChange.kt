package com.example.pal_grad.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.pal_grad.MainActivity
import com.example.pal_grad.R

import kotlinx.android.synthetic.main.face_change_fragment.*
import kotlinx.android.synthetic.main.face_change_fragment.view.*
import java.io.IOException
import java.lang.reflect.Method
import java.security.Permission
import java.util.jar.Manifest


class FaceChange : Fragment() {
    private val OPEN_GALLERY = 1
    private val OPEN_CAMERA = 2

    private fun openGalleryForImage() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, OPEN_GALLERY)
    }
    private fun captureCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, OPEN_CAMERA)
    }


    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater!!.inflate(R.layout.face_change_fragment, container, false)
        view.face_upload_button.setOnClickListener {
            openGalleryForImage()
        }
        view.api_test.setOnClickListener {
            (activity as MainActivity).apiTest()
        }
        return view
    }


    companion object {
        fun create(): FaceChange {
            return FaceChange()
        }
    }
}