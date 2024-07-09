package uz.bahadew.bookappupdate.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.bahadew.bookappupdate.data.BookData
import uz.bahadew.bookappupdate.data.BooksByCategoryData
import uz.bahadew.bookappupdate.databinding.ItemCategoryBinding

class CategoriesAdapter :
    ListAdapter<BooksByCategoryData, CategoriesAdapter.CategoryHolder>(CategoryDiffUtil) {
    private var onClickBook: ((BookData) -> Unit)? = null

    inner class CategoryHolder(private val binding: ItemCategoryBinding) :
        ViewHolder(binding.root) {
        private val adapter = BooksAdapter()

        init {
            adapter.setOnClickBook {
                onClickBook?.invoke(it)
            }
            binding.rvList.adapter = adapter
            binding.rvList.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        }

        fun bind() {
            getItem(adapterPosition).let {
                adapter.submitList(it.books)
                binding.name.text = it.name
            }
        }
    }

    object CategoryDiffUtil : DiffUtil.ItemCallback<BooksByCategoryData>() {
        override fun areItemsTheSame(
            oldItem: BooksByCategoryData,
            newItem: BooksByCategoryData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BooksByCategoryData,
            newItem: BooksByCategoryData
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        return CategoryHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind()
    }

    fun setOnClickBook(onClickBook: ((BookData) -> Unit)) {
        this.onClickBook = onClickBook
    }
}