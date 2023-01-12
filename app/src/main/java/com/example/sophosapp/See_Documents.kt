package com.example.sophosapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sophos.userslogin.Image_Service
import com.example.sophos.userslogin.See_service
import com.example.sophosapp.See_Documents_Api.Item
import com.example.sophosapp.See_Documents_Api.Values
import com.example.sophosapp.See_Documents_Api.Values_Image
import com.example.sophosapp.SophosApplication.Companion.prefs
import com.example.sophosapp.Tools_RecyclerView.Adapter
import com.example.sophosapp.databinding.FragmentSeeDocumentsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class See_Documents : Fragment() {

    lateinit var binding: FragmentSeeDocumentsBinding
    lateinit var Img_String: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSeeDocumentsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerView
        binding.progressBar.visibility = View.VISIBLE

        //--EndPoint-Get for See shipments--//
        See_service.service_see.DocumentGet(prefs.getEmail())
            .enqueue(object : Callback<Values> {
                override fun onResponse(call: Call<Values>, response: Response<Values>) {
                    if (response.isSuccessful) {
                        recyclerView.apply {
                            layoutManager = LinearLayoutManager(activity)
                            adapter = Adapter(response.body()!!, { onItemSelected(it) })
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerView.addItemDecoration(
                                DividerItemDecoration(
                                    activity,
                                    LinearLayoutManager(activity).orientation
                                )
                            )


                        }
                    }
                }

                override fun onFailure(call: Call<Values>, t: Throwable) {
                    call.cancel()
                }

            })

        //--Listener Pop-up Menu--//
        binding.popup.setOnClickListener { v: View ->
            showMenu(v, R.menu.menu_item)
        }

        //--Fragment Transition to Menu--//
        binding.backToMenu.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, menu_screen())
                commit()
            }
        }

    }

    //--Function to see image from document--//
    fun onItemSelected(Values: Item) {
        Image_Service.service_Image.DocumentGet(Values.IdRegistro)
            .enqueue(object : Callback<Values_Image> {
                override fun onResponse(call: Call<Values_Image>, response: Response<Values_Image>) {
                    if (response.isSuccessful) {
                        var img_Item = response.body()!!.Items
                        for (i in img_Item) {
                            Img_String = i.Adjunto
                        }
                        val imageBytes = Base64.decode(Img_String, Base64.DEFAULT)
                        val decodedImage =
                            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                        binding.ImageGet.setImageBitmap(decodedImage)
                        binding.ImageGet.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<Values_Image>, t: Throwable) {
                    call.cancel()
                }

            })
    }

    //--Pop-Up Menu process--//
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


