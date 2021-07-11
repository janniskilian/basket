package de.janniskilian.basket.core.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import de.janniskilian.basket.core.ui.compose.BasketTheme
import de.janniskilian.basket.core.ui.compose.component.DefaultTopAppBar

abstract class ComposeBaseFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                BasketTheme {
                    Scaffold(
                        topBar = { DefaultTopAppBar(titleTextRes) },
                        content = { CreateContent() }
                    )
                }
            }
        }

    @Composable
    abstract fun CreateContent()
}
