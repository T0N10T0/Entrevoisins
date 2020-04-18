
package com.openclassrooms.entrevoisins.neighbour_list;

import android.view.View;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;


/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

    // This is fixed
    private static int ITEMS_COUNT = 12;

    private ListNeighbourActivity mActivity;

    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule =
            new ActivityTestRule(ListNeighbourActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myNeighboursList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(withId(R.id.list_neighbours))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        onView(withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(withId(R.id.list_neighbours))
                .perform(actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT - 1));
    }

    /**
     * When we click on an item, the item's detail screen is shown
     */
    @Test
    public void myNeighboursList_shouldShowDetailScreen() {
        // When perform a click on an element
        onView(withId(R.id.list_neighbours)).perform(actionOnItemAtPosition(0, click()));
        // Then : the neighbour's detail screen is shown
        onView(withId(R.id.mDetailAvatar)).check(matches(isDisplayed()));
    }

    /**
     * When on the detail screen, the user's TextView is the same as in the recyclerview holder
     */
    @Test
    public void myNeighboursList_neighbourNameShouldBeTheSame() {
        // When perform a click on an element
        onView(withId(R.id.list_neighbours)).perform(actionOnItemAtPosition(0, click()));
        // Then : the displayed name is the same as in the neighbours list
        onView(withId(R.id.mDetailName)).check(matches(withText("Caroline")));
    }

    /**
     * When Favorites Tab is selected, it only show favorites neighbours
     */
    @Test
    public void myNeighboursList_showOnlyFavoriteNeighbours() {
        // When perform a click on an element
        onView(withId(R.id.list_neighbours)).perform(actionOnItemAtPosition(0, click()));
        // Then : perform a click on the fav button
        onView(withId(R.id.fab)).perform(click());
        // Close DetailPage
        // pressBack();
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        // Select 'FAVORITES' tab
//        Matcher<View> newMatcher = CoreMatchers.allOf(withText("FAVORITES"), isDescendantOfA(withId(R.id.tabs)));
        onView(CoreMatchers.allOf(withText("FAVORITES"), isDescendantOfA(withId(R.id.tabs)))).perform(click());
        // When perform a click on a item
        onView(withId(R.id.list_favorite_neighbours)).perform(actionOnItemAtPosition(0, click()));
        // Then : check if the neighbour is favorite

//        onView(CoreMatchers.allOf(withId(R.id.fab), withTagValue(is((Object) "isFavorite")))).check(matches(isDisplayed()));
//        onView(withId(R.id.mDetailName).matches("isFavorite"));
        // onView(withId(R.id.list_favorite_neighbours)).check(withItemCount(1));
        onView(withId(R.id.mDetailName)).check(matches(withText("Caroline")));
    }
}
