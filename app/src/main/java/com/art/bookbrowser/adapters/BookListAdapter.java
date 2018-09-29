package com.art.bookbrowser.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.art.bookbrowser.R;
import com.art.bookbrowser.models.Book;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {
    private ArrayList<Book> bookList = new ArrayList();
    private ClickListenerCallback callback;

    public interface ClickListenerCallback {
        void onItemClick(Book book, View view);
        void onItemLongClick(Book book, View view);
    }

    public BookListAdapter(ClickListenerCallback callback) {
        this.callback = callback;
    }

    public void setData(ArrayList<Book> bookList){
        this.bookList = bookList;
        sortData();
        notifyDataSetChanged();
    }

    public void addBook(Book book){
        bookList.add(book);
        notifyDataSetChanged();
    }

    public void deleteBook(Book book){
        bookList.remove(book);
        notifyDataSetChanged();
    }

    public ArrayList<Book> getBookList() {
        return bookList;
    }

    private void sortData(){
        Collections.sort(this.bookList, Book.getTitleComparatorAsc());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_list_book, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.setData(bookList.get(position));
        holder.fillItemUI();
    }

    @Override
    public int getItemCount() {
        if(!bookList.isEmpty()){
            return bookList.size();
        }else{
            return 0;
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, RecyclerView.OnLongClickListener {
        @BindView(R.id.bookRowTitle)
        TextView title;
        private Book book;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void setData(Book book){
            this.book = book;
        }

        public void fillItemUI(){
            title.setText(book.getTitle());
        }

        @Override
        public void onClick(View v) {
            callback.onItemClick(book, v);
        }

        @Override
        public boolean onLongClick(View v) {
            callback.onItemLongClick(book, v);
            return true;
        }
    }
}
