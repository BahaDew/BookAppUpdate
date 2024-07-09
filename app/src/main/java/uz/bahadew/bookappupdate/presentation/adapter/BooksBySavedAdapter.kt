package uz.bahadew.bookappupdate.presentation.adapter

import android.net.Uri
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import uz.bahadew.bookappupdate.R
import uz.bahadew.bookappupdate.data.BookData
import uz.bahadew.bookappupdate.databinding.ItemBookForSavedBinding
import uz.bahadew.bookappupdate.utils.myLogger

class BooksBySavedAdapter : ListAdapter<BookData, BooksBySavedAdapter.MyHolder>(MyDiffUtil) {
    private var onClickBook: ((BookData) -> Unit)? = null
    private var onClickCheckBox: ((BookData, Boolean) -> Unit)? = null
    private var query = ""

    inner class MyHolder(private val binding: ItemBookForSavedBinding) : ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onClickBook?.invoke(getItem(adapterPosition))
            }
            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                onClickCheckBox?.invoke(getItem(adapterPosition), isChecked)
            }
        }

        fun bind() {
            getItem(adapterPosition).let {
                binding.author.text = it.author
                binding.overview.text = it.overview
                Glide
                    .with(binding.root.context)
                    .load(Uri.parse(it.imgPath))
                    .into(binding.img)
                binding.checkbox.isGone = onClickCheckBox == null
                if (query.trim().isNotEmpty()) {
                    val spannable = SpannableString(it.name)
                    val index = it.name.indexOf(query, ignoreCase = true)
                    myLogger("query -> $query name -> ${it.name} index -> $index", "SPAN")
                    spannable
                        .setSpan(
                            ForegroundColorSpan(binding.root.context.getColor(R.color.app_active_color)),
                            index,
                            index + query.length,
                            SpannableString.SPAN_EXCLUSIVE_INCLUSIVE
                        )
                    binding.name.text = spannable
                } else {
                    binding.name.text = it.name
                }
            }
        }
    }

    object MyDiffUtil : DiffUtil.ItemCallback<BookData>() {
        override fun areItemsTheSame(oldItem: BookData, newItem: BookData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: BookData, newItem: BookData): Boolean {
            return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            ItemBookForSavedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind()
    }

    fun setOnClickBook(onClickBook: ((BookData) -> Unit)) {
        this.onClickBook = onClickBook
    }

    fun setOnClickCheckBox(onClickCheckBox: ((BookData, Boolean) -> Unit)) {
        this.onClickCheckBox = onClickCheckBox
    }

    fun setQuery(query: String, list: List<BookData>) {
        this.query = query
        submitList(list)
    }
}