package com.infostrategic.edoctor.utils

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

val View.isVisible get() = visibility == View.VISIBLE

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.show(show: Boolean, animationDuration: Long? = null, animationStartDelay: Long? = null) {
    if (show && visibility == View.VISIBLE || !show && visibility == View.GONE)
        return

    if (animationDuration != null) {
        alpha = if (show) 0f else 1f
    }

    if (show) show() else hide()

    if (animationDuration != null) {
        animate()
            .alpha(if (show) 1f else 0f)
            .setDuration(animationDuration)
            .withStartAction { if (show) visibility = View.VISIBLE }
            .withEndAction { if (!show) visibility = View.GONE }
            .setStartDelay(animationStartDelay ?: 0)
            .start()
    }
}

var TextView.textColorRes: Int
    get() = throw IllegalStateException("Property does not have a getter")
    set(value) = setTextColor(ContextCompat.getColor(context, value))

operator fun ViewGroup.plusAssign(view: View) {
    addView(view)
}

inline fun <reified T : View> View.lazyFind(@IdRes id: Int) = lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(id) }
inline fun <reified T : View> Activity.lazyFind(@IdRes id: Int) = lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(id) }
inline fun <reified T : View> Fragment.lazyFind(@IdRes id: Int) = lazy(LazyThreadSafetyMode.NONE) { view?.findViewById<T>(id) }
inline fun <reified T : View> Dialog.lazyFind(@IdRes id: Int) = lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(id) }
