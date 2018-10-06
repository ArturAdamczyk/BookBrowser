package com.art.bookbrowser.interfaces.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.art.bookbrowser.models.Book;

import java.util.List;

import io.reactivex.Single;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface BookDao {

    @Query("SELECT * FROM Book WHERE id LIKE :bookId")
    Single<Book> getBook(String bookId);

    @Query("SELECT * FROM Book")
    Single<List<Book>> getBooks();

    @Insert(onConflict = REPLACE)
    long addBook(Book book);

    @Delete
    int deleteBook(Book book);
}
