package de.janniskilian.basket.core.ui.compose

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


val Int.du: Dp
    get() = (this * UNIT).dp

val Double.du: Dp
    get() = (this * UNIT).dp

val Int.sdu: TextUnit
    get() = (this * UNIT).sp

val Double.sdu: TextUnit
    get() = (this * UNIT).sp

private const val UNIT = 8
