package io.github.mellamopablo.simondice.extensions

import android.support.design.widget.Snackbar
import android.view.View

fun View.snackbar(text: String, cb: (() -> Unit)? = null) {
	val snack = Snackbar.make(this, text, Snackbar.LENGTH_LONG)
	cb?.let {
		snack.addCallback(object : Snackbar.Callback() {
			override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
				super.onDismissed(transientBottomBar, event)
				cb()
			}
		})
	}
	snack.show()
}
