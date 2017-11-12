package io.github.mellamopablo.simondice.extensions

val <T> List<T>.randomElement get() = this[(Math.random() * this.size).toInt()]