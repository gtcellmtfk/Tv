package com.bytebyte6.library

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, V : DetailsViewHolder> : RecyclerView.Adapter<V>() {

    override fun getItemCount(): Int = adapterHelper().list.size

    override fun onBindViewHolder(holder: V, position: Int) {
        adapterHelper().onHelperBindViewHolder(holder, position)
    }

    abstract fun adapterHelper():AdapterHelper<T,V>
}