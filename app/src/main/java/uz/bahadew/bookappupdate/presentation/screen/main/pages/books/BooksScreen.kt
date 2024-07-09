package uz.bahadew.bookappupdate.presentation.screen.main.pages.books

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.bahadew.bookappupdate.R
import uz.bahadew.bookappupdate.databinding.ScreenBooksBinding
import uz.bahadew.bookappupdate.presentation.adapter.CategoriesAdapter

@AndroidEntryPoint
class BooksScreen : Fragment(R.layout.screen_books) {
    private val binding by viewBinding(ScreenBooksBinding::bind)
    private val viewModel by viewModels<BooksViewModel>()
    private val adapter = CategoriesAdapter()
    private var time = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.requestAllBooksByCategory()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initFlow()
    }

    private fun initView() = binding.apply {
        rvList.adapter = adapter
        rvList.layoutManager = LinearLayoutManager(requireContext())
        adapter.setOnClickBook {
            if (System.currentTimeMillis() - time >= 500) {
                time = System.currentTimeMillis()
                viewModel.onClickBook(it)
            }
        }
        srl.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.requestAllBooksByCategory()
            }
        }
    }

    private fun initFlow() = binding.apply {
        viewModel.allBooksByCategory
            .onEach {
                adapter.submitList(it.sortedBy { item ->  item.name })
            }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
        viewModel.isRefreshing
            .onEach {
                srl.isRefreshing = it
            }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }
}