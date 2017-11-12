package com.example.android.booklisting;

import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.os.Build.VERSION_CODES.M;
import static java.security.AccessController.getContext;

public class BookActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = BookActivity.class.getSimpleName();
    private String GoogleBooks_Request_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private String requestUrl = "";

    private BookAdapter mAdapter;
    private int bookIndex;
    LoaderManager loaderManager;

    private static final int BOOK_LOADER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        final ListView bookListView = (ListView) findViewById(R.id.list);
        mAdapter = new BookAdapter(this,new ArrayList<Book>());
        bookListView.setAdapter(mAdapter);
        loaderManager = getLoaderManager();


        bookListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    if (bookListView.getLastVisiblePosition() == mAdapter.getCount()-1){
                        if (bookIndex<30){
                            loadDataFromServer();
                        }else{
                            Toast.makeText(getApplicationContext(),"Result limit",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);;
        MenuItem item = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.clear();
                requestUrl = GoogleBooks_Request_URL + query;

                requestUrl = requestUrl.replaceAll("\\s","+");
                bookIndex = -10;
                loadDataFromServer();

                //Without this line the listener fires twice
                //searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }



    public void loadDataFromServer(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        bookIndex += 10;

        if (networkInfo != null && networkInfo.isConnected()){
            if (loaderManager.getLoader(BOOK_LOADER_ID)==null){
                loaderManager.initLoader(BOOK_LOADER_ID,null,this);
                Toast.makeText(getApplicationContext(),"init loader,",Toast.LENGTH_SHORT).show();

            }else{
                loaderManager.restartLoader(BOOK_LOADER_ID,null,this);
                Toast.makeText(getApplicationContext(),"restart loader,",Toast.LENGTH_SHORT).show();

            }

        }

    }



    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        String myRequestUrl = requestUrl + "&startIndex=" + bookIndex;
        return new BookLoader(this,myRequestUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        if (books != null && !books.isEmpty()){
            mAdapter.addAll(books);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }
}
