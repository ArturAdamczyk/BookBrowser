package com.art.bookbrowser.di;

import com.art.bookbrowser.App;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ViewModelModule.class})
public interface AppComponent {
    ActivityComponent getActivityComponent(ActivityModule module);

    void inject(App app);
}
