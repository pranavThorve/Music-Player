package com.projects.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MusicAdapter.WhichSong {

    List<String> mList;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclecontainer);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 20);
        }else{
            displaySong();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 20){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                displaySong();
            }
        }
    }

    public List<File> getMusicList(File file){

        List<File> musicList = new ArrayList<>();

        File[]  files = file.listFiles();

        for(File singleFile:files){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                musicList.addAll(getMusicList(singleFile));
            }else{
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".m4a")){
                    musicList.add(singleFile);
                }
            }
        }
        return musicList;
    }

    List<File> files;
    void displaySong(){
      files  = getMusicList(Environment.getExternalStorageDirectory());

        mList = new ArrayList<>();

        for(int i = 0; i < files.size();i++){
            mList.add(files.get(i).getName().toString().replace(".mp3","").replace(".wav",""));
        }

        MusicAdapter adapter = new MusicAdapter(this,mList,this);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void selectSong(int position) {
        Intent intent = new Intent(this,PlayerActivity.class);
        intent.putExtra("songName",files.get(position).getName());
        intent.putExtra("songPath",files.get(position).getPath());
        startActivity(intent);
    }
}