package com.example.sophosapp.Tools_RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sophosapp.R
import com.example.sophosapp.See_Documents_Api.Item
import com.example.sophosapp.See_Documents_Api.Values


class Adapter(val Values: Values, private val onClickListener: (Item) -> Unit) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_for_get, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bindView(Values.Items[position], onClickListener)
    }

    override fun getItemCount(): Int {
        return Values.Items.size
    }


}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvDate: TextView = itemView.findViewById(R.id.date_view)
    private val tvTypeAtc: TextView = itemView.findViewById(R.id.type_atc_view)
    private val tvNamevw: TextView = itemView.findViewById(R.id.Name_view)

    fun bindView(Values: Item, onClickListener: (Item) -> Unit) {
        tvDate.text = Values.Fecha
        tvTypeAtc.text = Values.TipoAdjunto
        tvNamevw.text = Values.Nombre

        itemView.setOnClickListener {
            onClickListener(Values)
        }

    }


}