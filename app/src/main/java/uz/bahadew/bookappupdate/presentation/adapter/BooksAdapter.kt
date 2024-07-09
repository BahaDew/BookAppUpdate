package uz.bahadew.bookappupdate.presentation.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import uz.bahadew.bookappupdate.data.BookData
import uz.bahadew.bookappupdate.databinding.ItemBookBinding

class BooksAdapter : ListAdapter<BookData, BooksAdapter.BookHolder>(BookDiffUtil) {
    private var onClickBook: ((BookData) -> Unit)? = null


    inner class BookHolder(private val binding: ItemBookBinding) : ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClickBook?.invoke(getItem(adapterPosition))
            }
        }

        fun bind() {
            getItem(adapterPosition).let {
                binding.name.text = it.name
                binding.overview.text = it.overview
                Glide.with(binding.root.context)
                    .load(Uri.parse(it.imgPath))
                    .into(binding.img)
            }
        }
    }

    object BookDiffUtil : DiffUtil.ItemCallback<BookData>() {
        override fun areItemsTheSame(oldItem: BookData, newItem: BookData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: BookData, newItem: BookData): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        return BookHolder(
            ItemBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        holder.bind()
    }

    fun setOnClickBook(onClickBook: ((BookData) -> Unit)) {
        this.onClickBook = onClickBook
    }
}