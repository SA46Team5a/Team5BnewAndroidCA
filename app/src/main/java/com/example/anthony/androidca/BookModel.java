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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class BookModel extends HashMap<String,String>  {

    final static String baseURL = "http://172.17.118.1/BookStore/Endpoint/IBookService.svc/";
    final static String imageURL = "http://172.17.118.1/BookStore/Resources/BookCovers";

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

        final List<String> searchWords = getIndividualSearchWords(searchCriteria);
        List<BookModel> unsortedSearchResults = new ArrayList<BookModel>();
        for (String isbn: allBooksISBN) {
            BookModel book = getBook(isbn);
            if(BookModel.hasMatchingString(book, searchCriteria)){
                searchResult.add(book.get("ISBN").toString());
            }
            else{
                if(BookModel.hasMatchingWords(book,searchWords)>0 && !resultsContain(searchResult,book)){
                    book.put("match",Integer.toString(BookModel.hasMatchingWords(book,searchWords)));
                    unsortedSearchResults.add(book);
                }
            }
        }
        if(searchResult.size()==0){
            Collections.sort(unsortedSearchResults, new SearchComparator());
            for (BookModel book: unsortedSearchResults) {
                searchResult.add(book.get("ISBN"));
            }
        }
        return searchResult;
    }

    private static boolean hasMatchingString(BookModel book, String s) {
        return (book.get("title").toString().toLowerCase()).contains(s.trim().toLowerCase());
    }

    private static int hasMatchingWords(BookModel book,List<String> searchWords){
        int noOfMatches = 0;
        for(String words : searchWords){
            if(hasMatchingString(book, words) ){
                noOfMatches++;
            }
        }
        return noOfMatches;
    }

    public static boolean resultsContain(final List<String> searchResultsWithISBN, final BookModel book){
        for(String ISBN: searchResultsWithISBN){
            BookModel bookInResult = getBook(ISBN);
            if(bookInResult.get("title").equals(book.get("title")))
                return true;
        }
        return false;
    }

    private static List<String> getIndividualSearchWords(String searchCriteria) {
        List<String> searchWords = new ArrayList<String>(Arrays.asList(searchCriteria.toLowerCase().split("\\s+|[,.-<>]")));
        List<String> omittedWords = Arrays.asList("the","and","a","of","to");

        Iterator<String> i = searchWords.iterator();
        while(i.hasNext()){
            String s = i.next();
            for (String omittedWord:omittedWords) {
                if(s.equals(omittedWord))
                    i.remove();
            }
        }
        return searchWords;
    }

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

    public static class SearchComparator implements Comparator<BookModel>{
        @Override
        public int compare(BookModel b1, BookModel b2) {
            int b1matches = Integer.parseInt(b1.get("match"));
            int b2matches = Integer.parseInt(b2.get("match"));

            if(b1matches==b2matches)
                return 0;
            else if(b1matches<b2matches)
                return 1;
            else
                return -1;
        }
    }
}

