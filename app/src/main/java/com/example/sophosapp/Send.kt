package com.example.sophosapp


import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.sophos.userslogin.db_maps
import com.example.sophos.userslogin.doc_service
import com.example.sophosapp.Delivery_Documents.Post_doc
import com.example.sophosapp.SophosApplication.Companion.prefs
import com.example.sophosapp.databinding.FragmentSendBinding
import com.example.sophosapp.Maps_and_cities_Api.maps_Items
import com.google.android.material.textfield.TextInputEditText
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File


class Send : Fragment() {
    lateinit var binding: FragmentSendBinding
    lateinit var stringImage: String
    private val CAMERA_REQUEST_CODE = 1
    private val GALLERY_REQUEST_CODE = 2


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSendBinding.inflate(inflater, container, false)

        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cityList = mutableSetOf<String>()

        //--Button to use camera--//
        binding.cameraView.setOnClickListener {
            cameraCheckPermission(context as AppCompatActivity)
        }

        //--Button to use Gallery--//
        binding.btnGallery.setOnClickListener {
            galleryCheckPermission()
        }

        //--Call to Api for the list of cities and countries--//
        db_maps.service_map.listcity()
            .enqueue(object : Callback<maps_Items> {
                override fun onResponse(call: Call<maps_Items>, response: Response<maps_Items>) {
                    if (response.isSuccessful) {
                        Log.i("RESPONSE", response.body().toString())
                        val bodycities = response.body()!!.Items
                        for (city in bodycities) {
                            city.Ciudad
                            cityList.add(city.Ciudad)
                        }
                        val arrayAdapter2 =
                            ArrayAdapter(requireContext(), R.layout.list_item, cityList.toList())
                        binding.dropmenuCity.setAdapter(arrayAdapter2)
                    }
                }

                override fun onFailure(call: Call<maps_Items>, t: Throwable) {
                    call.cancel()
                }

            })

        //--List for Document-type--//
        val list = resources.getStringArray(R.array.List_type_doc)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.list_item, list)
        binding.dropmenuDoc.setAdapter(arrayAdapter)

        //--Get email for field--//
        val emails = prefs.getEmail()
        val setEmail: TextInputEditText = binding.EmailBox
        setEmail.setText(emails)

        //--Button Send-Listener--//
        binding.btnSend.setOnClickListener {
            val image = stringImage
            val lastnames = binding.LastNameBox.text.toString()
            val citiesdrop = binding.dropmenuCity.text.toString()
            val email = binding.EmailBox.text.toString()
            val doc_num = binding.numberDocument.text.toString()
            val names = binding.NameBox.text.toString()
            val type_d = binding.dropmenuDoc.text.toString()
            val type_attach = binding.typeAttached.text.toString()
            val body =createbody(type_d, doc_num, names, lastnames, citiesdrop, email, type_attach, image)
            send_documentation(body)
        }

        //--Pop-up menu Listener--//
        binding.popup.setOnClickListener { v: View ->
            showMenu(v, R.menu.menu_item)
        }

    //--Fragment transition to Menu-Screen--//
        binding.backToMenu.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, menu_screen())
                commit()
            }
        }


    }

    //--Gallery Permissions--//
    private fun galleryCheckPermission() {

        Dexter.withContext(activity).withPermission(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                gallery()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(
                    activity,
                    "You have denied the storage permission to select image",
                    Toast.LENGTH_SHORT
                ).show()

                showRotationalDialogForPermission(context!!)

            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                showRotationalDialogForPermission(context!!)
            }


        }).onSameThread().check()
    }

    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }
    //--Camera Permissions--//
    private fun cameraCheckPermission(activity: AppCompatActivity) {
        Dexter.withContext(activity)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener {

                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            camera()
                        }
                        if (report.isAnyPermissionPermanentlyDenied) {
                            Toast.makeText(activity, "Without permissions", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    showRotationalDialogForPermission(context!!)
                }
            }
            ).onSameThread().check()
    }

    private fun camera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    //--Results for camera or gallery--//
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    binding.cameraView.load(bitmap) {
                        crossfade(true)
                        crossfade(1000)

                        toBase64(bitmap)
                    }

                }

                GALLERY_REQUEST_CODE -> {

                    binding.cameraView.load(data?.data) {
                        crossfade(true)
                        crossfade(1000)

                        converter(data)


                    }

                }

            }

        }

    }

    //--Permissions Turn-off--//
    private fun showRotationalDialogForPermission(context: Context) {
        AlertDialog.Builder(requireActivity())
            .setMessage("It looks like you have turned off permissions" + "required for this feature. It can be enable under App settings!!!")
            .setPositiveButton("Go to settings") { _, _ ->

                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    startActivity(intent)


                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }.show()

    }

    //--Converter data Gallery to bitmap--//
    private fun converter(data: Intent?) {
        if (data != null && data.data != null) {
            val uri = data.data!!
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
            cursor?.use { c ->
                val nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (c.moveToFirst()) {
                    val name = c.getString(nameIndex)
                    inputStream?.let { inputStream ->
                        // create same file with same name
                        val file = File(requireContext().cacheDir, name)
                        val os = file.outputStream()
                        os.use {
                            inputStream.copyTo(it)
                        }
                        val bitmap2 = BitmapFactory.decodeFile(file.absolutePath)

                        toBase64(bitmap2)
                    }
                }
            }
        }
    }

    //--Process Base to 64--//
    private fun toBase64(bitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val image = stream.toByteArray()
        stringImage = Base64.encodeToString(image, Base64.DEFAULT)
    }

    //--EndPoint-Post for Document Delivery--//
    private fun send_documentation(body: Post_doc) {
        doc_service.service_doc.DocPost(body)
            .enqueue(object : Callback<Post_doc> {
                override fun onResponse(call: Call<Post_doc>, response: Response<Post_doc>) {
                    if (response.isSuccessful) {
                        Toast.makeText(activity, "Successful Send", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "Size of image is to big", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<Post_doc>, t: Throwable) {
                    call.cancel()
                }

            })
    }

    //--Body for strings--//
    private fun createbody(
        type_d: String,
        doc_num: String,
        names: String,
        lastnames: String,
        citiesdrop: String,
        email: String,
        type_attach: String,
        image: String
    ): Post_doc {
        return Post_doc(type_d, doc_num, names, lastnames, citiesdrop, email, type_attach, image)

    }

    //--Process pop-up Menu--//
    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(activity, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.Send_Documents_popup -> requireActivity().supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.container, Send())
                        commit()
                    }
                R.id.See_Documents_popup -> requireActivity().supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.container, See_Documents())
                        commit()
                    }
                R.id.Offices_popup -> requireActivity().supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.container, Google_Maps())
                        commit()
                    }
            }
            true
        })

        popup.show()
    }

}







