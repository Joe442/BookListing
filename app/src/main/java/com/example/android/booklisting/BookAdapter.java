package com.example.android.booklisting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Johannes on 06.11.2017.
 * This class sets the view of a single ListItem
 *
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, ArrayList<Book> books){
        super(context,0,books);
    }

    public View getView (int position, final View convertView, ViewGroup parent){
        View listitemView = convertView;

        if (listitemView == null){
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        Book book = getItem(position);

        TextView title = (TextView) listitemView.findViewById(R.id.title);
        TextView author = (TextView) listitemView.findViewById(R.id.author);
        TextView descripton = (TextView) listitemView.findViewById(R.id.content);
        ImageView image = (ImageView) listitemView.findViewById(R.id.image);

        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        descripton.setText(book.getDescription());

        String url = book.getImageLink();
        if (!url.equals("No Image")) {
            Picasso.with(getContext()).load(url).into(image);
        }




        return listitemView;




    }


}
