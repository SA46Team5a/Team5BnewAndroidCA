package com.example.anthony.androidca;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookModel extends HashMap<String,String>  {


    final static String baseURL = "http://172.17.118.1/BookStore/Endpoint/IBookService.svc/";

    public BookModel(String ISBN,String title, String authorName, String categoryName, String price,String discountedPrice,String stockLevel,String synopsis) {

        put("ISBN", ISBN);
        put("title", title);
        put("authorName", authorName);
        put("categoryName", categoryName);
        put("price",price);
        put("discountedPrice",discountedPrice);
        put("stockLevel",stockLevel);
        put("synopsis",synopsis);
    }




    public static List<String> list() {
        List<String> list = new ArrayList<String>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(baseURL + "Books");
        try {
            for (int i =0; i<a.length(); i++){
                JSONObject b = a.getJSONObject(i);
                list.add(b.get("ISBN").toString());
            }

        } catch (Exception e) {
            Log.e("BookModel.list()", "JSONArray error");
        }
        return(list);
    }

    public static BookModel getBook(String ISBN) {
        JSONObject b = JSONParser.getJSONFromUrl(baseURL  + "Books/" + ISBN );
        try {
            return new BookModel(b.getString("ISBN"),b.getString("Title"),b.getString("AuthorName"),b.getString("CategoryName"),b.getString("Price"),b.getString("DiscountedPrice"),b.getString("StockLevel"),b.getString("Synopsis"));
        } catch (Exception e) {
            Log.e("BookModel.getBook()", "JSONArray error");
        }
        return(null);
    }

    public static List<String> searchBookByTitle(String searchCriteria){
        List<String> allBooksISBN = list();
        List<String> searchResult = new ArrayList<String>();
        for (String isbn: allBooksISBN) {
            BookModel book = getBook(isbn);
            if((book.get("title").toString().toLowerCase()).contains(searchCriteria.toLowerCase())){
                searchResult.add(book.get("ISBN").toString());
            }
        }
        return searchResult;
    }
    final static String imageURL = "http://172.17.118.1/BookStore/Resources/BookCovers";
    public static Bitmap getPhoto(boolean thumbnail, String isbn) {
        try {
            URL url = (thumbnail ? new URL(String.format("%s/%s-s.jpg",imageURL, isbn)) :
                    new URL(String.format("%s/%s.jpg",imageURL, isbn)));

            URLConnection conn = url.openConnection();
            InputStream ins = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(ins);
            ins.close();
            return bitmap;
        } catch (Exception e) {
            Log.e("BookModel.getPhoto()", "Bitmap error");
        }
        return(null);
    }
}

