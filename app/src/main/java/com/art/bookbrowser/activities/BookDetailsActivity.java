package com.art.bookbrowser.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.art.bookbrowser.App;
import com.art.bookbrowser.R;
import com.art.bookbrowser.base.BaseActivity;
import com.art.bookbrowser.di.ActivityModule;
import com.art.bookbrowser.params.Params;
import com.art.bookbrowser.viewmodels.BookDetailsViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ceylonlabs.imageviewpopup.ImagePopup;

import java.util.Optional;

import butterknife.BindView;
import butterknife.OnClick;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class BookDetailsActivity extends BaseActivity<BookDetailsViewModel> {

    @BindView(R.id.bookDetailsImageView)
    ImageView bookDetailsImageView;
    @BindView(R.id.bookDetailsTextViewTitle)
    TextView bookDetailsTextViewTitle;
    @BindView(R.id.bookDetailsLinearLayoutLine)
    LinearLayout bookDetailsLinearLayoutLine;
    @BindView(R.id.bookDetailsTextViewDescription)
    TextView bookDetailsTextViewDescription;
    @BindView(R.id.bookDetailsTextViewErrorLabel)
    TextView bookDetailsTextViewErrorLabel;
    @BindView(R.id.bookDetailsParentLayout)
    ConstraintLayout bookDetailsParentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_book_details);
        super.onCreate(savedInstanceState);
        initViews();
        getIntentParams();
        viewModel.getData();
    }

    @OnClick(R.id.bookDetailsImageView)
    public void onViewClicked() {
        initPopUp();
    }

    private void initViews(){
        bookDetailsTextViewDescription.setMovementMethod(new ScrollingMovementMethod());
    }

    private void getIntentParams() {
        viewModel.setBookId(getIntent().getStringExtra(Params.BOOK_ID));
    }

    private void refreshUI() {
        Optional.ofNullable(viewModel.getBook().getValue()).ifPresent(book -> {
            Optional.ofNullable(book.getTitle())
                    .ifPresent(str -> bookDetailsTextViewTitle.setText(str));
            Optional.ofNullable(book.getDescription())
                    .ifPresent(str -> bookDetailsTextViewDescription.setText(str));
            Optional.ofNullable(book.getCoverUrl())
                    .ifPresent(str -> {
                        Glide.with(getApplicationContext())
                                .load(str)
                                .transition(withCrossFade())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.no_photo)
                                )
                                .into(bookDetailsImageView);
                    });
        });
    }

    private void setEmptyLayout() {
        bookDetailsImageView.setVisibility(View.INVISIBLE);
        bookDetailsTextViewDescription.setVisibility(View.INVISIBLE);
        bookDetailsTextViewTitle.setVisibility(View.INVISIBLE);
        bookDetailsLinearLayoutLine.setVisibility(View.INVISIBLE);
        bookDetailsTextViewErrorLabel.setVisibility(View.VISIBLE);
    }

    private void initPopUp() {
        if(bookDetailsImageView.getDrawable().getConstantState() !=
                ContextCompat.getDrawable(this, R.drawable.no_photo).getConstantState()){
            Optional.ofNullable(viewModel.getBook().getValue()).ifPresent(book -> {
                ImagePopup imagePopup = new ImagePopup(this);
                imagePopup.setFullScreen(true);
                imagePopup.setHideCloseIcon(true);
                imagePopup.setImageOnClickClose(false);
                imagePopup.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                imagePopup.initiatePopupWithGlide(book.getCoverUrl());
                imagePopup.viewPopup();
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
                .get(BookDetailsViewModel.class);
        viewModel.getBook().observe(this, book -> refreshUI());
        viewModel.getPassMessage().observe(this, msg -> {
            if(msg.equals(getString(R.string.book_details_fetch_failure))){
                setEmptyLayout();
            }
        });
    }

}
