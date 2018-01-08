package com.github.rossdanderson.stateless4k.interfaces

import com.github.rossdanderson.stateless4k.Transition

interface StateAction<in S, in T, in V> {

    operator fun invoke(target: V,
                        transition: Transition<S, T>)
}
