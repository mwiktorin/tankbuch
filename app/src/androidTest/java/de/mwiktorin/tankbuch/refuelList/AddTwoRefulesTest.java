package de.mwiktorin.tankbuch.refuelList;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import de.mwiktorin.tankbuch.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddTwoRefulesTest {

    @Rule
    public ActivityTestRule<RefuelList> mActivityTestRule = new ActivityTestRule<>(RefuelList.class);

    @Test
    public void addTwoRefulesTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.first_start_start_button), withText("Start app"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.main_add_button),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.add_refuel_milage),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_refuel_layout_milage),
                                        0),
                                0)));
        appCompatEditText.perform(scrollTo(), replaceText("123"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.add_refuel_milage), withText("123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_refuel_layout_milage),
                                        0),
                                0)));
        appCompatEditText2.perform(pressImeActionButton());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.add_refuel_price_per_litre),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_refuel_layout_price_per_litre),
                                        0),
                                0)));
        appCompatEditText3.perform(scrollTo(), replaceText("1.32"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.add_refuel_price_per_litre), withText("1.32"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_refuel_layout_price_per_litre),
                                        0),
                                0)));
        appCompatEditText4.perform(pressImeActionButton());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.add_refuel_price),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_refuel_layout_price),
                                        0),
                                0)));
        appCompatEditText5.perform(scrollTo(), replaceText("23"), closeSoftKeyboard());

        ViewInteraction editText = onView(
                allOf(withId(R.id.add_refuel_volume), withText("17.42"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_refuel_layout_volume),
                                        0),
                                0),
                        isDisplayed()));
        editText.check(matches(withText("17.42")));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.add_refuel_save), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                18)));
        appCompatButton2.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(withId(R.id.main_refuel_entry_milage), withText("123 km"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_recycler_view),
                                        0),
                                3),
                        isDisplayed()));
        textView.check(matches(withText("123 km")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.main_refuel_entry_price_per_litre), withText("1.32 €/L"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_recycler_view),
                                        0),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("1.32 €/L")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.main_refuel_entry_price), withText("22.99 €"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_recycler_view),
                                        0),
                                2),
                        isDisplayed()));
        textView3.check(matches(withText("22.99 €")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.main_refuel_efficiency), withText("first entry"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_recycler_view),
                                        0),
                                4),
                        isDisplayed()));
        textView4.check(matches(withText("first entry")));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.main_add_button),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.main_add_button),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.add_refuel_milage), withText("123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_refuel_layout_milage),
                                        0),
                                0),
                        isDisplayed()));
        editText2.check(matches(withText("123")));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.add_refuel_price_per_litre),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_refuel_layout_price_per_litre),
                                        0),
                                0)));
        appCompatEditText6.perform(scrollTo(), replaceText("1.34"), closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.add_refuel_price),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_refuel_layout_price),
                                        0),
                                0)));
        appCompatEditText7.perform(scrollTo(), replaceText("45"), closeSoftKeyboard());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.add_refuel_volume), withText("33.58"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_refuel_layout_volume),
                                        0),
                                0),
                        isDisplayed()));
        editText3.check(matches(withText("33.58")));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.add_refuel_save), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                18)));
        appCompatButton3.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.main_refuel_entry_milage), withText("123 km"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_recycler_view),
                                        0),
                                3),
                        isDisplayed()));
        textView5.check(matches(withText("123 km")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.main_refuel_entry_price_per_litre), withText("1.34 €/L"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_recycler_view),
                                        0),
                                1),
                        isDisplayed()));
        textView6.check(matches(withText("1.34 €/L")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.main_refuel_entry_price), withText("45.00 €"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_recycler_view),
                                        0),
                                2),
                        isDisplayed()));
        textView7.check(matches(withText("45.00 €")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.main_refuel_efficiency), withText("∞ L/100 km"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_recycler_view),
                                        0),
                                4),
                        isDisplayed()));
        textView8.check(matches(withText("∞ L/100 km")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
