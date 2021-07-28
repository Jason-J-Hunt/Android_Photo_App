package com.example.photosapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class PhotoThumbnail_Adapter extends ArrayAdapter<Photo> {

    private Context pContext;
    private List<Photo> photosList = new ArrayList<>();

    public PhotoThumbnail_Adapter(Context context, ArrayList<Photo> list) {
        super(context, 0 , list);
        pContext = context;
        photosList = list;
    }

    @NonNull
    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(pContext).inflate(R.layout.album_photos_listitem,parent,false);

        Photo currentPhoto = photosList.get(position);
        try{
            ImageView image = (ImageView)listItem.findViewById(R.id.photo_thumbnail);
            image.setImageBitmap(currentPhoto.getImagebitmap());
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return listItem;
    }
}
