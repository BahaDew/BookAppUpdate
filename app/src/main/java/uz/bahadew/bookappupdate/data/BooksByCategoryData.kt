package uz.bahadew.bookappupdate.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BooksByCategoryData(
    val docId : String,
    val name : String,
    val books : List<BookData>
) : Parcelable