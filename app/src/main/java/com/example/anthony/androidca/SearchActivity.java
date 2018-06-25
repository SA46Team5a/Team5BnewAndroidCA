package com.example.anthony.androidca;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity {

    EditText searchText;
    Button searchAllBtn;
    Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchText = findViewById(R.id.search_txt);
        searchBtn = findViewById(R.id.btn_search);
        searchAllBtn = findViewById(R.id.btn_search_all);
        final Toast t=Toast.makeText(SearchActivity.this,"Sorry, the book you searched is not in our Store",Toast.LENGTH_LONG);

        //final Context ctx = getApplicationContext();

        searchBtn.setOnClickListener( new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                final String searchCriteria = getSearchText();

                new AsyncTask<String,Void,List<String>>(){

                    @Override
                    protected List<String> doInBackground(String... strings) {
                        if(searchCriteria==null || searchCriteria.equals(" ")){
                            return BookModel.list();

                        }
                        else{
                            if(BookModel.searchBookByTitle(searchCriteria).isEmpty())
                            {
                                t.show();
                            }
                            return BookModel.searchBookByTitle(searchCriteria);
                        }
                    }

                    @Override
                    protected void onPostExecute(List<String> result) {
                        if(!result.isEmpty()) {
                            Intent i = new Intent(SearchActivity.this, ListOfBooksActivity.class);
                            i.putExtra("listOfBooks", (ArrayList<String>) result);
                            startActivity(i);
                        }
                    }
                }.execute();

            }
        });

        searchAllBtn.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                new AsyncTask<String,Void,List<String>>(){
                    @Override
                    protected List<String> doInBackground(String... strings) {
                            return BookModel.list();
                    }

                    @Override
                    protected void onPostExecute(List<String> result) {
                        Intent i  = new Intent(SearchActivity.this,ListOfBooksActivity.class);
                        i.putExtra("listOfBooks",(ArrayList<String>)result);
                        startActivity(i);
                    }
                }.execute();

            }
        });
    }


    public String getSearchText(){
        return searchText.getText().toString();
    }
}
