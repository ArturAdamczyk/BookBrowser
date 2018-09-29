package com.art.bookbrowser.interfaces;

import com.art.bookbrowser.models.Book;

import java.util.List;

import io.reactivex.Single;

public interface Repository {
    Single<Book> getBook(String bookId);

    Single<Book> addBook(Book book);

    Single<Boolean> deleteBook(String bookId);

    Single<List<Book>> getBooks();

}
