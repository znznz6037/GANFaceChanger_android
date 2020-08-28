package com.example.pal_grad.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pal_grad.R

class Selfie2Anime : Fragment() {
    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.selfie2anime_change_fragment, container)
    }

    companion object {
        fun create(): Selfie2Anime {
            return Selfie2Anime()
        }
    }
}