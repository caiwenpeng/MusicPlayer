package com.example.adminis.musicplayer;

/**
 * Created by Adminis on 2016/7/13.
 */
public class Mp3Info {

    /**
     * String name 歌曲名
     * String singer 歌手
     * String duration 时长(String)
     * long time 时长（long）
     * String url 路径
     */

    private String url;
    private String name;
    private String singer;
    private String duration;
    private long time;


    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getSinger() {
        return singer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }



    public Mp3Info(String name, String singer, String duration) {

        this.name = name;
        this.singer = singer;
        this.duration = duration;
    }


    public Mp3Info() {

        setName("未知");
        setSinger("未知");
        setDuration("未知");
    }


}
