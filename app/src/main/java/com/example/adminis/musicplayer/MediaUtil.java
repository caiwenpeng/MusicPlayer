package com.example.adminis.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adminis on 2016/7/13.
 */
public class MediaUtil {

    public static List<Mp3Info> getMp3Infos(Context context) {

        List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);


        if (cursor.getCount() != 0) {
            for (int i = 0; i < cursor.getCount(); i++) {

                Mp3Info mp3Info = new Mp3Info();
                cursor.moveToNext();
                //必须要有 cursor.moveToNext();

                String name = cursor.getString((cursor
                        .getColumnIndex(MediaStore.Audio.Media.TITLE)));
                String singer = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.ARTIST));
                long duration = cursor.getLong((cursor
                        .getColumnIndex(MediaStore.Audio.Media.DURATION)));
                String url = cursor.getString(cursor.
                        getColumnIndex(MediaStore.Audio.Media.DATA));


                Log.e("MediaUtil", name + singer);

                int isMusic = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));//是否为音乐

                if (isMusic != 0) {     //只把音乐添加到集合当中

                    mp3Info.setName(name);
                    mp3Info.setSinger(singer);
                    mp3Info.setDuration(formatTime(duration));
                    mp3Info.setTime(duration);
                    mp3Info.setUrl(url);
                    mp3Infos.add(mp3Info);
                }
            }
        }

        return mp3Infos;
    }

    public static String formatTime(long time) {

        time = time / 1000;
//        time =(long)83;
        String mTime = null;
        String hour = null;
        String min = time / 60 + "";
        String sec = time % 60 + "";

        if ((time / 60) >= 60) {
            hour = (time / 60) / 60 + "";
            min = (time / 60) % 60 + "";
        }
        if (min.length() < 2) {
            min = "0" + min;
        }
        if (sec.length() < 2) {
            sec = "0" + sec;
        }

        if (hour != null) {

            mTime = hour+":" + min + ":" + sec;
        } else {

            mTime =  min + ":" + sec;
        }

        return mTime;
    }
}
