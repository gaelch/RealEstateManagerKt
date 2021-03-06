package com.cheyrouse.gael.realestatemanagerkt.view


import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cheyrouse.gael.realestatemanagerkt.R

class ListPaddingDecoration(
    context: Context,
    private val paddingLeft: Float,
    private val paddingRight: Int
) : RecyclerView.ItemDecoration() {

    private var mDivider: Drawable? = null

    // init divider
    init {
        mDivider = ContextCompat.getDrawable(context, R.drawable.divider_medium)
    }

    // Draw function
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        val left = parent.paddingLeft + paddingLeft
        val right = parent.width - parent.paddingRight - paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + (mDivider?.intrinsicHeight ?: 0)

            mDivider?.let {
                it.setBounds(left.toInt(), top, right, bottom)
                it.draw(c)
            }

        }
    }

}