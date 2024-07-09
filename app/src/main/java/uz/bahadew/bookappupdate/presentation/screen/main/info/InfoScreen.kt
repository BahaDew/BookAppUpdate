package uz.bahadew.bookappupdate.presentation.screen.main.info

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.bahadew.bookappupdate.R
import uz.bahadew.bookappupdate.databinding.ScreenInfoBinding

@AndroidEntryPoint
class InfoScreen : Fragment(R.layout.screen_info) {
    private val binding by viewBinding(ScreenInfoBinding::bind)
    private val viewModel by viewModels<InfoViewModel>()
    private val navArgs by navArgs<InfoScreenArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFlow()
        initView()
    }

    private fun initFlow() = binding.apply {
        viewModel.isFavoriteBook
            .onEach {
                checkbox.isChecked = it
                checkbox.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.favoriteChange(navArgs.bookData, isChecked)
                }
            }.flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }

    private fun initView() = binding.apply {
        name.text = navArgs.bookData.name
        overview.text = navArgs.bookData.overview
        author.text = navArgs.bookData.author
        Glide
            .with(this@InfoScreen)
            .load(Uri.parse(navArgs.bookData.imgPath))
            .into(img)
        back.setOnClickListener {
            viewModel.onClickBack()
        }
        btnRead.setOnClickListener {
            viewModel.onClickRead(navArgs.bookData)
        }
        viewModel.isFavoriteBook(navArgs.bookData)
    }
}