package com.example.anthony.androidca;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.example.anthony.androidca.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class BookDetailActivity extends Activity {
    final static String baseURL = "http://172.17.118.1/BookStore/Endpoint/IBookService.svc/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
       // StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);
        final Intent intent1=getIntent();
        final String ISBN=intent1.getStringExtra("ISBN");
        //BookModel book=BookModel.getBook(ISBN);


        //show(book);
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
        int[] ids={R.id.editText1,R.id.editText2,R.id.editText3,R.id.editText4,R.id.editText5,R.id.editText6,R.id.editText7,R.id.editText8};
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

