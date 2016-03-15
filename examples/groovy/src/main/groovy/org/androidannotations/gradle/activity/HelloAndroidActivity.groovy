package org.androidannotations.gradle.activity

import android.app.Activity
import android.widget.TextView
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.UiThread
import org.androidannotations.annotations.ViewById
import org.androidannotations.annotations.res.StringRes
import org.androidannotations.gradle.R

@EActivity(R.layout.main)
public class HelloAndroidActivity extends Activity {

    @StringRes
    protected String hello // @PackageScope or no modifier cannot be used for AA annotated fields!

    @ViewById
    protected TextView helloTextView

    @UiThread
    @AfterViews
    void afterViews() {
        Date now = new Date()
        String helloMessage = String.format(hello, now.toString())
        helloTextView.text = helloMessage
    }
}
