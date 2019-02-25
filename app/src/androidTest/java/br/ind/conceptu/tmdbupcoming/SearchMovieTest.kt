package br.ind.conceptu.tmdbupcoming

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.test.espresso.IdlingRegistry
import br.ind.conceptu.tmdbupcoming.view.SearchMovieActivity
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchMovieTest {

    @get: Rule
    val searchActivity = ActivityTestRule(SearchMovieActivity::class.java)

    private val searchQuery = "How to Train Your Dragon"

    @Before
    fun registerIdleResource() {
        val componentIdlingResource = searchActivity.activity.countingIdlingResource
        IdlingRegistry.getInstance().register(componentIdlingResource)
    }

    @Test
    fun searchTest() {
        Espresso.onView(ViewMatchers.withId(R.id.searchBar))
                .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.isAssignableFrom(AutoCompleteTextView::class.java))
                .perform(ViewActions.typeText(searchQuery))
        Espresso.onView(ViewMatchers.isAssignableFrom(AutoCompleteTextView::class.java))
                .perform(ViewActions.pressImeActionButton(), ViewActions.closeSoftKeyboard())

        val clickAction = RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.searchResultList)).perform(clickAction)
        Espresso.onView(AllOf.allOf(IsInstanceOf.instanceOf(TextView::class.java), ViewMatchers.withParent(ViewMatchers.withId(R.id.toolbar))))
                .check(ViewAssertions.matches(ViewMatchers.withSubstring(searchQuery)))
    }
}