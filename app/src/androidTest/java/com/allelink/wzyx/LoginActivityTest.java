package com.allelink.wzyx;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.allelink.wzyx.activity.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by yangc on 2018/3/21.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> mTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void testLogin(){
        onView(withId(R.id.et_login_phone_number)).perform(replaceText("15700178425"),closeSoftKeyboard());
        onView(withId(R.id.et_login_password)).perform(replaceText("ych123"), closeSoftKeyboard());
        onView(withId(R.id.btn_login_login)).perform(click());

    }

}
