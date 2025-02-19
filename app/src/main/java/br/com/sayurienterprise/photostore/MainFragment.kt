package br.com.sayurienterprise.photostore

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainFragment : Fragment() {

    lateinit var edtName: EditText
    lateinit var spnAge: Spinner
    lateinit var tvDate: TextView
    lateinit var recyclerViewImages: RecyclerView
    lateinit var imageFilesList: List<String>

    companion object {
        @JvmStatic
        fun newInstance() : MainFragment {
            return MainFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)

        edtName = rootView.findViewById(R.id.etName)
        spnAge = rootView.findViewById(R.id.spnAge)
        populateAgeSpinner()
        tvDate = rootView.findViewById(R.id.tvDate)
        recyclerViewImages = rootView.findViewById(R.id.recyclerViewPictures)
        recyclerViewImages.layoutManager = LinearLayoutManager(context)

        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        tvDate.text = "Data: $currentDate"

        loadImagesFromDownloads()

        val capturePhotoButton: View = rootView.findViewById(R.id.capturePhotoButton)
        capturePhotoButton.setOnClickListener {
            capturePhotoFragment()
        }

        return rootView
    }

    fun populateAgeSpinner(){
        val ageList = (1..150).toList()
        val spinnerAgeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            ageList
        )
        spinnerAgeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spnAge.adapter = spinnerAgeAdapter
    }

    private fun loadImagesFromDownloads() {
        val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val files = downloadsFolder.listFiles()

        imageFilesList = ArrayList()

        files?.forEach { file ->
            if (file.isFile && (file.name.endsWith(".jpg"))) {
                (imageFilesList as ArrayList).add(file.name)
            }
        }

        val adapter = ImageAdapter(imageFilesList)
        recyclerViewImages.adapter = adapter
    }

    private fun capturePhotoFragment() {
        val capturePhotoFragment = CapturePhotoFragment()
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, capturePhotoFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}