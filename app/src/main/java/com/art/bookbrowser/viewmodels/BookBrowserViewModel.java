package com.art.bookbrowser.viewmodels;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;

import com.art.bookbrowser.R;
import com.art.bookbrowser.base.BaseViewModel;
import com.art.bookbrowser.helpers.RxUtils;
import com.art.bookbrowser.interfaces.Repository;
import com.art.bookbrowser.models.Book;

import java.util.List;
import javax.inject.Inject;
import lombok.Getter;

public class BookBrowserViewModel extends BaseViewModel {
    private Repository repository;
    private RxUtils rxUtils;

    @Getter
    private MutableLiveData<Book> book = new MutableLiveData();
    @Getter
    private MutableLiveData<Book> deleteBook = new MutableLiveData();
    @Getter
    private MutableLiveData<List<Book>> books = new MutableLiveData();

    @Inject
    BookBrowserViewModel(Application app,
                         Repository repository,
                   RxUtils rxUtils
    ){
        super(app);
        this.repository = repository;
        this.rxUtils = rxUtils;
    }

    public void getData(){
        disposables.add(rxUtils.baseCall(repository.getBooks())
                .subscribe(
                        response -> {
                            passMessage(resources.getString(R.string.books_fetch_success));
                            books.postValue(response);
                        }, throwable -> passMessage(resources.getString(R.string.books_fetch_failure))
                )
        );
    }

    public void addBook(Book book){
        disposables.add(rxUtils.baseCall(repository.addBook(book))
                .subscribe(
                        response -> {
                            passMessage(resources.getString(R.string.add_book_success));
                            this.book.postValue(response);
                            getData();
                        }, throwable -> passMessage(resources.getString(R.string.add_book_failure))
                ));
    }

    public void deleteBook(Book book){
        disposables.add(rxUtils.baseCall(repository.deleteBook(book.getId()))
                .subscribe(
                        response -> {
                            deleteBook.postValue(book);
                            passMessage(resources.getString(R.string.delete_book_success));
                        } ,
                        throwable -> passMessage(resources.getString(R.string.delete_book_failure))
                ));
    }
}
