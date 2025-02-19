package br.com.sayurienterprise.photostore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class CapturePhotoFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = CapturePhotoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_capture_photo, container, false)
    }

}