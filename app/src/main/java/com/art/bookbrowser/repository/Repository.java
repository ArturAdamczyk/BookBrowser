package com.art.bookbrowser.repository;

import com.art.bookbrowser.helpers.RxUtils;
import com.art.bookbrowser.interfaces.RepositoryApi;
import com.art.bookbrowser.interfaces.dao.BookDao;
import com.art.bookbrowser.interfaces.rest.RestApi;
import com.art.bookbrowser.models.Book;

import java.util.List;

import io.reactivex.Single;

public class Repository implements RepositoryApi {
    private RestApi restApi;
    private BookDao bookDao;
    private RxUtils rxUtils;

    // TODO
    // App should fetch and combine data from local and remote storage.
    // If no internet connection is available than only local data should be used (for browsing books, other options should be unavailable)

    public Repository(BookDao bookDao,
                      RestApi restApi,
                      RxUtils rxUtils){
        this.bookDao = bookDao;
        this.restApi = restApi;
        this.rxUtils = rxUtils;
    }

    @Override
    public Single<Book> getBook(String bookId){
        return bookDao.getBook(bookId);
    }

    @Override
    public Single<Boolean> addBook(Book book){
        return Single
                .create((emitter) -> {
                    bookDao.addBook(book);
                    emitter.onSuccess(true);
                });
    }

    @Override
    public Single<Boolean> deleteBook(Book book){
        return Single.create((emitter) -> {
            bookDao.deleteBook(book);
            emitter.onSuccess(true);
        });
    }

    @Override
    public Single<List<Book>> getBooks(){
        return bookDao.getBooks();
    }
}
