package com.example.photosapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class DisplayPhoto extends AppCompatActivity {
    public ListView tags_lv;
    PhotoTags_Adapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_photo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        loadphoto_data(MainActivity.current_Photo);
    }

    public void loadphoto_data(Photo photo){
        ImageView imagetoshow=(ImageView) findViewById(R.id.photo_imgview);
        imagetoshow.setImageBitmap(photo.getImagebitmap());

        tags_lv=(ListView) findViewById(R.id.tags_lv);
        arrayAdapter = new PhotoTags_Adapter(this, MainActivity.current_Photo.tags);
        tags_lv.setAdapter(arrayAdapter);
    }

    public void add_tag(View view){
        TextView persontag_field = (TextView) findViewById(R.id.persontag_field);
        String persontag_toadd=persontag_field.getText().toString();

        if(!persontag_toadd.contentEquals("")){
            TagPair persontag=new TagPair("Person", persontag_toadd);
            arrayAdapter.add(persontag);

            tags_lv.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();

            persontag_field.setText("");
        }

        TextView locationtag_field = (TextView) findViewById(R.id.locationtag_field);
        String locationtag_toadd=locationtag_field.getText().toString();

        if(!locationtag_toadd.contentEquals("")){
            TagPair locationtag=new TagPair("Location", locationtag_toadd);
            arrayAdapter.add(locationtag);

            tags_lv.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();

            locationtag_field.setText("");
        }
    }

    public void delete_tag(View view){
        if (tags_lv.getCheckedItemPosition() >= MainActivity.current_Photo.tags.size() || tags_lv.getCheckedItemPosition() == -1) {
            return;
        }

        TagPair tag_todelete = (TagPair) tags_lv.getAdapter().getItem(tags_lv.getCheckedItemPosition());
        arrayAdapter.remove(tag_todelete);

        tags_lv.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    public void prev_photo(View view){
        if(MainActivity.current_Album.photos.size()==1){
            return;
        }
        int currentphoto_ind=MainActivity.current_Album.photos.indexOf(MainActivity.current_Photo);
        int prevphoto_ind=-1;
        if(currentphoto_ind==0){
            prevphoto_ind=MainActivity.current_Album.photos.size()-1;
        }
        else{
            prevphoto_ind=currentphoto_ind-1;
        }
        MainActivity.current_Photo=MainActivity.current_Album.photos.get(prevphoto_ind);
        loadphoto_data(MainActivity.current_Photo);
    }

    public void next_photo(View view){
        if(MainActivity.current_Album.photos.size()==1){
            return;
        }
        int currentphoto_ind=MainActivity.current_Album.photos.indexOf(MainActivity.current_Photo);
        int nextphoto_ind=-1;

        if(currentphoto_ind==MainActivity.current_Album.photos.size()-1){
            nextphoto_ind=0;
        }
        else{
            nextphoto_ind=currentphoto_ind+1;
        }
        MainActivity.current_Photo=MainActivity.current_Album.photos.get(nextphoto_ind);
        loadphoto_data(MainActivity.current_Photo);
    }

    public void save_state() {
        System.out.println("Running save!!!!");
        try {
            FileOutputStream fileOut = this.openFileOutput("albums.ser", Context.MODE_PRIVATE); //file to output byte code to
            ObjectOutputStream out = new ObjectOutputStream(fileOut); //actual stream of byte code and where to write it to
            out.writeObject(MainActivity.user_albums);
            System.out.println(MainActivity.user_albums.toString());
            out.close();
            fileOut.close();
        } catch (IOException i) {
            System.out.print(i);
        }
    }

    public void onStop(){
        save_state();
        super.onStop();
    }

}
