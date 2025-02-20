package br.com.sayurienterprise.photostore.view

import ImageAdapter
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.sayurienterprise.photostore.PhotoStoreApplication
import br.com.sayurienterprise.photostore.PhotoViewModel
import br.com.sayurienterprise.photostore.PhotoViewModelFactory
import br.com.sayurienterprise.photostore.R
import br.com.sayurienterprise.photostore.model.Photo
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainFragment : Fragment() {
    private val TAG: String = MainFragment::class.java.simpleName
    lateinit var edtName: EditText
    lateinit var spnAge: MaterialAutoCompleteTextView
    lateinit var tvDate: TextView
    lateinit var photoFileName: String
    lateinit var recyclerViewImages: RecyclerView
    lateinit var capturePhotoButton: ImageView
    lateinit var imageAdapter: ImageAdapter

    companion object

    val viewModel: PhotoViewModel by viewModels {
        PhotoViewModelFactory((requireActivity().application as PhotoStoreApplication).repository)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        initializeViews(rootView)
        configureViewModel()
        viewModel.logDatabaseContents()
        recyclerViewImages = rootView.findViewById(R.id.recyclerViewPictures)
        recyclerViewImages.layoutManager = LinearLayoutManager(requireContext())
        val imageFiles = getImageFilesFromDownloads()
        imageAdapter = ImageAdapter(imageFiles) { filePath ->
            showImageDialog(filePath)
        }
        recyclerViewImages.adapter = imageAdapter

        capturePhotoButton.setOnClickListener {
            capturePhotoFragment()
        }
        parentFragmentManager.setFragmentResultListener(
            "requestKey",
            viewLifecycleOwner
        ) { requestKey, bundle ->
            val photoFileName = bundle.getString("photoFileName")
            if (photoFileName != null) {
                this.photoFileName = photoFileName
                Log.d(TAG, "onCreateView: this.PhotoFileName: $photoFileName")
                val photo = viewModel.age.value?.let {
                    Photo(
                        name = edtName.text.toString(),
                        age = it,
                        date = viewModel.date.toString(),
                        photoFileName = photoFileName
                    )
                }
                if (photo != null) {
                    viewModel.insert(photo)
                }
            }
        }
        return rootView
    }

    private fun getImageFilesFromDownloads(): List<String> {
        val downloadsFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        if (downloadsFolder.exists() && downloadsFolder.isDirectory) {
            val files = downloadsFolder.listFiles()
            return files?.filter { file ->
                file.isFile && file.name.startsWith("photo_") && file.name.endsWith(
                    ".jpg",
                    ignoreCase = true
                )
            }?.map { file ->
                file.absolutePath
            } ?: emptyList()
        }

        return emptyList()
    }

    private fun showImageDialog(imagePath: String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_image, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        val imageView = dialogView.findViewById<ImageView>(R.id.dialog_image_view)
        val closeIcon = dialogView.findViewById<ImageView>(R.id.close_icon)
        val bitmap = decodeSampledBitmapFromFile(imagePath, imageView.width, imageView.height)
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap)
        } else {
            Toast.makeText(requireContext(), "Imagem não pode ser exibida!", Toast.LENGTH_LONG)
                .show()
        }

        closeIcon.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun decodeSampledBitmapFromFile(
        filePath: String,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap? {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(filePath, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath, options)
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        if (height == 0 || width == 0 || reqWidth == 0 || reqHeight == 0) {
            return 1
        }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun configureViewModel() {
        viewModel.name.observe(viewLifecycleOwner) { name ->
            edtName.setText(name)
        }
        viewModel.age.observe(viewLifecycleOwner) { age ->
            spnAge.setSelection(age ?: 0)
        }
        viewModel.date.observe(viewLifecycleOwner) { date ->
            tvDate.text = date
        }
        viewModel.photoFileName.observe(viewLifecycleOwner) { photoFileName ->
            this.photoFileName = photoFileName
        }
    }

    private fun initializeViews(rootView: View) {
        edtName = rootView.findViewById(R.id.etName)
        spnAge = rootView.findViewById(R.id.spnAge)
        populateAgeDropdown()
        tvDate = rootView.findViewById(R.id.tvDate)
        recyclerViewImages = rootView.findViewById(R.id.recyclerViewPictures)
        recyclerViewImages.layoutManager = LinearLayoutManager(context)
        capturePhotoButton = rootView.findViewById(R.id.capturePhotoButton)
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        tvDate.text = "Data: $currentDate"
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun populateAgeDropdown() {
        val ageList = (1..150).map { it.toString() }
        val ageAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            ageList
        )
        spnAge.setAdapter(ageAdapter)
    }

    private fun capturePhotoFragment() {
        val capturePhotoFragment = CapturePhotoFragment()
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, capturePhotoFragment)
        transaction.addToBackStack("CapturePhotoFragment")
        transaction.commit()
    }

}