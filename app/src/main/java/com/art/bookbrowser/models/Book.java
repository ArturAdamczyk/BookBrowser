package com.art.bookbrowser.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import static java.util.Comparator.comparing;

@Entity(tableName = "Book")
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Book {
    @PrimaryKey
    @NonNull
    @SerializedName("Id")
    @Expose(serialize = false)
    //private String id;
    private String id = String.valueOf(UUID.randomUUID());
    @NonNull
    @SerializedName("Title")
    @Expose
    private String title;
    @NonNull
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("CoverUrl")
    @Expose
    private String coverUrl;

    public static Comparator<Book> getTitleComparatorAsc(){
        return comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER);
    }

    public static Comparator<Book> getTitleComparatorDsc(){
        return getTitleComparatorAsc().reversed();
    }
}

