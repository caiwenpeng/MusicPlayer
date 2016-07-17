package com.example.adminis.musicplayer;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private ListView mMusicListview;
    private Mp3InfoAdapter mMusicAdapter;
    private List<Mp3Info> mMp3InfoList;
    private MediaPlayer mediaPlayer;

    private ImageButton btn_play;
    private ImageButton btn_pre;
    private ImageButton btn_next;

    private TextView text_time_change;
    private TextView text_time_duration;

    private String path;
    private String time_duration;

    private long time_change;
    private long time_all;

    private int mPosition = 0;
    volatile static int mProgress = 0;

    private Handler handler = new Handler();
    private Runnable runnable;

    private SeekBar seekBar;

    boolean isRunning = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        initListview();

        init();

        mMusicListview.setAdapter(mMusicAdapter);

        mMusicListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (isRunning) {
                    handler.removeCallbacks(runnable);
                    isRunning = false;
                }

                mPosition = position;
                dataChange();

                Toast.makeText(MainActivity.this, mMp3InfoList.get(position).getName(), Toast.LENGTH_SHORT).show();

                initMediaPlayer();
                play();

            }
        });

        initMediaPlayer();


    }

    void init() {

        timeTask();

        mediaPlayer = new MediaPlayer();

        seekBar = (SeekBar) findViewById(R.id.seekbar);

        mMusicListview = (ListView) findViewById(R.id.listviewMusic);

        mMusicAdapter = new Mp3InfoAdapter(MainActivity.this, R.layout.music_item, mMp3InfoList);

        btn_play = (ImageButton) findViewById(R.id.play);
        btn_pre = (ImageButton) findViewById(R.id.pre);
        btn_next = (ImageButton) findViewById(R.id.next);

        text_time_change = (TextView) findViewById(R.id.time_change);
        text_time_duration = (TextView) findViewById(R.id.time_duration);

        btn_pre.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_play.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setMax(1000);
    }

    public void initListview() {

        mMp3InfoList = MediaUtil.getMp3Infos(MainActivity.this);
        dataChange();
    }

    void dataChange() {

        path = mMp3InfoList.get(mPosition).getUrl();

        time_all = mMp3InfoList.get(mPosition).getTime() / 1000;
        time_change = mMp3InfoList.get(mPosition).getTime() / 1000;
        time_duration = mMp3InfoList.get(mPosition).getDuration();

        Log.e("ErrorTAG", "*****01b******");

    }

    void play() {

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            btn_play.setImageResource(R.drawable.pause);

            Toast.makeText(MainActivity.this, "paly", Toast.LENGTH_SHORT).show();

            if (!isRunning) {
                handler.post(runnable);
                isRunning = true;
            }

        } else if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btn_play.setImageResource(R.drawable.play);

            if (isRunning) {
                handler.removeCallbacks(runnable);
                isRunning = false;

            }


            Toast.makeText(MainActivity.this, "pause", Toast.LENGTH_SHORT).show();

        }
    }

    void initMediaPlayer() {

        try {
            mediaPlayer.reset();//把各项参数恢复到初始状态
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();  //进行缓冲

            text_time_change.setText(formateTime(time_change));
            text_time_duration.setText(time_duration);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formateTime(long time_change) {

        long time = time_change;

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

    private void timeTask() {
        runnable = new Runnable() {
            @Override
            public void run() {

                //要做的事情

                int time = mediaPlayer.getCurrentPosition()/1000 ;

                mProgress = (int) ((mediaPlayer.getCurrentPosition() * 1.0 / 1000) / time_all * 1000);

//                mProgress = (int) ((1.00 - (time_change * 1.0) / (time_all * 1.0)) * 1000);

                Log.e("MainActivity", "mProgress:" + mProgress + "getCurrentPosition" + (mediaPlayer.getCurrentPosition() / 1000));

                time_change--;

                text_time_change.setText(formateTime(time));

                seekBar.setProgress(mProgress);

                handler.postDelayed(this, 1000);

            }
        };

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.pre:

                if (isRunning) {
                    handler.removeCallbacks(runnable);
                    isRunning = false;
                }

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

                if (isRunning) {
                    handler.removeCallbacks(runnable);
                    isRunning = false;
                }

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


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        int secs = 0;
        seekBar.setProgress(progress);

        secs = (int) ((progress * 1.0 / 1000 * 1.0) * time_all * 1000);


        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(secs);
            btn_play.setImageResource(R.drawable.pause);

            Toast.makeText(MainActivity.this, "paly", Toast.LENGTH_SHORT).show();

            if (!isRunning) {
                handler.post(runnable);
                isRunning = true;
            }
        }

        Log.e("MainActivity", progress + "");

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btn_play.setImageResource(R.drawable.play);

            if (isRunning) {
                handler.removeCallbacks(runnable);
                isRunning = false;
            }
        }

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            btn_play.setImageResource(R.drawable.pause);

            Toast.makeText(MainActivity.this, "paly", Toast.LENGTH_SHORT).show();

            if (!isRunning) {
                handler.post(runnable);
                isRunning = true;
            }
        }
    }

}
