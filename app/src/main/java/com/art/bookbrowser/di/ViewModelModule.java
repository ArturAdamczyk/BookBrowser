package com.art.bookbrowser.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.art.bookbrowser.base.ViewModelFactory;
import com.art.bookbrowser.viewmodels.BookBrowserViewModel;
import com.art.bookbrowser.viewmodels.BookDetailsViewModel;
import com.art.bookbrowser.viewmodels.LoginViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory baseViewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    public abstract ViewModel loginViewModel(LoginViewModel loginViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BookDetailsViewModel.class)
    public abstract ViewModel bookDetailsViewModel(BookDetailsViewModel bookDetailsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BookBrowserViewModel.class)
    public abstract ViewModel bookBrowserViewModel(BookBrowserViewModel bookBrowserViewModel);

}
