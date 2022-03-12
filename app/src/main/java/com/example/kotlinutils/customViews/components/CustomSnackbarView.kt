package com.example.kotlinutils.customViews.components

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.kotlinutils.R
import com.google.android.material.snackbar.ContentViewCallback

class CustomSnackbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ContentViewCallback {

    private val iconImage: ImageView

    init {
        View.inflate(context, R.layout.custom_view_snackbar, this)
        this.iconImage = findViewById(R.id.icon_snackbar)
    }

    override fun animateContentIn(delay: Int, duration: Int) {

        val scaleX = ObjectAnimator.ofFloat(iconImage, View.SCALE_X, 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(iconImage, View.SCALE_Y, 0f, 1f)
        val animatorSet = AnimatorSet().apply {
            interpolator = OvershootInterpolator()
            setDuration(500)
            playTogether(scaleX, scaleY)
        }
        animatorSet.start()
    }

    override fun animateContentOut(delay: Int, duration: Int) {

    }

}