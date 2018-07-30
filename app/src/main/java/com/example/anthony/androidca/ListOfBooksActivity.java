package com.example.anthony.androidca;

import android.app.ListActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListOfBooksActivity extends ListActivity {
Button B1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        final List<String> ISBNs=(List<String>)i.getSerializableExtra("listOfBooks");


        MyAdapter adapter =new MyAdapter(ListOfBooksActivity.this,R.layout.row,ISBNs);
        setListAdapter(adapter);
         B1=findViewById(R.id.button);
         B1.setOnClickListener(view)
        {

        }
    }



    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        final String item = (String) getListAdapter().getItem(position);
        Intent intent=new Intent(this,BookDetailActivity.class);
        Toast t= Toast.makeText(this,"Book is selected",Toast.LENGTH_LONG);
        t.show();
        intent.putExtra("ISBN",item);
        startActivity(intent);

    }
}

