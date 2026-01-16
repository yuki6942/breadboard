package moe.apex.rule34.image

import android.content.Context
import androidx.annotation.StringRes
import moe.apex.rule34.R
import moe.apex.rule34.preferences.PrefEnum


private val FILTER_SAFE         = listOf("-rating:safe", "-rating:general", "-rating:g")
private val FILTER_SENSITIVE    = listOf("-rating:sensitive", "-rating:s")
private val FILTER_QUESTIONABLE = listOf("-rating:questionable", "-rating:q")
private val FILTER_EXPLICIT     = listOf("-rating:explicit", "-rating:e")


enum class ImageRating(@StringRes val labelRes: Int) : PrefEnum<ImageRating> {
    SAFE(R.string.rating_safe),
    SENSITIVE(R.string.rating_sensitive),
    QUESTIONABLE(R.string.rating_questionable),
    EXPLICIT(R.string.rating_explicit),
    UNKNOWN(R.string.rating_unknown);

    override val label: String
        get() = name

    fun label(context: Context): String = context.getString(labelRes)

    companion object {
        private val mapping = mapOf(
            SAFE to FILTER_SAFE,
            SENSITIVE to FILTER_SENSITIVE,
            QUESTIONABLE to FILTER_QUESTIONABLE,
            EXPLICIT to FILTER_EXPLICIT
        )

        fun buildQueryListFor(vararg ratings: ImageRating): List<List<String>> {
            val currentFilter = mutableListOf(FILTER_SAFE, FILTER_SENSITIVE, FILTER_QUESTIONABLE, FILTER_EXPLICIT)
            for (rating in ratings) {
                mapping[rating]?.let { currentFilter.remove(it) }
            }
            return currentFilter
        }

        fun buildSearchStringFor(vararg ratings: ImageRating): String {
            val currentFilter = buildQueryListFor(*ratings)
            return currentFilter.joinToString("+") { it.joinToString("+") }
        }

        fun buildSearchStringFor(ratings: Collection<ImageRating>): String {
            return buildSearchStringFor(*ratings.toTypedArray())
        }
    }
}