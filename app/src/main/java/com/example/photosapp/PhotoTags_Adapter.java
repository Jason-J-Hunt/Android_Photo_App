package com.example.photosapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PhotoTags_Adapter extends ArrayAdapter<TagPair> {
    private Context tContext;
    private List<TagPair> tagPairList = new ArrayList<>();

    public PhotoTags_Adapter(Context context, ArrayList<TagPair> list) {
        super(context, 0 , list);
        tContext = context;
        tagPairList = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(tContext).inflate(R.layout.display_photo_tags_listitem,parent,false);

        TagPair currentTag = tagPairList.get(position);
        try{
            TextView tag = (TextView)listItem.findViewById(R.id.Tag);
            TextView tagvalue = (TextView)listItem.findViewById(R.id.TagValue);

            tag.setText(currentTag.getTagname());
            tagvalue.setText(currentTag.getTagvalue());
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return listItem;
    }
}
