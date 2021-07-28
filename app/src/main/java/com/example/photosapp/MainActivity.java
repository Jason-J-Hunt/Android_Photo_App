package com.example.photosapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.R.*;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<Album> user_albums = new ArrayList<Album>();
    public static Album current_Album;
    public static Photo current_Photo;
    ArrayAdapter<Album> arrayAdapter;
    public ListView album_lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        load_saved_state();

        album_lv = (ListView) findViewById(R.id.albums_lv);
        arrayAdapter = new ArrayAdapter<Album>(this, android.R.layout.simple_list_item_1, user_albums);
        album_lv.setAdapter(arrayAdapter);

        album_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedalbum = (album_lv.getItemAtPosition(position).toString());
                TextView renamealbum_field = (TextView) findViewById(R.id.renamealbum_field);
                renamealbum_field.setText(selectedalbum);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void add_album(View view) {
        TextView albumname_field = (TextView) findViewById(R.id.albumname_field);
        String albumname_toadd = albumname_field.getText().toString();

        if(albumname_toadd.contentEquals("")){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Add Album Error");
            alertDialog.setMessage("Can not add album without an album name.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }

        System.out.println(albumname_toadd);
        Album album_toadd = new Album(albumname_toadd);
        user_albums.add(album_toadd);
        albumname_field.setText("");

        album_lv.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    public void delete_album(View view) {
        album_lv = (ListView) findViewById(R.id.albums_lv);
        if (album_lv.getCheckedItemPosition() >= user_albums.size() || album_lv.getCheckedItemPosition() == -1) {
            return;
        }
        Album album_todelete = (Album) album_lv.getAdapter().getItem(album_lv.getCheckedItemPosition());
        System.out.println("ALbum to delete: " + album_todelete);
        user_albums.remove(album_todelete);

        TextView renamealbum_field = (TextView) findViewById(R.id.renamealbum_field);
        renamealbum_field.setText("");

        album_lv.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    public void rename_album(View view){
        album_lv = (ListView) findViewById(R.id.albums_lv);
        if (album_lv.getCheckedItemPosition() >= user_albums.size() || album_lv.getCheckedItemPosition() == -1) {
            return;
        }

        TextView renamealbum_field = (TextView) findViewById(R.id.renamealbum_field);
        String renameto=renamealbum_field.getText().toString();
        Album album_torename = (Album) album_lv.getAdapter().getItem(album_lv.getCheckedItemPosition());
        album_torename.setAlbumname(renameto);
        renamealbum_field.setText("");

        album_lv.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    public void open_album(View view) {
        album_lv = (ListView) findViewById(R.id.albums_lv);

        if (album_lv.getCheckedItemPosition() >= user_albums.size() || album_lv.getCheckedItemPosition() == -1) {
            return;
        }

        current_Album=(Album) album_lv.getAdapter().getItem(album_lv.getCheckedItemPosition());
        startActivity(new Intent(MainActivity.this, activity_open_album.class));
    }

    public void search(View view){
        startActivity(new Intent(MainActivity.this, Search_Photos.class));
    }

    public void save_state() {
        System.out.println("Running save!!!!");
        try {
            FileOutputStream fileOut = this.openFileOutput("albums.ser", Context.MODE_PRIVATE); //file to output byte code to
            ObjectOutputStream out = new ObjectOutputStream(fileOut); //actual stream of byte code and where to write it to
            out.writeObject(user_albums);
            System.out.println(user_albums.toString());
            out.close();
            fileOut.close();
        } catch (IOException i) {
            System.out.print(i);
        }
    }

    public void load_saved_state() {
        try {
            FileInputStream fileIn = this.openFileInput("albums.ser"); //file to load byte code from
            ObjectInputStream in = new ObjectInputStream(fileIn); //actual stream of byte code
            user_albums = (ArrayList<Album>) in.readObject();
            System.out.println(user_albums.toString());
            in.close();
            fileIn.close();
        } catch (IOException i) {
            System.out.println(i);
            System.out.println("No data to serilize");
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("User class not found");
            return;
        }
    }

    public void onStop(){
        save_state();
        super.onStop();
    }
}
