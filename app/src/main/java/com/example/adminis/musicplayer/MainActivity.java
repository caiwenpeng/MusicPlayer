package com.example.adminis.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemClickListener {

    private ListView mMusicListview;
    private Mp3InfoAdapter mMusicAdapter;
    private List<Mp3Info> mMp3InfoList;
    private MediaPlayer mediaPlayer;

    private ImageButton btn_play;
    private ImageButton btn_pre;
    private ImageButton btn_next;
    private ImageButton btn_menu;

    private TextView text_time_change;
    private TextView text_time_duration;

    private String path;

    private long time_change = 0;
    private long time_all;

    private int mPosition = 0;

    private Handler handler = new Handler();
    private Runnable runnable;
    private MediaUtil mediaUtil;
    private MusicService musicService;
    private SeekBar seekBar;

    boolean isRunning = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        init();

        Log.e("ErrorTAG", "*****02b******");

    }

    void init() {

        initRunnable();

        mediaPlayer = new MediaPlayer();
        musicService = new MusicService(MainActivity.this);
        mediaUtil = new MediaUtil(musicService);

        mMp3InfoList = mediaUtil.getMp3Infos();

        seekBar = (SeekBar) findViewById(R.id.seekbar);
        mMusicListview = (ListView) findViewById(R.id.listviewMusic);

        mMusicAdapter = new Mp3InfoAdapter(MainActivity.this, R.layout.music_item, mMp3InfoList);


        btn_play = (ImageButton) findViewById(R.id.play);
        btn_pre = (ImageButton) findViewById(R.id.pre);
        btn_next = (ImageButton) findViewById(R.id.next);
        btn_menu = (ImageButton) findViewById(R.id.menu);

        text_time_change = (TextView) findViewById(R.id.time_change);
        text_time_duration = (TextView) findViewById(R.id.time_duration);

        btn_pre.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_play.setOnClickListener(this);
        btn_menu.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(this);
        mMusicListview.setOnItemClickListener(this);

        mMusicListview.setAdapter(mMusicAdapter);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                palyer_pause();
                if (mPosition < mMp3InfoList.size() - 1) {
                    mPosition++;
                    dataChange();
                    initMediaPlayer();
                    play();

                } else {
                    mPosition = 0;
                    dataChange();
                    initMediaPlayer();
                    play();

                }

            }
        });

        initListview();

        initMediaPlayer();

    }

    public void initListview() {

        dataChange();
        Log.e("ErrorTAG", "*****initListview******");
    }

    void dataChange() {

        //如果列表为空则返回
        if (mMp3InfoList.size() == 0)
            return;

        path = mMp3InfoList.get(mPosition).getUrl();

        //除以1000 将毫秒转换为秒
        time_all = mMp3InfoList.get(mPosition).getTime() / 1000;
        time_change = 0;

        seekBar.setMax((int) mMp3InfoList.get(mPosition).getTime());


    }

    void initMediaPlayer() {

        try {
            mediaPlayer.reset();//把各项参数恢复到初始状态
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();  //进行缓冲

            text_time_change.setText(MediaUtil.formatTime(time_change));
            text_time_duration.setText(MediaUtil.formatTime(time_all));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化Handler
     */

    private void initRunnable() {

        runnable = new Runnable() {
            @Override
            public void run() {

                //要做的事情
                time_change++;
                text_time_change.setText(MediaUtil.formatTime(mediaPlayer.getCurrentPosition() / 1000));
                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                handler.postDelayed(this, 1000);

            }
        };
    }


    void play() {

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            btn_play.setImageResource(R.drawable.pause);

            player_play();
            Toast.makeText(MainActivity.this, "播放", Toast.LENGTH_SHORT).show();

        } else if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btn_play.setImageResource(R.drawable.play);

            palyer_pause();
            Toast.makeText(MainActivity.this, "暂停", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.pre:

                palyer_pause();
                if (mPosition == 0) {
                    mPosition = mMp3InfoList.size() - 1;

                    dataChange();
                    initMediaPlayer();
                    play();

                } else {

                    mPosition--;
                    dataChange();
                    initMediaPlayer();
                    play();
                }
                break;

            case R.id.next:

                palyer_pause();
                if (mPosition < mMp3InfoList.size() - 1) {
                    mPosition++;
                    dataChange();
                    initMediaPlayer();
                    play();

                } else {
                    mPosition = 0;
                    dataChange();
                    initMediaPlayer();
                    play();

                }
                break;
            case R.id.play:

                play();
                break;

            case R.id.menu:

                Intent intent = new Intent(MainActivity.this ,Activity_PlayerInfo.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        seekBar.setProgress(progress);
        if (!mediaPlayer.isPlaying()) {

            mediaPlayer.seekTo(progress);

            time_change = progress / 1000;

            btn_play.setImageResource(R.drawable.play);

            Toast.makeText(MainActivity.this, "播放", Toast.LENGTH_SHORT).show();
            palyer_pause();
        }

        Log.e("MainActivity", progress + "");

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btn_play.setImageResource(R.drawable.play);
            palyer_pause();
        }

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        if (!mediaPlayer.isPlaying()) {

            mediaPlayer.start();
            btn_play.setImageResource(R.drawable.pause);
            player_play();
            Toast.makeText(MainActivity.this, "播放", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        palyer_pause();

        mPosition = position;
        dataChange();
        Toast.makeText(MainActivity.this, mMp3InfoList.get(position).getName(), Toast.LENGTH_SHORT).show();
        initMediaPlayer();
        play();

    }

    void palyer_pause() {

        if (isRunning) {
            handler.removeCallbacks(runnable);
            isRunning = false;
        }
    }

    void player_play() {

        if (!isRunning) {
            handler.post(runnable);
            isRunning = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }


}
