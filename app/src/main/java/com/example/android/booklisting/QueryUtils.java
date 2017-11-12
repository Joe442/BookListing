package com.example.android.booklisting;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static android.os.Build.VERSION_CODES.M;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;
import static java.security.AccessController.getContext;

/**
 * Created by Johannes on 05.11.2017.
 * This class was created to make a HTTP Request and read the JSON Response from the
 * HTTP Request
 */

public class QueryUtils {

    private static  final String LOG_TAG = QueryUtils.class.getSimpleName();


    private QueryUtils(){
    }


    //This Method creates the Url, makes an Http Request and parses the JsonResponse
    public static ArrayList<Book> fetchBookData (String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG,"Error closing Input Stream",e);
        }

        ArrayList<Book> books = extractBooks(jsonResponse);

        return books;


    }

    //This method creates the Url using the requestUrl which was passed in by method fetchBookData
    private static URL createUrl (String requestUrl){
        URL url = null;

        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error while creating Url",e);
        }

        return url;
    }

    //This method makes the Http Request using the created URL. It returns the Answer as a String
    private static String makeHttpRequest (URL url) throws IOException {
        String jsonRespnse = "";

        if (url == null){
            return jsonRespnse;
        }

        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000);
            urlConnection.setConnectTimeout(1500);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonRespnse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG,"Error Response Code" + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }

        return jsonRespnse;
    }

    //This method takes the input stream as input and makes a String out of the Stream using String Builder
    private static String readFromStream (InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null){
                output.append(line);
                line = reader.readLine();
            }

        }
        return output.toString();

    }


    // This method reads from the Json Response and returns an Array List with all the Books the request contained
    private static ArrayList<Book> extractBooks (String jsonResponse){
        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        ArrayList<Book> books = new ArrayList<Book>();



        try {
            JSONObject response = new JSONObject(jsonResponse);


            JSONArray items = response.getJSONArray("items");
            for (int i = 0; i<items.length(); i++){
                JSONObject volume = items.getJSONObject(i);
                JSONObject volumeInfo = volume.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                String author = "";
                if (volumeInfo.has("authors")) {
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    author = authors.getString(0);
                }else{
                    author = "No author";
                }

                String description;
                if (volumeInfo.has("description")){
                    description = volumeInfo.getString("description");
                }else{
                    description = "No description";
                }

                String imageLink;
                if(volumeInfo.has("imageLinks")){
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                    imageLink = imageLinks.getString("smallThumbnail");
                }else{
                    imageLink = "NoImage";
                }

                Book currentBook = new Book(title,author,description,imageLink);
                books.add(currentBook);


            }


        } catch (JSONException e) {
            Log.e(LOG_TAG,"Problem with JsonResult", e);
        }

        return books;

    }





}
