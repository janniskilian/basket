package de.janniskilian.basket.feature.lists

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

const val KEY_RESULT = "KEY_RESULT"
const val KEY_REQUEST_ROUTE = "KEY_REQUEST_ROUTE"
const val KEY_RESULT_CODE = "KEY_RESULT_CODE"
const val KEY_RESULT_DATA = "KEY_RESULT_DATA"

enum class ResultCode {

    SUCCESS,
    CANCEL
}

val NavBackStackEntry.requestCode: Int?
    get() = savedStateHandle.get(KEY_REQUEST_ROUTE)

fun NavHostController.navigateWithResult(route: String, requestCode: Int) {
    navigate(route)
}

fun NavHostController.setResult(resultCode: ResultCode, data: Parcelable? = null) {
    previousBackStackEntry
        ?.savedStateHandle
        ?.set(
            KEY_RESULT,
            bundleOf(
                KEY_REQUEST_ROUTE to currentDestination?.route,
                KEY_RESULT_CODE to resultCode,
                KEY_RESULT_DATA to data
            )
        )
}

fun NavHostController.clearResult() {
    currentBackStackEntry
        ?.savedStateHandle
        ?.remove<Bundle>(KEY_RESULT)
}


fun NavHostController.getNavigationResult(): LiveData<Bundle>? =
    currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData(KEY_RESULT)

fun NavHostController.getNavigationResultBundle(): Bundle? =
    currentBackStackEntry
        ?.savedStateHandle
        ?.get(KEY_RESULT)

fun Bundle.getNavigationResult(
    block: (requestRoute: String, resultCode: ResultCode, data: Parcelable?) -> Unit
) {
    val requestRoute = getString(KEY_REQUEST_ROUTE)
    val resultCode = getSerializable(KEY_RESULT_CODE) as? ResultCode
    val data = getParcelable<Parcelable>(KEY_RESULT_DATA)

    if (requestRoute != null && resultCode != null) {
        block(requestRoute, resultCode, data)
    }
}
