package com.art.bookbrowser.repository;

import com.art.bookbrowser.interfaces.RepositoryApi;
import com.art.bookbrowser.models.Book;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class TestDataRepository implements RepositoryApi {

    public TestDataRepository(){ }

    @Override
    public Single<Book> getBook(String bookId){
        return Single.just(new Book());
    }

    @Override
    public Single<Boolean> addBook(Book book){
        return Single.just(true);
    }

    @Override
    public Single<Boolean> deleteBook(Book book){
        return Single.just(true);
    }

    @Override
    public Single<List<Book>> getBooks(){
        ArrayList<Book> books = new ArrayList();
        books.add(new Book());
        return Single.just(books);
    }
}
