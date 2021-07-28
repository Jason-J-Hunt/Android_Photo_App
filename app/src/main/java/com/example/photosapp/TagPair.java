package com.example.photosapp;

/**
 * <h1>TagPair</h1>
 * TagPair class to hold information about each tag a photo in an album has.
 * <p>
 *
 * @author  Timothy Kinsey
 * @since   2019-03-31
 */
public class TagPair implements java.io.Serializable {
    private static final long serialVersionUID = -6098450046445379145L;
    String tagname;
    String tagvalue;

    public TagPair(String tagname, String tagvalue) {
        this.tagname=tagname;
        this.tagvalue=tagvalue;
    }

    public String getTagname() {
        return this.tagname;
    }

    public String getTagvalue() {
        return this.tagvalue;
    }

    public boolean equals(Object o) {
        if(o == null || !(o instanceof TagPair)) {
            return false;
        }
        TagPair t = (TagPair) o;
        return this.tagname.equalsIgnoreCase(t.tagname) && (this.tagvalue.equalsIgnoreCase(t.tagvalue) || this.tagvalue.toLowerCase().startsWith(t.tagvalue.toLowerCase()));
    }
}

