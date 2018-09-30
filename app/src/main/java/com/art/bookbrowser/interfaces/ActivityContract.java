package com.art.bookbrowser.interfaces;

public interface ActivityContract {
    void injectDependencies();
    void initViewModel();
    void openActivity(Class targetActivity);
}