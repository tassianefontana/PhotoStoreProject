package br.com.sayurienterprise.photostore.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.sayurienterprise.photostore.PhotoStoreApplication
import br.com.sayurienterprise.photostore.R

class MainActivity : AppCompatActivity() {
    private val TAG: String? = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()

        val db = (applicationContext as PhotoStoreApplication).database
        Log.d(TAG, "Database instance: ${db.openHelper.databaseName}")

        val permissionsHelper = (applicationContext as PhotoStoreApplication).permissionsHelper

        permissionsHelper.permissionsGranted.observe(this) { granted ->
            if (granted) {
                if (savedInstanceState == null) {
                    openFragment(MainFragment())
                }
            } else {
                Toast.makeText(this, "Alguma permissão não foi concedida!", Toast.LENGTH_LONG)
                    .show()
            }
        }
        permissionsHelper.requestPermissionsIfNecessary(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                (applicationContext as PhotoStoreApplication).permissionsHelper
            .onRequestPermissionsResult(requestCode)
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}