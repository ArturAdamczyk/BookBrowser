package com.art.bookbrowser.dialogs;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.art.bookbrowser.R;
import com.art.bookbrowser.models.Book;
import com.art.bookbrowser.params.Params;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddBookDialog {
    private final Context ctx;
    private Callback callback;
    private ViewHolder holder;
    private View view;
    private AlertDialog dialog;

    public interface Callback{
        void onAddNote(Book book);
    }

    public AddBookDialog(Callback callback, Context ctx){
        this.ctx = ctx;
        this.callback = callback;
        initView();
    }

    private void initView(){
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view  = inflater.inflate(R.layout.dialog_add_book, null);
        holder = new AddBookDialog.BookAddViewHolder(view);
    }

    public void showDialog(boolean cancelable) {
        initLabel();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
        alertDialog.setView(view);
        alertDialog.setCancelable(cancelable);
        dialog = alertDialog.show();
    }

    public void close(){
        if(dialog != null){
            if(dialog.isShowing()){
                dialog.cancel();
            }
        }
    }

    public void passErrorMessage(String msg){
        ((BookAddViewHolder) holder).showErrorMessage(msg);
    }

    private void initLabel(){
        ((BookAddViewHolder) holder).initUI();
    }

    public class ViewHolder{
        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    public class BookAddViewHolder extends ViewHolder{
        @BindView(R.id.dialogAddBookEditTextTitle)
        EditText title;
        @BindView(R.id.dialogAddBookEditTextDescription)
        EditText description;
        @BindView(R.id.dialogAddBookEditTextCoverUrl)
        EditText coverUrl;
        @BindView(R.id.dialogAddBookTextViewErrorLabel)
        TextView errorLabel;
        @BindView(R.id.dialogAddBookButtonConfirm)
        Button buttonConfirm;
        @BindView(R.id.dialogAddBookButtonDiscard)
        Button buttonDiscard;

        public BookAddViewHolder(View view){ super(view); }

        public void initUI(){
            initHolderView();
        }

        public void showErrorMessage(String msg){
            errorLabel.setText(msg);
        }

        @OnClick({R.id.dialogAddBookButtonConfirm, R.id.dialogAddBookButtonDiscard})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.dialogAddBookButtonConfirm:
                    if(!title.getText().toString().isEmpty()
                            && !description.getText().toString().isEmpty()){
                        clearErrorLabel();
                        callback.onAddNote(createNewBook());
                    }else{
                        if(title.getText().toString().isEmpty()
                                && description.getText().toString().isEmpty()){
                            errorLabel.setText(ctx.getResources().getString(R.string.dialog_add_book_fields_cannot_be_empty));
                        }else{
                            if(title.getText().toString().isEmpty()){
                                errorLabel.setText(ctx.getResources().getString(R.string.dialog_add_book_field_title_cannot_be_empty));
                            }else{
                                errorLabel.setText(ctx.getResources().getString(R.string.dialog_add_book_field_description_cannot_be_empty));
                            }
                        }
                    }
                    break;
                case R.id.dialogAddBookButtonDiscard:
                    dialog.dismiss();
                    break;
            }
        }

        private void initHolderView(){
            title.setOnTouchListener((view, motionEvent) -> {
                clearErrorLabel();
                return false;
            });
            description.setOnTouchListener((view, motionEvent) -> {
                clearErrorLabel();
                return false;
            });
            coverUrl.setOnTouchListener((view, motionEvent) -> {
                clearErrorLabel();
                return false;
            });
        }

        private void clearErrorLabel(){
            errorLabel.setText(Params.EMPTY_VALUE);
        }

        private Book createNewBook(){
            Book book = new Book();
            book.setTitle(title.getText().toString());
            book.setDescription(description.getText().toString());
            book.setCoverUrl(coverUrl.getText().toString());
            return book;
        }
    }
}