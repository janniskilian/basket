package de.janniskilian.basket.feature.lists.list

import android.os.Parcelable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import de.janniskilian.basket.core.ui.compose.component.BottomDrawerMenu
import de.janniskilian.basket.core.ui.compose.component.BottomDrawerMenuItem
import de.janniskilian.basket.core.ui.compose.component.BottomSheetDialog
import de.janniskilian.basket.core.util.compose.LocalNavController
import de.janniskilian.basket.feature.lists.R
import de.janniskilian.basket.feature.lists.ResultCode
import de.janniskilian.basket.feature.lists.setResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.parcelize.Parcelize

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun ListMoreMenuContent() {
    val navController = LocalNavController.current

    BottomSheetDialog {
        BottomDrawerMenu(
            items = ListMoreMenuItem.values().toList(),
            onItemClick = {
                navController.setResult(
                    ResultCode.SUCCESS,
                    ListMoreMenuResult(it)
                )
                navController.popBackStack()
            }
        )
    }
}

@Parcelize
class ListMoreMenuResult(
    val menuItem: ListMoreMenuItem
) : Parcelable

enum class ListMoreMenuItem : BottomDrawerMenuItem {

    CHECK_ALL {
        @Composable
        override fun icon(): ImageVector = Icons.Outlined.CheckBox

        @Composable
        override fun text(): String = stringResource(R.string.action_menu_check_all_items)
    },

    UNCHECK_ALL {
        @Composable
        override fun icon(): ImageVector = Icons.Outlined.CheckBoxOutlineBlank

        @Composable
        override fun text(): String = stringResource(R.string.action_menu_uncheck_all_items)
    },

    REMOVE_CHECKED {
        @Composable
        override fun icon(): ImageVector =
            ImageVector.vectorResource(R.drawable.ic_remove_checked_24)

        @Composable
        override fun text(): String = stringResource(R.string.action_menu_remove_checked_items)
    },

    REMOVE_ALL {
        @Composable
        override fun icon(): ImageVector = ImageVector.vectorResource(R.drawable.ic_remove_all_24)

        @Composable
        override fun text(): String = stringResource(R.string.action_menu_remove_all_items)
    },

    SEND {
        @Composable
        override fun icon(): ImageVector = Icons.Outlined.Send

        @Composable
        override fun text(): String = stringResource(R.string.action_menu_send_list)
    }
}
