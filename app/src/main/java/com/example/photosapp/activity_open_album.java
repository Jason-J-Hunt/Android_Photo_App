package com.example.photosapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class activity_open_album extends AppCompatActivity {
    PhotoThumbnail_Adapter arrayAdapter;
    public ListView photos_lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_album);

        photos_lv=(ListView) findViewById(R.id.photos_lv);
        arrayAdapter = new PhotoThumbnail_Adapter(this, MainActivity.current_Album.photos);
        photos_lv.setAdapter(arrayAdapter);

        Spinner spinner = (Spinner) findViewById(R.id.albums_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<Album> adapter = new ArrayAdapter<Album>(this, android.R.layout.simple_spinner_item, MainActivity.user_albums);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }

    public void addphoto(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);

    }

    public void deletephoto(View view){
        if (photos_lv.getCheckedItemPosition() >= MainActivity.current_Album.photos.size() || photos_lv.getCheckedItemPosition() == -1) {
            return;
        }

        System.out.println(photos_lv.getCheckedItemPosition());
        Photo phototo_delete = (Photo) photos_lv.getAdapter().getItem(photos_lv.getCheckedItemPosition());
        System.out.println(phototo_delete);
        arrayAdapter.remove(phototo_delete);

        photos_lv.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    public void display_photo(View view){
        if (photos_lv.getCheckedItemPosition() >= MainActivity.current_Album.photos.size() || photos_lv.getCheckedItemPosition() == -1) {
            return;
        }

        MainActivity.current_Photo=(Photo) photos_lv.getAdapter().getItem(photos_lv.getCheckedItemPosition());
        startActivity(new Intent(activity_open_album.this, DisplayPhoto.class));
    }

    public void movephoto(View view){
        Spinner spinner = (Spinner) findViewById(R.id.albums_spinner);

        if (photos_lv.getCheckedItemPosition() >= MainActivity.current_Album.photos.size() || photos_lv.getCheckedItemPosition() == -1) {
            return;
        }

        Photo phototo_move=(Photo) photos_lv.getAdapter().getItem(photos_lv.getCheckedItemPosition());

        Album albumto=(Album)spinner.getSelectedItem();
        if(albumto==null){
            return;
        }

        if(albumto.equals(MainActivity.current_Album)){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Transfer Failed");
            alertDialog.setMessage("Photo can not be moved to album already in.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }

        albumto.photos.add(phototo_move);

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Transfer Complete");
        alertDialog.setMessage("Photo was moved succesfully");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }

    // To handle when an image is selected from the browser, add the following to your Activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                try{
                    // currImageURI is the global variable I'm using to hold the content:// URI of the image
                    Uri currImageURI = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), currImageURI);

                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
                    byte [] b=baos.toByteArray();
                    String stringbitmap= Base64.encodeToString(b, Base64.DEFAULT);

                    Photo phototoadd=new Photo(MainActivity.current_Album, currImageURI.toString(), stringbitmap);
                    if(MainActivity.current_Album.photos.contains(phototoadd)){
                        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                        alertDialog.setTitle("Add Photo Error");
                        alertDialog.setMessage("Can not add duplicate photo to the same album.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        return;
                    }
                    MainActivity.current_Album.photos.add(phototoadd);
                    photos_lv.setAdapter(arrayAdapter);
                    System.out.println(currImageURI.getPath());
                    System.out.println(currImageURI.toString());
                }
                catch(Exception e){

                }

            }
        }
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
