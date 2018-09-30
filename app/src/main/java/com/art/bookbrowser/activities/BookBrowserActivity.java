package com.art.bookbrowser.activities;

import android.arch.lifecycle.ViewModelProviders;
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
import com.art.bookbrowser.models.Book;
import com.art.bookbrowser.models.InternetConnectionEvent;
import com.art.bookbrowser.params.Params;
import com.art.bookbrowser.viewmodels.BookBrowserViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Optional;

import butterknife.BindView;
import butterknife.OnClick;

public class BookBrowserActivity extends BaseActivity<BookBrowserViewModel> {

    @BindView(R.id.bookBrowserTextViewErrorLabel)
    TextView bookBrowserTextViewErrorLabel;
    @BindView(R.id.bookBrowserRecyclerView)
    RecyclerView bookBrowserRecyclerView;
    @BindView(R.id.bookBrowserFab)
    FloatingActionButton bookBrowserFab;
    @BindView(R.id.bookBrowserParentLayout)
    CoordinatorLayout bookBrowserParentLayout;

    private BookListAdapter bookListAdapter;
    private AddBookDialog addBookDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_book_browser);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initUI();
        viewModel.getData();
    }

    @OnClick(R.id.bookBrowserFab)
    public void onViewClicked() {
        openAddBookDialog();
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
                viewModel.deleteBook(book);
            }
        });
        bookBrowserRecyclerView.setAdapter(bookListAdapter);
    }

    private void refreshUI(List<Book> bookList){
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
        addBookDialog = new AddBookDialog(book -> viewModel.addBook(book), this);
        addBookDialog.showDialog(false);
    }



    private void goToBookDetailsActivity(Book book) {
        intentManager.getIntentExtras().putString(Params.BOOK_ID, book.getId());
        openActivity(BookDetailsActivity.class);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInternetConnectionChange(InternetConnectionEvent event) {
        if(event.isInternetAvailable()){
            Optional.ofNullable(bookListAdapter).ifPresent(bookListAdapter -> {
                if(bookListAdapter.getBookList().isEmpty()){
                    viewModel.getData();
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

    @Override
    public void initViewModel(){
        viewModel = ViewModelProviders
                .of(this, baseViewModelFactory)
                .get(BookBrowserViewModel.class);
        viewModel.getBooks().observe(this, books -> refreshUI(books));
        viewModel.getBook().observe(this, book -> closeDialog());
        viewModel.getDeleteBook().observe(this, book -> bookListAdapter.deleteBook(book));
        viewModel.getPassMessage().observe(this, msg -> {
            if(msg.equals(getString(R.string.add_book_failure))){
                Optional.ofNullable(addBookDialog)
                        .ifPresent(addBookDialog1 ->
                                addBookDialog.passErrorMessage(
                                        getResources().getString(R.string.add_book_failure)));
            }
        });
    }
}
