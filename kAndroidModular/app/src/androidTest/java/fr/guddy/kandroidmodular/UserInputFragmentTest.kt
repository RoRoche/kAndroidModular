package fr.guddy.kandroidmodular

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import com.android21buttons.fragmenttestrule.FragmentTestRule
import fr.guddy.kandroidmodular.usecases.DebugActivity
import fr.guddy.kandroidmodular.usecases.userinput.mvvm.UserInputFragment
import org.junit.Rule
import org.junit.Test

class UserInputFragmentTest {
    @get:Rule
    var fragmentTestRule = FragmentTestRule(
            DebugActivity::class.java,
            UserInputFragment::class.java,
            true,
            true,
            false
    )

    @Test
    fun it_displays_prefilled_user_and_clickable_select_button() {
        // given
        val text = "TEST_USER"
        val fragment = UserInputFragment.newInstance(text)

        // when
        fragmentTestRule.launchFragment(fragment)

        // then
        onView(withId(R.id.editTextUser)).check(matches(withText(text)))
        onView(withId(R.id.buttonSelect))
                .check(matches(isClickable()))
                .check(matches(withText(R.string.user_input_button)))
    }
}