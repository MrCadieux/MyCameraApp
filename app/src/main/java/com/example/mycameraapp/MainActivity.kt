package com.example.mycameraapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File


private const val REQUEST_CODE = 1
private lateinit var myPhoto: File
private var myPhotoName = "Photo"

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    lateinit var pictureTaker: Button
    lateinit var picture: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pictureTaker = findViewById(R.id.pictureButton)
        picture = findViewById(R.id.myImage)

        pictureTaker.setOnClickListener{
            val intentPics = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            myPhoto = getPhotoFromCamera(myPhotoName)
            //security portion
            val newProvider = FileProvider.getUriForFile(this, "com.example.mycameraapp.fileprovider", myPhoto)
            intentPics.putExtra(MediaStore.EXTRA_OUTPUT, newProvider)

            if (intentPics.resolveActivity(this.packageManager)!=null){
                startActivityForResult(intentPics,REQUEST_CODE)
            } else {
                //there is an error
                Toast.makeText(this, "No Camera", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun getPhotoFromCamera(fileName: String): File {
        val localDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", localDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            //val LQimg = data?.extras?.get("data") as Bitmap
            val HQimg = BitmapFactory.decodeFile(myPhoto.absolutePath)
            picture.setImageBitmap(HQimg)
        }


        super.onActivityResult(requestCode, resultCode, data)
    }


}