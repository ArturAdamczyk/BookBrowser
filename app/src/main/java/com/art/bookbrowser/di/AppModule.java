package com.art.bookbrowser.di;

import android.app.Application;
import android.content.Context;

import com.art.bookbrowser.App;
import com.art.bookbrowser.db.AppDatabase;
import com.art.bookbrowser.helpers.Messenger;
import com.art.bookbrowser.helpers.RestApiFactory;
import com.art.bookbrowser.helpers.SnackBarQueue;
import com.art.bookbrowser.interfaces.Repository;
import com.art.bookbrowser.interfaces.dao.BookDao;
import com.art.bookbrowser.interfaces.rest.RestApi;
import com.art.bookbrowser.params.WebServiceConfiguration;
import com.art.bookbrowser.repository.RepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@Module
public class AppModule {
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @AppContext
    public Context providesContext() {
        return app.getApplicationContext();
    }

    @Provides
    public Application providesApplication() {
        return app;
    }

    @Provides
    public Messenger provideMessenger() {
        return new Messenger(provideSnackBarQueue());
    }

    @Provides
    public SnackBarQueue provideSnackBarQueue() {
        return new SnackBarQueue();
    }

    @Provides
    @Singleton
    public Repository provideRepository() {
        return new RepositoryImpl();
    }

    @Provides
    @Singleton
    public RestApi provideRestApi() {
        return RestApiFactory.getService(WebServiceConfiguration.WEB_SERVICE_ADDRESS);
    }

    @Provides
    @Singleton
    public AppDatabase providesAppDatabase() {
        return AppDatabase.getAppDatabase(app);
    }

    @Provides
    @Singleton
    public BookDao providesBookDao(AppDatabase database) {
        return database.bookDao();
    }

    @Provides
    @Singleton
    public Timber.DebugTree providesDebugTree() {
        return new Timber.DebugTree();
    }

}