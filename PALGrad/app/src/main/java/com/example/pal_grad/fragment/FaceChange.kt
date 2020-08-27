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
import com.android.volley.Request.Method.POST
import com.android.volley.Response
import com.android.volley.toolbox.Volley
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
        val view:View = inflater.inflate(R.layout.face_change_fragment, container, false)
            view.face_upload_gallery.setOnClickListener { view->
                openGalleryForImage()
        }
            view.face_upload_camera.setOnClickListener{ view ->
                captureCamera()
        }
            view.sendButton.setOnClickListener{
                view -> (activity as MainActivity).uploadImage()
            }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            when(requestCode) {
                OPEN_GALLERY -> {
                    val uri = data?.data
                    if (uri != null) {
                        faceImageView.setImageURI(uri) // handle chosen image
                        (activity as MainActivity).createImageData(uri)
                    }
                }
                OPEN_CAMERA -> {
                    faceImageView.setImageBitmap(data?.extras?.get("data") as Bitmap)
                }
            }
        }
    }

    companion object {
        fun create(): FaceChange {
            return FaceChange()
        }
    }
}