package com.bytebyte6.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Ignore
import com.bytebyte6.common.randomColorByNightMode
import kotlinx.android.parcel.Parcelize
import kotlin.random.Random

@Parcelize
@Keep
data class Languages(
    var language: List<Language> = emptyList(),
    @Ignore var content: String = "",
    @Ignore override val color: Int = randomColorByNightMode(),
    @Ignore override val radius: Int = 0,
    @Ignore override val outline: Boolean = Random.Default.nextBoolean()
) : Parcelable, Card {

    private fun getLanguageName(): String {
        val stringBuilder = StringBuilder()
        language.forEachIndexed { index, language ->
            stringBuilder.append(language.languageName)
            if (index != this.language.size - 1) {
                stringBuilder.append(" , ")
            }
        }
        return stringBuilder.toString()
    }

    private fun getLanguageCode(): String {
        val stringBuilder = StringBuilder()
        language.forEachIndexed { index, language ->
            stringBuilder.append(language.languageCode)
            if (index != this.language.size - 1) {
                stringBuilder.append(" , ")
            }
        }
        return stringBuilder.toString()
    }

    override val title: String
        get() = getLanguageName()

    override val body: String
        get() = "Code: " + getLanguageCode()

    override val transitionName: String
        get() = title
}