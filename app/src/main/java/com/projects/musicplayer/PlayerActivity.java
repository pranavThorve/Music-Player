package com.projects.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayerActivity extends AppCompatActivity {

    TextView songName,currTime,totalTime;
    ImageView fare,fafo,play;
    SeekBar seekBar;
    ImageView imageView;

    static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        songName = findViewById(R.id.Name);
        currTime = findViewById(R.id.currtime);
        totalTime = findViewById(R.id.total);
        fare= findViewById(R.id.fr);
        fafo = findViewById(R.id.ff);
        play = findViewById(R.id.play);
        seekBar = findViewById(R.id.seekbar);
        imageView = findViewById(R.id.bgm);

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        String songPath = getIntent().getStringExtra("songPath");
        String songN = getIntent().getStringExtra("songName");



        Uri uri = Uri.parse(songPath);
        songName.setText(songN);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();

        totalTime.setText(createTime(mediaPlayer.getDuration()));


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;

                while(currentPosition < totalDuration){
                    try {

                        Thread.sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        seekBar.setMax(mediaPlayer.getDuration());
        thread.start();

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String curr = createTime(mediaPlayer.getCurrentPosition());
                currTime.setText(curr);
                handler.postDelayed(this,1000);
            }
        },1000);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    play.setBackgroundResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                }else{
                    play.setBackgroundResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                }
            }
        });

        fafo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
                }
            }
        });

        fare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
                }
            }
        });

    }

    public String createTime(int duration){
        String time= "";
        int min = duration/1000/60;
        int sec = duration/1000%60;

        time+=min+":";
        if(sec < 10){
            time+="0";
        }

        time+=sec;
        return time;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mediaPlayer.stop();
    }
}