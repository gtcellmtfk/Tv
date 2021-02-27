package com.bytebyte6.utils

import androidx.recyclerview.widget.RecyclerView

abstract class BaseDetailAdapter<T, V : DetailsViewHolder> : RecyclerView.Adapter<V>() {

    override fun getItemCount(): Int = adapterHelper().list.size

    override fun onBindViewHolder(holder: V, position: Int) {
        adapterHelper().onHelperBindViewHolder(holder, position)
    }

    abstract fun adapterHelper(): AdapterHelper<T, V>
}