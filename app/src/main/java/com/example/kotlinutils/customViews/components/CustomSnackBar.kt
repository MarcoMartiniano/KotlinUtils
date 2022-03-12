package com.example.kotlinutils.customViews.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.kotlinutils.R
import com.example.kotlinutils.extensions.findSuitableParent
import com.google.android.material.snackbar.BaseTransientBottomBar

class CustomSnackBar(
    parent: ViewGroup,
    content: CustomSnackbarView,
    customSnackbarStyle: Style
) : BaseTransientBottomBar<CustomSnackBar>(parent, content, content) {


    init {
        val title = getView().findViewById(R.id.title_snackbar) as AppCompatTextView
        val description = getView().findViewById(R.id.description_snackbar) as AppCompatTextView
        val view = getView().findViewById(R.id.cl_darkview) as ConstraintLayout
        val snackbarIcon = getView().findViewById(R.id.icon_snackbar) as AppCompatImageView
        val root = getView().findViewById(R.id.cl_customsnackbar) as ConstraintLayout

        getView().setBackgroundResource(android.R.color.transparent)
        when (customSnackbarStyle) {
            Style.CUSTOM -> {
                root.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.background_light_level_1)
                title.text = "Custom SnackBar title"
                title.setTextColor(
                    ContextCompat.getColorStateList(
                        context,
                        R.color.neutral_lightest
                    )
                )
                description.text = "Custom SnackBar description"
                description.setTextColor(
                    ContextCompat.getColorStateList(
                        context,
                        R.color.neutral_light
                    )
                )
                view.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.primary_dark)
                snackbarIcon.imageTintList =
                    ContextCompat.getColorStateList(context, R.color.primary_lightest)
            }
            Style.SUCCESS -> {
                root.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.success_default)
                title.text = "Custom SnackBar title"
                title.setTextColor(
                    ContextCompat.getColorStateList(
                        context,
                        R.color.neutral_darkest
                    )
                )
                description.text = "Custom SnackBar description"
                description.setTextColor(
                    ContextCompat.getColorStateList(
                        context,
                        R.color.neutral_darkest
                    )
                )
                view.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.success_dark)
                snackbarIcon.imageTintList =
                    ContextCompat.getColorStateList(context, R.color.success_light)
            }
            Style.ERROR -> {
                root.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.error_default)
                title.text = "Custom SnackBar title"
                title.setTextColor(
                    ContextCompat.getColorStateList(
                        context,
                        R.color.neutral_lightest
                    )
                )
                description.text = "Custom SnackBar description"
                description.setTextColor(
                    ContextCompat.getColorStateList(
                        context,
                        R.color.neutral_light
                    )
                )
                view.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.error_dark)
                snackbarIcon.imageTintList =
                    ContextCompat.getColorStateList(context, R.color.error_light)
            }
            Style.WARNING -> {
                root.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.warning_default)
                title.text = "Custom SnackBar title"
                title.setTextColor(
                    ContextCompat.getColorStateList(
                        context,
                        R.color.neutral_darkest
                    )
                )
                description.text = "Custom SnackBar description"
                description.setTextColor(
                    ContextCompat.getColorStateList(
                        context,
                        R.color.neutral_darkest
                    )
                )
                view.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.secondary_darkest)
                snackbarIcon.imageTintList =
                    ContextCompat.getColorStateList(context, R.color.warning_light)
            }
        }

        getView().setPadding(0, 0, 0, 0)
    }

    companion object {

        fun make(view: View, customSnackbarStyle: Style): CustomSnackBar {
            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(

            )
            val customView = LayoutInflater.from(view.context).inflate(
                R.layout.layout_custom_snackbar,
                parent,
                false
            ) as CustomSnackbarView
            return CustomSnackBar(
                parent,
                customView,
                customSnackbarStyle
            )
        }
    }

    enum class Style {
        CUSTOM, SUCCESS, ERROR, WARNING
    }

}