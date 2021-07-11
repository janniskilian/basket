package de.janniskilian.basket.core.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import de.janniskilian.basket.core.ui.R
import de.janniskilian.basket.core.ui.navigation.KEY_REQUEST_CODE
import de.janniskilian.basket.core.ui.navigation.NavigationContainerProvider
import de.janniskilian.basket.core.ui.navigation.ResultCode
import de.janniskilian.basket.core.ui.navigation.clearResult
import de.janniskilian.basket.core.ui.navigation.getNavigationResult
import de.janniskilian.basket.core.util.android.doOnEvent
import de.janniskilian.basket.core.util.android.getThemeColor
import de.janniskilian.basket.core.util.android.hideKeyboard
import de.janniskilian.basket.core.util.android.viewmodel.DefaultMutableLiveData
import timber.log.Timber

@Suppress("TooManyFunctions")
abstract class BaseFragment : Fragment() {

    @IdRes
    private var navDestinationId = 0

    val navigationContainer
        get() = (requireActivity() as NavigationContainerProvider).navigationContainer

    @get:MenuRes
    open val menuRes: Int?
        get() = null

    @get:StringRes
    open val titleTextRes: Int?
        get() = null

    @get:StringRes
    open val fabTextRes: Int?
        get() = null

    open val isShowAppBar: Boolean get() = true

    open val appBarColor: LiveData<Int> by lazy {
        DefaultMutableLiveData(requireContext().getThemeColor(R.attr.colorPrimarySurface))
    }

    open val animateAppBarColor get() = true

    open val useDefaultTransitions get() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveNavDestinationId(savedInstanceState)
        setupNavigationResultHandling()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideKeyboard()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(KEY_NAV_DESTINATION_ID, navDestinationId)
    }

    open fun onNavigationResult(requestCode: Int, resultCode: ResultCode, data: Any?) {
        // Override this method to receive navigation results.
    }

    open fun onNavigateUpAction(): Boolean = false

    open fun onHomePressed(): Boolean = false

    open fun onBackPressed(): Boolean = false

    open fun onFabClicked() {
        // This is a click listener for the FAB
        // that should be overridden by fragments where the FAB is displayed.
    }

    fun navigate(
        @IdRes resId: Int,
        args: Bundle? = null,
        navOptions: NavOptions? = null,
        navigatorExtras: Navigator.Extras? = null
    ) {
        findNavController().navigate(
            resId,
            args,
            navOptions,
            navigatorExtras
        )
    }

    fun navigate(directions: NavDirections, navigatorExtras: Navigator.Extras? = null) {
        navigate(
            directions.actionId,
            directions.arguments,
            null,
            navigatorExtras
        )
    }

    fun navigateWithResult(
        directions: NavDirections,
        requestCode: Int,
        navOptions: NavOptions? = null
    ) {
        val args = directions.arguments
        args.addRequestCode(requestCode)
        navigate(directions.actionId, args, navOptions)
    }

    private fun saveNavDestinationId(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            findNavController().currentDestination?.let {
                navDestinationId = it.id
            }
        } else {
            navDestinationId = savedInstanceState.getInt(KEY_NAV_DESTINATION_ID)
        }
    }

    private fun setupNavigationResultHandling() {
        if (navDestinationId != 0) {
            val backStackEntry = try {
                findNavController().getBackStackEntry(navDestinationId)
            } catch (e: IllegalArgumentException) {
                Timber.e(e)
                return
            }

            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    backStackEntry.getNavigationResult { requestCode, resultCode, data ->
                        clearResult()
                        onNavigationResult(requestCode, resultCode, data)
                    }
                }
            }

            backStackEntry.lifecycle.addObserver(observer)

            viewLifecycleOwner.lifecycle.doOnEvent(Lifecycle.Event.ON_DESTROY) {
                backStackEntry.lifecycle.removeObserver(observer)
            }
        }
    }

    companion object {

        private const val KEY_NAV_DESTINATION_ID = "KEY_NAV_DESTINATION_ID"
    }
}

private fun Bundle.addRequestCode(requestCode: Int) {
    putInt(KEY_REQUEST_CODE, requestCode)
}
