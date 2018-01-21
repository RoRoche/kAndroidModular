package fr.guddy.kandroidmodular

import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import com.android21buttons.fragmenttestrule.FragmentTestRule
import fr.guddy.kandroidmodular.common.usecases.DebugActivity
import fr.guddy.kandroidmodular.userinput.R
import fr.guddy.kandroidmodular.userinput.mvvm.UserInputFragment
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
        Espresso.onView(ViewMatchers.withId(R.id.editTextUser)).check(ViewAssertions.matches(ViewMatchers.withText(text)))
        Espresso.onView(ViewMatchers.withId(R.id.buttonSelect))
                .check(ViewAssertions.matches(ViewMatchers.isClickable()))
                .check(ViewAssertions.matches(ViewMatchers.withText(R.string.user_input_button)))
    }
}