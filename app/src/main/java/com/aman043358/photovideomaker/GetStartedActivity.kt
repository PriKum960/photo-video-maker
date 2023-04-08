package com.aman043358.photovideomaker

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aman043358.photovideomaker.databinding.ActivityGetStartedBinding
import com.aman043358.photovideomaker.databinding.LayoutPermissionBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class GetStartedActivity : AppCompatActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                openActivity()
            } else {
                showPermissionBottomSheet()
            }
        }
    val STORAGE_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
    lateinit var binding: ActivityGetStartedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetStarted.setOnClickListener {
            requestStoragePermission(it)
        }
    }

    fun requestStoragePermission(view: View) {
        when {
            isStoragePermissionAllowed() -> openActivity()
            shouldShowRequestPermissionRationale() -> showPermissionBottomSheet()
            else -> requestPermissionLauncher.launch(STORAGE_PERMISSION)
        }
    }

    private fun showPermissionBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = LayoutPermissionBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setCancelable(false)
        binding.btnAllow.setOnClickListener {
            requestPermissionLauncher.launch(STORAGE_PERMISSION)
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    fun isStoragePermissionAllowed() = ContextCompat.checkSelfPermission(
        this@GetStartedActivity,
        STORAGE_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

    fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            STORAGE_PERMISSION
        )

    private fun openActivity() {
        startActivity(Intent(this@GetStartedActivity, MainActivity::class.java))
    }
}