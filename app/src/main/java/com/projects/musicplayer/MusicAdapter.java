package com.projects.musicplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.viewHolder> {

    Context context;
    List<String> mList;
    WhichSong whichSong;

    public MusicAdapter(Context context, List<String> mList, WhichSong whichSong) {
        this.context = context;
        this.mList = mList;
        this.whichSong = whichSong;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
          String music = mList.get(position);
          holder.textView.setText(music);

        holder.textView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  whichSong.selectSong(position);
              }
          });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
         TextView textView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.musicName);
        }
    }

   public interface WhichSong{
        public void selectSong(int position);
   }
}
