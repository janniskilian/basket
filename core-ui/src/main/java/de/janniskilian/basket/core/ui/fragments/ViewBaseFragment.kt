package de.janniskilian.basket.core.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import de.janniskilian.basket.core.ui.R
import de.janniskilian.basket.core.util.android.addListener
import de.janniskilian.basket.core.util.android.getLong

@Suppress("TooManyFunctions")
abstract class ViewBaseFragment<VB : ViewBinding> : BaseFragment() {

    private val transitionDuration by lazy {
        getLong(requireContext(), R.integer.transition_duration)
    }

    lateinit var binding: VB
        private set

    val toolbar: MaterialToolbar?
        get() = requireView().findViewById(R.id.toolbar)

    val titleTextView: TextView?
        get() = toolbar?.findViewById(R.id.title)

    abstract fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (useDefaultTransitions) {
            setupTransitions()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        createViewBinding(inflater, container)
            .also {
                binding = it
            }
            .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (view as? ViewGroup)?.isTransitionGroup = true

        titleTextRes?.let {
            titleTextView?.text = getString(it)
        }
    }

    fun enableFadeThroughExitTransition() {
        exitTransition = MaterialFadeThrough().apply {
            duration = transitionDuration
            setListener()
        }
    }

    private fun setupTransitions() {
        val isRootDestinationStartedFromRootDestination =
            (arguments?.getBoolean(KEY_STARTED_FROM_ROOT_DESTINATION) ?: false
                    && findNavController().currentDestination?.parent?.id == R.id.nav_graph)
        arguments?.remove(KEY_STARTED_FROM_ROOT_DESTINATION)
        updateTransitions(isRootDestinationStartedFromRootDestination)
    }

    private fun updateTransitions(enableRootNavigationTransition: Boolean) {
        if (enableRootNavigationTransition) {
            enterTransition = MaterialFadeThrough().apply {
                duration = transitionDuration
                setListener()
            }
        } else {
            enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
                duration = transitionDuration
            }
            exitTransition = enterTransition

            reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
                duration = transitionDuration
            }
            returnTransition = reenterTransition
        }
    }

    private fun MaterialFadeThrough.setListener() {
        addListener(
            onEnd = { updateTransitions(false) },
            onCancel = { updateTransitions(false) }
        )
    }

    companion object {

        const val KEY_STARTED_FROM_ROOT_DESTINATION = "KEY_STARTED_FROM_ROOT_DESTINATION"
    }
}
