package com.example.anthony.androidca;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends ArrayAdapter<String> {

    private List<String> items;
    int resource;

    public MyAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(resource, null);
        final String isbn = items.get(position);
        if (isbn != null) {
           final TextView titleView = (TextView) v.findViewById(R.id.titleView);
           final TextView authorView=(TextView) v.findViewById(R.id.authorView);
           final TextView priceView=(TextView) v.findViewById(R.id.priceView);
            new AsyncTask<Void, Void,BookModel>() {
                @Override
                protected BookModel doInBackground(Void... params) {
                  return BookModel.getBook(isbn);

                }
                @Override
                protected void onPostExecute(BookModel result) {
                    titleView.setText(result.get("title"));
                    authorView.setText(result.get("authorName"));
                    priceView.setText(result.get("price"));
                }
            }.execute();

            final ImageView image = (ImageView) v.findViewById(R.id.bookIcon);
            new AsyncTask<Void, Void,Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
               return BookModel.getPhoto(false, isbn);

            }
            @Override
            protected void onPostExecute(Bitmap result) {
                image.setImageBitmap(result);
                }
        }.execute();

        }
        return v;
    }
}