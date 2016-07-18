package com.example.adminis.musicplayer;

import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adminis on 2016/7/13.
 */
public class MediaUtil {

    private MusicService musicService;

    public MediaUtil(MusicService musicService) {

        this.musicService = musicService;
    }

    public List<Mp3Info> getMp3Infos() {

        List<Mp3Info> mp3Infos = new ArrayList<>();
        //获取数据库中的音乐文件 cursor

        if (musicService.cursor.getCount() != 0) {

            for (int i = 0; i < musicService.cursor.getCount(); i++) {

                Mp3Info mp3Info = new Mp3Info();
                musicService.cursor.moveToNext();
                //必须要有 cursor.moveToNext();
                String name = musicService.getTitle();
                String url = musicService.getUrl();
                String singer = musicService.getAtist();

                long duration = musicService.cursor.getLong((musicService.cursor
                        .getColumnIndex(MediaStore.Audio.Media.DURATION)));

                Log.e("MediaUtil", name + singer);

                int isMusic = musicService.cursor.getInt(musicService.cursor
                        .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));//是否为音乐

                if (isMusic != 0 && duration >0) {
                    //只把音乐添加到集合当中
                    mp3Info.setName(name);
                    mp3Info.setSinger(singer);
                    mp3Info.setDuration(formatTime(duration /1000 ));
                    mp3Info.setTime(duration);
                    mp3Info.setUrl(url);
                    mp3Infos.add(mp3Info);
                }
            }
        }

        return mp3Infos;
    }

    public static String formatTime(long time) {

        //long time 秒的级别

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

            mTime = hour + ":" + min + ":" + sec;
        } else {

            mTime = min + ":" + sec;
        }

        return mTime;
    }
}
