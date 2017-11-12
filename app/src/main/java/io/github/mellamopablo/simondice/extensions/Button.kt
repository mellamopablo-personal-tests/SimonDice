package io.github.mellamopablo.simondice.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import io.github.mellamopablo.simondice.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

val Button.clicks get(): Observable<Unit> {
	val subject = PublishSubject.create<Unit>()
	this.setOnClickListener { subject.onNext(Unit) }
	return subject.share()
}

fun Button.flash(duration: Long) {
	fun resourceToColor(id: Int) = resources.getString(id).replace("#", "")

	if (this.background is ColorDrawable) {
		val background = this.background as ColorDrawable
		val originalColor = background.color

		val flashingColor = when (Integer.toHexString(originalColor)) {
			resourceToColor(R.color.greenButton) -> R.color.greenButtonLight
			resourceToColor(R.color.redButton) -> R.color.redButtonLight
			resourceToColor(R.color.yellowButton) -> R.color.yellowButtonLight
			resourceToColor(R.color.blueButton) -> R.color.blueButtonLight
			else -> null
		}

		flashingColor?.let {
			val nextColor = Color.parseColor("#${resourceToColor(it)}")

			val flashAnimation = ValueAnimator.ofObject(ArgbEvaluator(), originalColor, nextColor)
			val unFlashAnimation = ValueAnimator.ofObject(ArgbEvaluator(), nextColor, originalColor)

			flashAnimation.duration = duration / 2
			unFlashAnimation.duration = duration / 2

			flashAnimation.addUpdateListener {
				this.setBackgroundColor(it.animatedValue as Int)
			}

			unFlashAnimation.addUpdateListener {
				this.setBackgroundColor(it.animatedValue as Int)
			}

			flashAnimation.addListener(object : AnimatorListenerAdapter() {
				override fun onAnimationEnd(animation: Animator) {
					super.onAnimationEnd(animation)
					unFlashAnimation.start()
				}
			})

			flashAnimation.start()
		}
	}
}
