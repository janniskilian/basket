package de.janniskilian.basket.feature.lists.list

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import de.janniskilian.basket.core.util.android.getThemeColor
import de.janniskilian.basket.feature.lists.R
import kotlin.math.min
import kotlin.math.roundToInt

class ListRecyclerViewItemTouchHelperCallback(
    private val context: Context,
    private val onItemSwiped: (itemAdapterPosition: Int) -> Unit
) : ItemTouchHelper.SimpleCallback(0, 0) {

    private val backgroundPaint = Paint().apply {
        color = context.getThemeColor(R.attr.colorError)
    }

    private val iconDrawable = ResourcesCompat
        .getDrawable(context.resources, R.drawable.ic_delete_24, context.theme)
        ?.apply {
            setTint(context.getThemeColor(R.attr.colorOnError))
        }

    private val iconHorizontalOffset = context.resources.getDimensionPixelSize(R.dimen.two)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeDirs = if (viewHolder.itemViewType == 0) {
            ItemTouchHelper.START or ItemTouchHelper.END
        } else {
            0
        }

        return makeMovementFlags(0, swipeDirs)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onItemSwiped(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )

        if (dX != 0f) {
            val itemView = viewHolder.itemView
            drawBackground(c, itemView, dX)
            iconDrawable?.let {
                drawIcon(c, itemView, dX, it)
            }
        }
    }

    private fun drawBackground(canvas: Canvas, itemView: View, dX: Float) {
        val backgroundLeft: Float
        val backgroundRight: Float

        if (dX < 0) {
            backgroundRight = itemView.right.toFloat()
            backgroundLeft = backgroundRight + dX
        } else {
            backgroundLeft = itemView.left.toFloat()
            backgroundRight = dX
        }

        canvas.drawRect(
            backgroundLeft,
            itemView.top.toFloat(),
            backgroundRight,
            itemView.bottom.toFloat(),
            backgroundPaint
        )
    }

    private fun drawIcon(canvas: Canvas, itemView: View, dX: Float, drawable: Drawable) {
        val iconVerticalOffset = (itemView.height - drawable.intrinsicHeight) / 2
        val iconLeft = if (dX < 0) {
            itemView.right - min(
                drawable.intrinsicWidth + iconHorizontalOffset,
                -(dX + iconHorizontalOffset).roundToInt()
            )
        } else {
            itemView.left + min(
                iconHorizontalOffset,
                (dX - drawable.intrinsicWidth - iconHorizontalOffset).roundToInt()
            )
        }

        drawable.setBounds(
            iconLeft,
            itemView.top + iconVerticalOffset,
            iconLeft + drawable.intrinsicWidth,
            itemView.bottom - iconVerticalOffset
        )
        drawable.draw(canvas)
    }
}
