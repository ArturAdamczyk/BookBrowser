package com.art.bookbrowser.interfaces;

import com.art.bookbrowser.models.Book;

import java.util.List;

import io.reactivex.Single;

public interface RepositoryApi {
    Single<Book> getBook(String bookId);

    Single<Boolean> addBook(Book book);

    Single<Boolean> deleteBook(Book book);

    Single<List<Book>> getBooks();

}
