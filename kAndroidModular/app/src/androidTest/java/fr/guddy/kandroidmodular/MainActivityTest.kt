package fr.guddy.kandroidmodular

import android.arch.lifecycle.ViewModelProviders
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.agoda.kakao.KRecyclerItem
import com.agoda.kakao.KRecyclerView
import com.agoda.kakao.KTextView
import com.agoda.kakao.Screen
import fr.guddy.kandroidmodular.di.allModules
import fr.guddy.kandroidmodular.di.netModule
import fr.guddy.kandroidmodular.common.fsm.FsmContext
import fr.guddy.kandroidmodular.common.fsm.FsmViewModel
import fr.guddy.kandroidmodular.userrepos.net.GitHubApi
import fr.guddy.kandroidmodular.userrepos.net.dto.Repo
import fr.guddy.kandroidmodular.usecases.MainActivity
import fr.guddy.kandroidmodular.userinput.fsm.UserInputResult
import fr.guddy.kandroidmodular.userinput.fsm.userInputResult
import fr.guddy.kandroidmodular.userrepos.fsm.ShowingUserRepos
import io.reactivex.Single
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.Koin
import org.koin.dsl.module.applicationContext
import java.util.concurrent.CountDownLatch


@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    var activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    //region Koin
    private val mockGitHubApi: GitHubApi = object : GitHubApi {
        override fun listRepos(user: String): Single<List<Repo>> = Single.just(
                listOf(
                        Repo(id = "12", name = "TestName", description = "TestDescription")
                )
        )
    }
    private lateinit var context: Koin
    private val mockNetModule = applicationContext {
        provide { mockGitHubApi }
    }
    //endregion

    //region kakao
    class MainScreen : Screen<MainScreen>()

    class ItemRepo(parent: Matcher<View>) : KRecyclerItem<ItemRepo>(parent) {
        val name: KTextView = KTextView(parent) { withId(R.id.textViewRepoName) }
        val description: KTextView = KTextView(parent) { withId(R.id.textViewRepoDescription) }
    }

    val screen = MainScreen()
    val recycler: KRecyclerView = KRecyclerView({
        withId(R.id.listRepos)
    }, itemTypeBuilder = {
        itemType(::ItemRepo)
    })
    //endregion

    //region Test lifecycle
    @Before
    fun before() {
        val modules = allModules.toMutableList()
        modules.remove(netModule)
        modules.add(mockNetModule)
        context = Koin().build(modules)
    }
    //endregion

    //region Test methods
    @Test
    fun when_typing_a_user_and_clicking_on_select_it_displays_user_and_repos() {
        // given
        val text = "test_user"
        activityRule.launchActivity(null)

        // when
        onView(withId(R.id.editTextUser))
                .perform(typeText(text))
                .perform(closeSoftKeyboard())
        onView(withId(R.id.buttonSelect))
                .perform(click())

        // then
        thenItDisplaysUserAndRepos(text)
    }

    @Test
    fun when_going_directly_to_repos_list_with_filled_user_it_displays_user_and_repos() {
        // given
        val text = "test_user"
        activityRule.launchActivity(null)
        val fsmViewModel = ViewModelProviders.of(activityRule.activity).get(FsmViewModel::class.java)
        val fsmContext = FsmContext()
        fsmContext.userInputResult = UserInputResult(text)
        fsmContext.state = ShowingUserRepos

        // when
        whenGoingToState(fsmViewModel, fsmContext)

        // then
        thenItDisplaysUserAndRepos(text)
    }
    //endregion

    //region Inner job
    private fun whenGoingToState(fsmViewModel: FsmViewModel, fsmContext: FsmContext) {
        val countDownLatch = CountDownLatch(1)
        activityRule.activity.runOnUiThread {
            fsmViewModel.updateFlowContext(fsmContext)
            countDownLatch.countDown()
        }
        countDownLatch.await()
    }

    private fun thenItDisplaysUserAndRepos(text: String) {
        onView(withId(R.id.textViewUser)).check(
                matches(
                        withText(text)
                )
        )
        screen {
            recycler {
                firstChild<ItemRepo> {
                    isVisible()
                    name { hasText("TestName") }
                    description { hasText("TestDescription") }
                }
            }
        }
    }
    //endregion
}
