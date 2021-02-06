package taarak.greedygames.musicwiki.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import taarak.greedygames.musicwiki.Album_main;
import taarak.greedygames.musicwiki.Models.Model_Trackmain;
import taarak.greedygames.musicwiki.R;

public class Adapter_TrackMain extends RecyclerView.Adapter<Adapter_TrackMain.SimpleViewHolder>{


    int flags;
    List<Model_Trackmain> model_topalbums;
    LayoutInflater inflter;
    SharedPreferences sp;
    SharedPreferences.Editor edt;


    public Adapter_TrackMain(Context applicationContext, List<Model_Trackmain> model_topalbums) {


        this.model_topalbums =model_topalbums;
        this.flags=model_topalbums.size();

        inflter = (LayoutInflater.from(applicationContext));
        sp=inflter.getContext().getSharedPreferences("Key",inflter.getContext().MODE_PRIVATE);
        edt=sp.edit();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        TextView txt_trackname, txt_duration;
        ImageView img_trackinfo;


        public SimpleViewHolder(View view) {
            super(view);

            txt_duration =view.findViewById(R.id.txt_item_track_duration);
            txt_trackname =view.findViewById(R.id.txt_item_track_name);
            img_trackinfo =view.findViewById(R.id.txt_item_track_more);
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trackmain, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int i) {

        holder.txt_trackname.setText(model_topalbums.get(i).track_name);
        try {
            int s = Integer.parseInt(model_topalbums.get(i).track_duration);
            int sec = s % 60;
            int min = (s / 60) % 60;
            if(s>=10) {
                holder.txt_duration.setText(min + ":" +sec);
            }else{
                holder.txt_duration.setText(min + ":0" +sec);
            }
        }catch (Exception e){
            Log.e("Error",e.getMessage());

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edt.putString("SEL_TRACKINFO", String.valueOf(model_topalbums.get(i).track_name));
                edt.apply();
                edt.commit();

                Album_main.triggerTrackinfo(String.valueOf(model_topalbums.get(i).track_name));
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return flags;
    }
}

