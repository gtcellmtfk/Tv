package com.bytebyte6.view.adapter

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bytebyte6.common.GlideClearHelper
import com.bytebyte6.common.ImageClearHelper
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.entity.CountryDiff
import com.bytebyte6.utils.BaseListAdapter
import com.bytebyte6.view.load
import java.util.*


class CountryAdapter(
    // 清理图片资源
    private val clearHelper: ImageClearHelper = GlideClearHelper(),
    // 在没有动画的正常情况下应该使用Glide.with(fragment)加载图片
    // 有动画的情况下使用Glide.with(fragment)加载图片会出现动画还没结束，图片资源就被清理了，
    // 图片被清理后显示的是占位图，体验极差
    private val fragment: Fragment
) : BaseListAdapter<Country, ImageViewHolder>(CountryDiff), ImageClearHelper by clearHelper {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder.create(parent)

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.transitionName = currentList[position].name
        val item = getItem(position)
        val tvName = holder.binding.tvName
        val ivPreview = holder.binding.ivPreview
        holder.binding.flButton.isVisible = false

        // 需要清理的ImageView
        images.add(ivPreview)

        if (Locale.getDefault().language.contains("zh")) {
            tvName.text = item.nameChinese
        } else {
            tvName.text = item.name
        }
        // 使用上下文加载
        ivPreview.load(item.image)
        // 使用fragment加载
        // ivPreview.load(fragment,item.image)
    }
}