package taarak.greedygames.musicwiki.adapters;

import android.content.Context;
import android.content.Intent;
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
import taarak.greedygames.musicwiki.Artist_main;
import taarak.greedygames.musicwiki.Models.Model_topartists;
import taarak.greedygames.musicwiki.R;

public class Adapter_Topartist extends RecyclerView.Adapter<Adapter_Topartist.SimpleViewHolder>{


    int flags;
    List<Model_topartists> model_topalbums;
    LayoutInflater inflter;
    SharedPreferences sp;
    SharedPreferences.Editor edt;


    public Adapter_Topartist(Context applicationContext, List<Model_topartists> model_topalbums) {


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
        TextView  txt_artist;
        ImageView img_artist;


        public SimpleViewHolder(View view) {
            super(view);

            txt_artist =view.findViewById(R.id.txt_item_artist_name);

            img_artist =view.findViewById(R.id.img_item_artistpic);
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int i) {

        holder.txt_artist.setText(model_topalbums.get(i).artist_name);
        Picasso.get().load(model_topalbums.get(i).artist_pic).error(R.drawable.ic_default).into(holder.img_artist);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt.putString("SEL_ARTIST", model_topalbums.get(i).getArtist_name());
                edt.apply();

                edt.commit();
                Intent intent=new Intent(inflter.getContext(), Artist_main.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                inflter.getContext().startActivity(intent);

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

