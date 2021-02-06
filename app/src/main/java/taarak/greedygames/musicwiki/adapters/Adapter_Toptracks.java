package taarak.greedygames.musicwiki.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import taarak.greedygames.musicwiki.Album_main;
import taarak.greedygames.musicwiki.Models.Model_toptracks;
import taarak.greedygames.musicwiki.R;
import taarak.greedygames.musicwiki.Tracks_fragment;

public class Adapter_Toptracks extends RecyclerView.Adapter<Adapter_Toptracks.SimpleViewHolder>{


    int flags;
    List<Model_toptracks> model_topalbums;
    LayoutInflater inflter;
    SharedPreferences sp;
    SharedPreferences.Editor edt;


    public Adapter_Toptracks(Context applicationContext, List<Model_toptracks> model_topalbums) {


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
        TextView txt_album,txt_albumartist;
        ImageView img_albumart;


        public SimpleViewHolder(View view) {
            super(view);

            txt_albumartist =view.findViewById(R.id.txt_item_topalbum_artist);
            txt_album =view.findViewById(R.id.txt_item_topalbum_name);
            img_albumart =view.findViewById(R.id.img_item_topalbum);
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_toptracks, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int i) {

        holder.txt_album.setText(model_topalbums.get(i).track_name);
        holder.txt_albumartist.setText(model_topalbums.get(i).track_artist);
        if(!model_topalbums.get(i).track_pic.contains("2a96cbd8b46e442fc41c2b86b821562f.png"))
            Picasso.get().load(model_topalbums.get(i).track_pic).error(R.drawable.ic_default).into(holder.img_albumart);
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

