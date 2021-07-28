package com.example.photosapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.widget.ImageView;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * <h1>Photo</h1>
 * Photo class to hold information about each photo in an album.
 * <p>
 *
 * @author  Timothy Kinsey
 * @since   2019-03-31
 */

public class Photo implements java.io.Serializable {
    private static final long serialVersionUID = -1909781559553216306L;
    Album album_in;
    String caption;
    String imageuri;
    String imagebitmap;
    public ArrayList<TagPair> tags = new ArrayList<TagPair>();

    /**
     *Photo Constructor
     * @param album_in This is the album this photo is in
     * @param uri This is the path of the photo on this machine
     */
    public Photo(Album album_in, String uri, String imagebitmap){
        this.album_in=album_in;
        this.imageuri=uri;
        this.imagebitmap=imagebitmap;
    }

    /**
     *This method is used to populate TableView with Thumnnail of photo
     * @return ImageView photo to display in TableView
     */
    /*public ImageView getThumbnail() {
       ImageView imageto_show= new ImageView(this);
        imageto_show.setFitHeight(50);
        imageto_show.setFitWidth(50);
        imageto_show.setSmooth(true);
        return imageto_show;
    }*/

    /**
     *This method is used to get this photos String url
     * @return String url path of this photo
     */
    public String getImageuri() {
        return this.imageuri;
    }

    /**
     *This method is used to get this photos String url
     * @return String url path of this photo
     */
    public Bitmap getImagebitmap() {
        String encodedString=this.imagebitmap;
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    /**
     *This method is used to populate TableView with caption of photo
     * @return String photo caption
     */
    public String getCaption() {
        return this.caption;
    }

    /**
     *This method is used to set caption of photo
     * @return Nothing
     */
    public void setCaption(String tocaption) {
        this.caption=tocaption;
    }

    /**
     *This method is used to check if a photo is already present in the same album with the same caption name
     * @param o This is the object passed to see if it is equal to the initiated photo.
     * @return boolean True if the object o is equal, False otherwise.
     */
    public boolean equals(Object o) {
        if(o == null || !(o instanceof Photo)) {
            return false;
        }
        Photo p = (Photo) o;
        return this.album_in == (p.album_in) && this.imageuri.contentEquals(p.imageuri);
    }
}
