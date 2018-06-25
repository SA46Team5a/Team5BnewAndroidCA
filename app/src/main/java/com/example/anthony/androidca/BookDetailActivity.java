package com.example.anthony.androidca;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import java.text.DecimalFormat;

public class BookDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        final Intent incomingIntent = getIntent();
        final String ISBN = incomingIntent.getStringExtra("ISBN");

        new AsyncTask<Void, Void,BookModel>() {
            @Override
            protected BookModel doInBackground(Void... params) {
                return BookModel.getBook(ISBN);
            }
            @Override
            protected void onPostExecute(BookModel result) {
                show(result);
            }
        }.execute();

    }
    void show(BookModel book)
    {
        final BookModel Book=book;
        int[] ids={R.id.ISBN,R.id.title,R.id.authorName,R.id.categoryName,R.id.price,R.id.discountedPrice,R.id.stockLevel,R.id.synopsis};
        String[] keys={"ISBN","title", "authorName","categoryName","price","discountedPrice","stockLevel","synopsis"};

        for(int i=0;i<keys.length;i++)
        {
            EditText e=(EditText) findViewById(ids[i]);
            String text = book.get(keys[i]);
            text = text.trim().replace("\n", "").replace("\r", "").replaceAll("\\s+"," ");
            if(i==4||i==5){
                DecimalFormat df = new DecimalFormat("$###,###.00");
                text = df.format(Double.parseDouble(text));
            }
            e.setText(text);
        }
        final ImageView image=(ImageView)findViewById(R.id.bookImage);
        new AsyncTask<Void, Void,Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return BookModel.getPhoto(false,Book.get("ISBN"));
            }
            @Override
            protected void onPostExecute(Bitmap result) {
                image.setImageBitmap(result);
            }
        }.execute();


    }
}

