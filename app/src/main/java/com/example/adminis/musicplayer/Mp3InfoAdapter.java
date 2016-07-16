package com.example.adminis.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Adminis on 2016/7/12.
 */
public class Mp3InfoAdapter extends ArrayAdapter<Mp3Info> {


    int resourceId;

    public Mp3InfoAdapter(Context context, int resource, List<Mp3Info> objects) {
        super(context, resource, objects);

        resourceId = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Mp3Info mp3Info = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView name = (TextView) view.findViewById(R.id.name);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView duration = (TextView) view.findViewById(R.id.duration);

        name.setText(mp3Info.getName());
        title.setText(mp3Info.getSinger());
        duration.setText(mp3Info.getDuration());
        return view;

    }

}
