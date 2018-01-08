package com.github.rossdanderson.stateless4k

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TransitionTests {

    @Test
    fun IdentityTransitionIsNotChange() {
        val t = Transition(1, 1, 0)
        assertTrue(t.isReentry)
    }

    @Test
    fun TransitioningTransitionIsChange() {
        val t = Transition(1, 2, 0)
        assertFalse(t.isReentry)
    }
}
