package com.track_it.util;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.common.io.Files;
import com.track_it.R;
import com.track_it.application.Main;
import com.track_it.application.SetupParameters;
import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.persistence.SubscriptionPersistence;
import com.track_it.persistence.SubscriptionTagPersistence;
import com.track_it.persistence.fakes.FakeSubscriptionPersistenceDatabase;
import com.track_it.persistence.fakes.FakeSubscriptionTagPersistenceDatabase;
import com.track_it.persistence.hsqldb.SubscriptionPersistenceHSQLDB;
import com.track_it.persistence.hsqldb.SubscriptionTagPersistenceHSQLDB;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TestUtils {
    private static File DB_SRC;

    private static boolean useRealDatabase = false; //Should we use real database? - Default false means use fakeDataBase


    // Remove all subs in the database
    public static void clearDatabase(SubscriptionHandler subHandler) {
        List<SubscriptionObj> allSubs = subHandler.getAllSubscriptions();

        for (SubscriptionObj currSub : allSubs) {
            subHandler.removeSubscriptionByID(currSub.getID());
        }


    }

    // Populate database with 3 subs
    public static void populateDatabase(SubscriptionHandler subHandler) {
        //Create sub 1
        String name_1 = "Youtube";
        String frequency_1 = subHandler.getFrequencyNameList().get(4);
        int Payment_1 = 1956;

        SubscriptionObj newSub1 = new SubscriptionObj(name_1, Payment_1, frequency_1);
        subHandler.addSubscription(newSub1);

        //Create sub 2
        String name_2 = "Amazon prime";
        String frequency_2 = subHandler.getFrequencyNameList().get(4);
        int Payment_2 = 1000;
        String tags_2 = "movies";

        SubscriptionObj newSub2 = new SubscriptionObj(name_2, Payment_2, frequency_2);
        subHandler.setTags(newSub2, tags_2);
        subHandler.addSubscription(newSub2);

        // Create sub 3
        String name_3 = "Dark-Zone Pass";
        String frequency_3 = subHandler.getFrequencyNameList().get(3);
        int Payment_3 = 1350;
        String tags_3 = "fun"; //Dark zone was fun

        SubscriptionObj newSub3 = new SubscriptionObj(name_3, Payment_3, frequency_3);
        subHandler.setTags(newSub3, tags_2);
        subHandler.addSubscription(newSub3);

    }


    //This rather ugly code was auto generated by Espresso recorder, as we were having trouble targeting the search menu.
    // This will target and type into the search bar on the homepage with search string of inputSearch.
    public static void typeInSearch(String inputSearch) {

        ViewInteraction appCompatImageView = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatImageView")), withContentDescription("Search"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withId(R.id.search_by_name),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction searchAutoComplete = onView(
                allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete.perform(click());

        ViewInteraction searchAutoComplete2 = onView(
                allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        TestUtils.childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete2.perform(replaceText(inputSearch), closeSoftKeyboard());

    }

    // Commonly used function. This adds a subscription to the database, starting from the home page, and then goes back to home page.
    public static void addUser(String subName, String subPaymentAmount, String subFrequency, String subTags) {

        onView(withId(R.id.add_subscription_button)).perform(click()); // Click add sub - Starting from home page

        onView(withId(R.id.input_subscription_name)).perform(replaceText(subName)); // Give name
        onView(withId(R.id.input_payment_amount)).perform(replaceText(subPaymentAmount)); // Give payment
        onView(withId(R.id.subscription_list)).perform(click()); // Click frequency
        onView(withText(subFrequency)).inRoot(RootMatchers.isPlatformPopup()).perform(click());// Give frequency
        onView(withId(R.id.tag_input)).perform(replaceText(subTags)); // Give Tags

        onView(withId(R.id.submit_sub_button)).perform(click()); // Click add, and go back to home back

    }




    // This is match function, auto generated by espresso.
    public static Matcher<View> childAtPosition(
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


