package com.art.bookbrowser.repository;

import com.art.bookbrowser.interfaces.Repository;
import com.art.bookbrowser.interfaces.dao.BookDao;
import com.art.bookbrowser.interfaces.rest.RestApi;
import com.art.bookbrowser.models.Book;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class RepositoryImpl implements Repository {
    private RestApi restApi;
    private BookDao bookDao;

    // TODO
    // App should fetch and combine data from local and remote storage.
    // If no internet connection is available than only local data should be used (for browsing books, other options should be unavailable)

    public RepositoryImpl(){ }

    @Override
    public Single<Book> getBook(String bookId){
        return Single.just(new Book());
    }

    @Override
    public Single<Book> addBook(Book book){
        return Single.just(book);
    }

    @Override
    public Single<Boolean> deleteBook(String bookId){
        return Single.just(true);
    }

    @Override
    public Single<List<Book>> getBooks(){
        ArrayList<Book> books = new ArrayList();
        books.add(new Book());
        return Single.just(books);
    }
}
