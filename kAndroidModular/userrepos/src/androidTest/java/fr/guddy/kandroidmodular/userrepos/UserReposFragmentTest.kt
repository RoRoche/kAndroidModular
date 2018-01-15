package fr.guddy.kandroidmodular.userrepos

import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import com.agoda.kakao.KRecyclerItem
import com.agoda.kakao.KRecyclerView
import com.agoda.kakao.KTextView
import com.agoda.kakao.Screen
import com.android21buttons.fragmenttestrule.FragmentTestRule
import fr.guddy.kandroidmodular.common.fsm.fsmModule
import fr.guddy.kandroidmodular.common.usecases.DebugActivity
import fr.guddy.kandroidmodular.userrepos.di.userReposModule
import fr.guddy.kandroidmodular.userrepos.mvvm.UserReposFragment
import fr.guddy.kandroidmodular.userrepos.net.GitHubApi
import fr.guddy.kandroidmodular.userrepos.net.dto.Repo
import io.reactivex.Single
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.Koin
import org.koin.dsl.module.applicationContext

class UserReposFragmentTest {
    @get:Rule
    var fragmentTestRule = FragmentTestRule(
            DebugActivity::class.java,
            UserReposFragment::class.java,
            true,
            true,
            false
    )

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
        itemType(UserReposFragmentTest::ItemRepo)
    })
    //endregion

    //region Test lifecycle
    @Before
    fun before() {
        val modules = listOf(
                mockNetModule,
                fsmModule,
                userReposModule
        )
        context = Koin().build(modules)
    }
    //endregion

    //region Test methods
    @Test
    fun it_displays_user_and_repos() {
        // given
        val text = "TEST_USER"
        val fragment = UserReposFragment.newInstance(text)

        // when
        fragmentTestRule.launchFragment(fragment)

        // then
        thenItDisplaysUserAndRepos(text)
    }
    //endregion

    //region Inner job
    private fun thenItDisplaysUserAndRepos(text: String) {
        Espresso.onView(ViewMatchers.withId(R.id.textViewUser)).check(
                ViewAssertions.matches(
                        ViewMatchers.withText(text)
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