package com.example.anthony.androidca;

import android.app.ListActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListOfBooksActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent i=getIntent();

        final List<String> ISBNs=(List<String>)i.getSerializableExtra("listOfBooks");

        MyAdapter adapter =new MyAdapter(ListOfBooksActivity.this,R.layout.row,ISBNs);
        setListAdapter(adapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v,
                                   int position, long id) {
        final String item = (String) getListAdapter().getItem(position);
        Intent intent=new Intent(this,BookDetailActivity.class);
        intent.putExtra("ISBN",item);
        startActivity(intent);

//        new AsyncTask<Void, Void,String>() {
//            @Override
//            protected String doInBackground(Void... params) {
//                String selectedBook=BookModel.searchBookByTitle(item).get(0);
//                return selectedBook;
//            }
//            @Override
//            protected void onPostExecute(String result) {
//                Intent intent = new Intent(ListOfBooksActivity.this, BookDetailActivity.class);
//                intent.putExtra("ISBN", result );
//                startActivity(intent);
//            }
//        }.execute();

    }
}

