package io.hipteam.testutil;

import android.graphics.Rect;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.ImageButton;

import com.jraska.falcon.FalconSpoon;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.setFailureHandler;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static io.hipteam.testutil.EspressoTestHelper.clickOnViewGroupChildAt;
import static io.hipteam.testutil.EspressoTestHelper.getCurrentActivity;
import static io.hipteam.testutil.EspressoTestHelper.getViewById;
import static io.hipteam.testutil.EspressoTestHelper.typeInEditText;
import static io.hipteam.testutil.EspressoTestHelper.typeTextOnViewGroupChildAt;
import static io.hipteam.testutil.EspressoTestHelper.withToolbarTitle;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by richardradics on 08/03/16.
 */
public class BaseTest {
    public Solo solo;

    @Before
    public void clearSharedPreferences() {
        setFailureHandler(new CustomFailureHandler(InstrumentationRegistry.getTargetContext()));
        solo = new Solo(getInstrumentation(), getCurrentActivity());
    }

    public void waitForView(int resourceId) {
        solo.waitForView(resourceId);
    }

    public void waitForText(String text) {
        solo.waitForText(text);
    }

    public void screenshot(String imageName) {
        FalconSpoon.screenshot(getCurrentActivity(), imageName);
    }

    public void typeIntoEditText(int viewId, String text) {
        waitForView(viewId);
        scrollToView(viewId);
        typeInEditText(viewId, text);
        closeSoftKeyboard();
    }

    public void clickOnToolbar(int toolbarId) {
        onView(allOf(instanceOf(ImageButton.class), isDescendantOfA(withId(toolbarId)))).perform(click());
    }

    public void clickOnText(String text) {
        waitForText(text);
        onView(withText(text)).perform(click());
    }

    public void clickOnText(int resourceId) {
        waitForText(getCurrentActivity().getString(resourceId));
        onView(withText(resourceId)).perform(click());
    }

    public void clickOnId(int resourceId) {
        waitForView(resourceId);
        scrollToView(resourceId);
        onView(withId(resourceId)).perform(click());
    }

    public void clickOnDialogButtonText(int stringResId) {
        solo.clickOnButton(solo.getString(stringResId));
    }

    public void clickOnDialogButtonText(String text) {
        solo.clickOnButton(text);
    }

    public void clickOnSpinnerItem(int spinnerId, int spinnerItemTextId) {
        clickOnSpinnerItem(spinnerId, solo.getString(spinnerItemTextId));
    }

    public void clickOnSpinnerItem(int spinnerId, String spinnterItemText) {
        onView(withId(spinnerId)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(spinnterItemText))).perform(click());
    }

    public void clickOnScreen(int x, int y) {
        solo.clickOnScreen(x, y);
    }

    public void scrollOnePageUp() {
        solo.scrollUp();
    }

    public void scrollOnePageDown() {
        solo.scrollDown();
    }

    public void swipeLeftView(int resourceId) {
        getViewById(resourceId).perform(swipeLeft());
    }

    public void swipeRightViewByText(int resourceId) {
        waitForText(getCurrentActivity().getString(resourceId));
        onView(allOf(withText(resourceId), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(swipeRight());
    }

    public void swipeRightView(int resourceId) {
        getViewById(resourceId).perform(swipeRight());
    }

    public void swipeDownView(int resourceId) {
        getViewById(resourceId).perform(swipeDown());
    }

    public void swipeUpView(int resourceId) {
        getViewById(resourceId).perform(swipeUp());
    }

    public void isViewDisplayed(int resourceId) {
        onView(withId(resourceId)).check(matches(isDisplayed()));
    }

    public void isViewWithTextDisplayed(int stringId) {
        onView(withText(stringId)).check(matches(isDisplayed()));
    }

    public void checkToolbarTitle(CharSequence title) {
        onView(isAssignableFrom(Toolbar.class))
                .check(matches(withToolbarTitle(is(title))));
    }

    public void checkToolbarTitleWithId(int titleId) {

        onView(isAssignableFrom(Toolbar.class))
                .check(matches(withToolbarTitle(is((CharSequence) getCurrentActivity().getString(titleId)))));
    }

    public void checkViewWithText(int resourceId, String text) {
        onView(withId(resourceId)).check(ViewAssertions.matches(withText(text)));
    }

    public void clickOnListAtPosition(int listViewId, int position) {
        waitForView(listViewId);
        clickOnViewGroupChildAt(listViewId, position);
    }

    public void clickOnActionBarOverflow() {
        DisplayMetrics metrics = solo.getCurrentActivity().getResources().getDisplayMetrics();
        Rect rectangle = new Rect();
        Window window = solo.getCurrentActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        solo.clickOnScreen(metrics.widthPixels - 20, statusBarHeight + 24); // assuming notification area on top
    }

    public void typeTextListItemAtPosition(int listViewId, int position, int inputId, String text) {
        waitForView(listViewId);
        typeTextOnViewGroupChildAt(listViewId, position, inputId, text);
    }

    @After
    public void tearDown() throws Exception {
        try {
            solo.finishOpenedActivities();
            solo.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void scrollToView(int resourceId) {
        try {
            onView(withId(resourceId)).perform(ViewActions.scrollTo()).check(ViewAssertions.matches(isDisplayed()));
        } catch (PerformException e) {
        }
    }
}
