package com.example.photosapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class Search_Photos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__photos);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    public void searchforPhotos(View view){
        ListView searchphotos_lv = (ListView) findViewById(R.id.searchphotos_lv);
        TextView persontag_field= (TextView) findViewById(R.id.persontag_field);
        TextView locationtag_field= (TextView) findViewById(R.id.locationtag_field);
        RadioButton and_radio= (RadioButton) findViewById(R.id.and_radio);
        RadioButton or_radio= (RadioButton) findViewById(R.id.or_radio);

        ArrayList<Photo> searchresults=new ArrayList<Photo>();
        PhotoThumbnail_Adapter arrayAdapter;

        if(persontag_field.getText().toString().contentEquals("") && locationtag_field.getText().toString().contentEquals("")){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Search Error");
            alertDialog.setMessage("Can not search on empty string");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }

        else if(!persontag_field.getText().toString().contentEquals("") && locationtag_field.getText().toString().contentEquals("")){
            TagPair persontagPair=new TagPair("Person", persontag_field.getText().toString());
            for(Album albumsearching:MainActivity.user_albums) {
                for(Photo photosearching:albumsearching.photos) {
                    for(TagPair tagsearching: photosearching.tags) {
                        if(tagsearching.equals(persontagPair)) {
                            if(!searchresults.contains(photosearching)) {
                                searchresults.add(photosearching);
                            }
                        }
                    }
                }
            }
        }

        else if(persontag_field.getText().toString().contentEquals("") && !locationtag_field.getText().toString().contentEquals("")){
            TagPair locationtagPair=new TagPair("Location", locationtag_field.getText().toString());
            for(Album albumsearching:MainActivity.user_albums) {
                for(Photo photosearching:albumsearching.photos) {
                    for(TagPair tagsearching: photosearching.tags) {
                        if(tagsearching.equals(locationtagPair)) {
                            if(!searchresults.contains(photosearching)) {
                                searchresults.add(photosearching);
                            }
                        }
                    }
                }
            }
        }

        else if(!persontag_field.getText().toString().contentEquals("") && !locationtag_field.getText().toString().contentEquals("")){
            if(and_radio.isChecked()==false && or_radio.isChecked()==false){
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Search Error");
                alertDialog.setMessage("When searching on both Person tag and Location tag please search using and/or.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return;
            }
            else if(and_radio.isChecked()){
                TagPair persontagPair=new TagPair("Person", persontag_field.getText().toString());
                TagPair locationtagPair=new TagPair("Location", locationtag_field.getText().toString());
                for(Album albumsearching:MainActivity.user_albums) {
                    for(Photo photosearching:albumsearching.photos) {
                        for(TagPair tagsearching: photosearching.tags) {
                            if(tagsearching.equals(persontagPair)) {
                                for(TagPair tagsearching2: photosearching.tags) {
                                    if(tagsearching2.equals(locationtagPair)) {
                                        if(!searchresults.contains(photosearching)) {
                                            searchresults.add(photosearching);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else if(or_radio.isChecked()){
                TagPair persontagPair=new TagPair("Person", persontag_field.getText().toString());
                TagPair locationtagPair=new TagPair("Location", locationtag_field.getText().toString());
                for(Album albumsearching:MainActivity.user_albums) {
                    for(Photo photosearching:albumsearching.photos) {
                        for(TagPair tagsearching: photosearching.tags) {
                            if(tagsearching.equals(persontagPair) || tagsearching.equals(locationtagPair)) {
                                if(!searchresults.contains(photosearching)) {
                                    searchresults.add(photosearching);
                                }
                            }
                        }
                    }
                }
            }
        }

        arrayAdapter = new PhotoThumbnail_Adapter(this, searchresults);
        searchphotos_lv.setAdapter(arrayAdapter);

    }

}
