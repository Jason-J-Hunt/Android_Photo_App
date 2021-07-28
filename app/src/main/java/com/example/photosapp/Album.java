package com.example.photosapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * <h1>Album</h1>
 * Album class to hold information about each Album a user has.
 * <p>
 *
 * @author  Timothy Kinsey
 * @since   2019-03-31
 */

public class Album implements java.io.Serializable{
    private static final long serialVersionUID = -1096808658319587518L;
    String albumname;
    public ArrayList<Photo> photos=new ArrayList<Photo>();

    /**
     *Album Constructor
     * @param albumname This is the name for the given Album
     */
    public Album(String albumname) {
        this.albumname=albumname;
    }

    /**
     *This method is used to populate TableView with album name
     * @return String album name
     */
    public String getAlbumname() {
        return this.albumname;
    }

    /**
     *This method is used to set the album name
     * @return String album name
     */
    public void setAlbumname(String newalbumname) {
        this.albumname=newalbumname;
    }

    /**
     *This method is used to print an object of type album by its album name
     * @return String albumname
     */
    public String toString() {
        return this.albumname+"";
    }

    /**
     *This method is used to check if a Album is already present with the albumname
     * @param o This is the object passed to see if it is equal to the initiated album.
     * @return boolean True if the object o is equal, False otherwise.
     */
    public boolean equals(Object o) {
        if(o == null || !(o instanceof Album)) {
            return false;
        }
        Album a = (Album) o;
        return this.albumname.equalsIgnoreCase(a.albumname);
    }
}

