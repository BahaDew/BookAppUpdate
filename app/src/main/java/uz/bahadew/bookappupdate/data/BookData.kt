package uz.bahadew.bookappupdate.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookData(
    val docId : String,
    val name : String,
    val author : String,
    val bookPath : String,
    val imgPath : String,
    val categoryId : String,
    val overview : String,
) : Parcelable