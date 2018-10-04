package com.art.bookbrowser.viewmodels;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;

import com.art.bookbrowser.R;
import com.art.bookbrowser.base.BaseViewModel;
import com.art.bookbrowser.helpers.RxUtils;
import com.art.bookbrowser.interfaces.RepositoryApi;
import com.art.bookbrowser.models.Book;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

public class BookDetailsViewModel extends BaseViewModel {
    private RepositoryApi repositoryApi;
    private RxUtils rxUtils;

    @Getter
    private MutableLiveData<Book> book = new MutableLiveData();
    @Setter
    private String bookId;

    @Inject
    BookDetailsViewModel(Application app,
                         RepositoryApi repositoryApi,
                         RxUtils rxUtils
    ){
        super(app);
        this.repositoryApi = repositoryApi;
        this.rxUtils = rxUtils;
    }

    public void getData() {
        disposables.add(rxUtils.baseCall(repositoryApi.getBook(bookId))
                .subscribe(
                        response -> {
                            passMessage(resources.getString(R.string.book_details_fetch_success));
                            book.postValue(response);
                        }, throwable -> {
                            passMessage(resources.getString(R.string.book_details_fetch_failure));
                        }));
    }
}
