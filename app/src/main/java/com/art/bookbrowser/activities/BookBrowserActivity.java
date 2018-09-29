package com.art.bookbrowser.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.art.bookbrowser.App;
import com.art.bookbrowser.R;
import com.art.bookbrowser.adapters.BookListAdapter;
import com.art.bookbrowser.base.BaseActivity;
import com.art.bookbrowser.di.ActivityModule;
import com.art.bookbrowser.dialogs.AddBookDialog;
import com.art.bookbrowser.interfaces.Repository;
import com.art.bookbrowser.models.Book;
import com.art.bookbrowser.models.InternetConnectionEvent;
import com.art.bookbrowser.params.Params;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Optional;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BookBrowserActivity extends BaseActivity {

    @BindView(R.id.bookBrowserTextViewErrorLabel)
    TextView bookBrowserTextViewErrorLabel;
    @BindView(R.id.bookBrowserRecyclerView)
    RecyclerView bookBrowserRecyclerView;
    @BindView(R.id.bookBrowserFab)
    FloatingActionButton bookBrowserFab;
    @BindView(R.id.bookBrowserParentLayout)
    CoordinatorLayout bookBrowserParentLayout;

    @Inject
    protected Repository repository;
    private BookListAdapter bookListAdapter;
    private AddBookDialog addBookDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_browser);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        initUI();
        getData();
    }

    @Override
    protected void onStop(){
        super.onStop();
        disposables.clear();
    }

    @OnClick(R.id.bookBrowserFab)
    public void onViewClicked() {
        openAddBookDialog();
    }

    private void getData(){
        disposables.add(repository.getBooks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        passMessage(getResources().getString(R.string.books_fetch_success),
                                getResources().getString(R.string.books_fetch_success));
                        refreshUI((ArrayList) response);
                    }, throwable -> passMessage(throwable.getLocalizedMessage(),
                                getResources().getString(R.string.books_fetch_failure))
                )
        );
    }

    private void initUI(){
        LinearLayoutManager linearManager = new LinearLayoutManager(this);
        bookBrowserRecyclerView.setLayoutManager(linearManager);
        bookBrowserRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        bookListAdapter = new BookListAdapter(new BookListAdapter.ClickListenerCallback() {
            @Override
            public void onItemClick(Book book, View view) {
                goToBookDetailsActivity(book);
            }

            @Override
            public void onItemLongClick(Book book, View view) {
                deleteBook(book);
            }
        });
        bookBrowserRecyclerView.setAdapter(bookListAdapter);
    }

    private void refreshUI(ArrayList<Book> bookList){
        if(bookList.isEmpty()){
            bookBrowserTextViewErrorLabel.setVisibility(View.VISIBLE);
        }else{
            bookBrowserTextViewErrorLabel.setVisibility(View.INVISIBLE);
            bookListAdapter.setData(bookList);
        }
    }

    private void closeDialog(){
        Optional.ofNullable(addBookDialog)
                .ifPresent(addBookDialog -> addBookDialog.close());
    }

    private void openAddBookDialog() {
        addBookDialog = new AddBookDialog(book -> addBook(book), this);
        addBookDialog.showDialog(false);
    }

    private void addBook(Book book){
        disposables.add(repository.addBook(book)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        passMessage(getResources().getString(R.string.add_book_success),
                                getResources().getString(R.string.add_book_success));
                        closeDialog();
                        getData();
                    }, throwable -> {
                        Optional.ofNullable(addBookDialog)
                                .ifPresent(addBookDialog1 -> addBookDialog.passErrorMessage(getResources().getString(R.string.add_book_failure)));
                        passMessage(throwable.getLocalizedMessage(),
                                getResources().getString(R.string.add_book_failure));
                    }));
    }

    private void deleteBook(Book book){
        disposables.add(repository.deleteBook(book.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        passMessage(getResources().getString(R.string.delete_book_success),
                                getResources().getString(R.string.delete_book_success));
                        bookListAdapter.deleteBook(book);
                    }, throwable ->
                        passMessage(throwable.getLocalizedMessage(),
                                getResources().getString(R.string.delete_book_failure))
                    ));
    }

    private void goToBookDetailsActivity(Book book) {
        intentManager.getIntentExtras().putString(Params.BOOK_ID, book.getId());
        goToNextActivity(BookDetailsActivity.class);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInternetConnectionChange(InternetConnectionEvent event) {
        if(event.isInternetAvailable()){
            Optional.ofNullable(bookListAdapter).ifPresent(bookListAdapter -> {
                if(bookListAdapter.getBookList().isEmpty()){
                    getData();
                }
            });
        }
    }

    @Override
    public void injectDependencies(){
        App.appComponent
                .getActivityComponent(new ActivityModule(this))
                .inject(this);
    }
}
