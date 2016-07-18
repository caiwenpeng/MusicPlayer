package com.example.adminis.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by Adminis on 2016/7/17.
 * MusicService类 相当于一个服务类
 * 通过这个类可以获取 歌曲的相关信息
 * 歌曲名 、 艺术家 、图集
 * 和当前播放歌曲的时长和时间
 */
public class MusicService {

    private Context context;
    public Cursor cursor;
    private MediaPlayer mediaPlayer;


    public MusicService(Context context) {

        this.context = context;
        getAllSongsCursor();
    }

    /**
     * 获取所有歌曲 的 cursor
     */
    public void getAllSongsCursor() {

        if (cursor != null)
            return;
        Log.e("ErrorTAG", "*****aaacursor******");
        cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

    }

    /**
     * atist 艺术家
     */
    public String getUrl() {

        return cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

    }

    /**
     * atist 艺术家
     */
    public String getAtist() {

        return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));

    }

    /**
     * title 歌曲名称
     */
    public String getTitle() {
        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));

        return title;
    }

    /**
     * title 歌曲图集
     */
    public String getAlbum() throws RemoteException {

        return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
    }

    /**
     * 获取歌曲时长
     */

    public int getDuration() throws RemoteException {

        return mediaPlayer.getDuration();
    }

    /**
     * 获取当前播放时间
     */
    public int getTime() throws RemoteException {

        return mediaPlayer.getCurrentPosition();
    }

}

