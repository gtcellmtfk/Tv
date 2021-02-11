package com.bytebyte6.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Ignore
import com.bytebyte6.common.randomColorByNightMode
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

object LangDiff : DiffUtil.ItemCallback<Languages>() {
    override fun areItemsTheSame(oldItem: Languages, newItem: Languages): Boolean {
        return oldItem.language == newItem.language
    }

    override fun areContentsTheSame(oldItem: Languages, newItem: Languages): Boolean {
        return oldItem == newItem
    }
}

@Parcelize
@Keep
data class Languages(
    var language: List<Language> = emptyList()
) : Parcelable {

    @IgnoredOnParcel
    @Ignore
    val color: Int = randomColorByNightMode()

    @IgnoredOnParcel
    @Ignore
    val langName: String = getLanguageName()

    @IgnoredOnParcel
    @Ignore
    val langCode: String = getLanguageCode()

    private fun getLanguageName(): String {
        val stringBuilder = StringBuilder()
        language.forEachIndexed { index, lang ->
            stringBuilder.append(lang.languageName)
            if (index != language.size - 1) {
                stringBuilder.append(" , ")
            }
        }
        return stringBuilder.toString()
    }

    private fun getLanguageCode(): String {
        val stringBuilder = StringBuilder()
        language.forEachIndexed { index, lang ->
            stringBuilder.append(lang.languageCode)
            if (index != language.size - 1) {
                stringBuilder.append(" , ")
            }
        }
        return stringBuilder.toString()
    }


}