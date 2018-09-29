package com.art.bookbrowser.di;

import com.art.bookbrowser.activities.BookBrowserActivity;
import com.art.bookbrowser.activities.BookDetailsActivity;
import com.art.bookbrowser.activities.LoginActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(LoginActivity activity);
    void inject(BookBrowserActivity activity);
    void inject(BookDetailsActivity activity);
}

