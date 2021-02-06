package taarak.greedygames.musicwiki.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import taarak.greedygames.musicwiki.Album_main;
import taarak.greedygames.musicwiki.Artist_main;
import taarak.greedygames.musicwiki.Models.Model_Trackmain;
import taarak.greedygames.musicwiki.R;

public class Adapter_ArtistToptracks extends RecyclerView.Adapter<Adapter_ArtistToptracks.SimpleViewHolder>{


    int flags;
    List<Model_Trackmain> model_topalbums;
    LayoutInflater inflter;
    SharedPreferences sp;
    SharedPreferences.Editor edt;


    public Adapter_ArtistToptracks(Context applicationContext, List<Model_Trackmain> model_topalbums) {


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
        holder.txt_duration.setText(model_topalbums.get(i).track_duration);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edt.putString("SEL_TRACKINFO", String.valueOf(model_topalbums.get(i).track_name));
                edt.apply();
                edt.commit();

                Artist_main.triggerTrackinfo(String.valueOf(model_topalbums.get(i).track_name));
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

