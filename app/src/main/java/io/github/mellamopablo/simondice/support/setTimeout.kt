package io.github.mellamopablo.simondice.support

fun setTimeout(timeout: Long, op: () -> Unit) {
	android.os.Handler().postDelayed({ op() }, timeout)
}