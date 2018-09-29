package com.art.bookbrowser.interfaces.rest;

import com.art.bookbrowser.models.Book;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApi {
    @GET("Books")
    Single<List<Book>> getBooks();

    @GET("Book/{id}")
    Single<Book> getBook(@Path("id") String bookId);

    @POST("Book")
    Single<Book> addBook(@Body Book book);

    @DELETE("Book/{id}")
    Single<Boolean> deleteBook(@Path("id") String bookId);
}
