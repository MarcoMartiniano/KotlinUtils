package com.example.kotlinutils.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.isVisible(visible: Boolean) {
    visibility = if (visible)
        View.VISIBLE
    else
        View.GONE
}

inline val Int.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()
inline val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Context.getColorById(colorId: Int): ColorStateList {
    return ColorStateList.valueOf(
        ContextCompat.getColor(
            this,
            colorId
        )
    )
}

internal fun View?.findSuitableParent(): ViewGroup? {
    var view = this
    var fallback: ViewGroup? = null
    do {
        if (view is CoordinatorLayout) {
            return view
        } else if (view is FrameLayout) {
            if (view.id == android.R.id.content) {
                return view
            } else {
                fallback = view
            }
        }

        if (view != null) {
            val parent = view.parent
            view = if (parent is View) parent else null
        }
    } while (view != null)

    return fallback
}