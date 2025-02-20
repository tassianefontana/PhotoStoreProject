package br.com.sayurienterprise.photostore.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import br.com.sayurienterprise.photostore.PhotoStoreApplication
import br.com.sayurienterprise.photostore.R
import java.io.File
import java.io.FileOutputStream


class CapturePhotoFragment : Fragment() {
    private val TAG: String = CapturePhotoFragment::class.java.simpleName
    lateinit var cameraManager: CameraManager
    lateinit var textureView: TextureView
    lateinit var mCameraCaptureSession: CameraCaptureSession
    lateinit var mCameraDevice: CameraDevice
    lateinit var handler: Handler
    lateinit var mCaptureRequest: CaptureRequest.Builder
    private lateinit var handlerThread: HandlerThread
    lateinit var imageReader: ImageReader

    companion object;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_capture_photo, container, false)
        textureView = rootView.findViewById(R.id.textureView)
        cameraManager = requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        handlerThread = HandlerThread("cameraThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        imageReader =
            (requireActivity().application as PhotoStoreApplication).cameraHelper.setupImageReader()
        imageReader.setOnImageAvailableListener({ imageReader ->
            val image = imageReader.acquireLatestImage()
            var message = ""
            if (image != null) {
                try {
                    val buffer = image.planes[0].buffer
                    val bytes = ByteArray(buffer.remaining())
                    buffer.get(bytes)

                    val downloadsFolder =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val fileName = "photo_${System.currentTimeMillis()}.jpg"
                    val resultBundle = bundleOf("photoFileName" to fileName)
                    parentFragmentManager.setFragmentResult("requestKey", resultBundle)
                    val file = File(downloadsFolder, fileName)

                    val opStream = FileOutputStream(file)
                    opStream.write(bytes)
                    opStream.close()
                    message = getString(R.string.photo_saved_in_downloads)
                    parentFragmentManager.popBackStack()
                } catch (e: Exception) {
                    Log.e(TAG, "Error saving image: ${e.message}")
                    message = getString(R.string.error_saving_photo)
                } finally {
                    image.close()
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                }
            }
        }, handler)

        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {}
            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {}
        }

        rootView.findViewById<Button>(R.id.btnCamera).apply {
            setOnClickListener {
                mCaptureRequest =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                mCaptureRequest.addTarget(imageReader.surface)
                mCameraCaptureSession.capture(mCaptureRequest.build(), null, handler)
            }
        }
        return rootView
    }

    fun openCamera() {
        val cameraId = cameraManager.cameraIdList[0]
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(cameraDevice: CameraDevice) {
                mCameraDevice = cameraDevice
                mCaptureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                val surface = Surface(textureView.surfaceTexture)
                mCaptureRequest.addTarget(surface)

                cameraDevice.createCaptureSession(
                    listOf(surface, imageReader.surface),
                    object : CameraCaptureSession.StateCallback() {
                        override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                            mCameraCaptureSession = cameraCaptureSession
                            try {
                                cameraCaptureSession.setRepeatingRequest(
                                    mCaptureRequest.build(), null, handler
                                )
                            } catch (e: Exception) {
                                Log.e(TAG, "Error configuring repeating capture: ${e.message}")
                            }
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {
                            Log.e(TAG, "Failed to configure capture session")
                        }
                    },
                    handler
                )
            }

            override fun onDisconnected(cameraDevice: CameraDevice) {
                cameraDevice.close()
                mCameraDevice = cameraDevice
            }

            override fun onError(cameraDevice: CameraDevice, error: Int) {
                cameraDevice.close()
                mCameraDevice = cameraDevice
                Log.e(TAG, "Error opening camera: $error")
            }
        }, handler)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::mCameraDevice.isInitialized) {
            mCameraDevice.close()
        }
        handler.removeCallbacksAndMessages(null)
        handlerThread.quitSafely()
    }
}