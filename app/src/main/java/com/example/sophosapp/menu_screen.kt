package com.example.sophosapp

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sophosapp.SophosApplication.Companion.prefs
import com.example.sophosapp.databinding.FragmentMenuScreenBinding
import com.google.android.material.appbar.MaterialToolbar


class menu_screen : Fragment() {

    private lateinit var binding: FragmentMenuScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //--Fragment Transition to Send-Documents--//
        binding.buttonDoc.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, Send())
                commit()
            }
        }

        //--Fragment Transition to See-Documents--//
        binding.buttonSee.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, See_Documents())
                commit()
            }
        }

        //--Fragment Transition to Google-Maps--//
        binding.buttonOffice.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, Google_Maps())
                commit()
            }
        }

        //--Pop-up Menu Listener--//
        binding.popup.setOnClickListener { v: View ->
            showMenu(v, R.menu.menu_item)
        }

        //--Get-Name to top-Bar--//
        val names = prefs.getName()
        val setname: MaterialToolbar = binding.topAppBar
        setname.title = names

    }

    //--Pop-up Menu Process--//
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