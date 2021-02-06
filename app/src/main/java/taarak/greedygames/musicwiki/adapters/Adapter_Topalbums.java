package taarak.greedygames.musicwiki.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.GestureDetector;
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
import taarak.greedygames.musicwiki.Models.Model_topalbums;
import taarak.greedygames.musicwiki.R;

public class Adapter_Topalbums extends RecyclerView.Adapter<Adapter_Topalbums.SimpleViewHolder>{


    int flags;
    List<Model_topalbums> model_topalbums;
    LayoutInflater inflter;
    SharedPreferences sp;
    SharedPreferences.Editor edt;


    public Adapter_Topalbums(Context applicationContext, List<Model_topalbums> model_topalbums) {


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

            txt_albumartist =view.findViewById(R.id.txt_item_album_artist);
            txt_album =view.findViewById(R.id.txt_item_album_name);
            img_albumart =view.findViewById(R.id.img_item_albumart);
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int i) {

        holder.txt_album.setText(model_topalbums.get(i).album_name);
        holder.txt_albumartist.setText(model_topalbums.get(i).album_artist);
        try {
            Picasso.get().load(model_topalbums.get(i).album_pic).error(R.drawable.ic_default).into(holder.img_albumart);
        }catch (Exception e){
            holder.img_albumart.setBackgroundResource(R.drawable.ic_default);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt.putString("SEL_ALBUM", model_topalbums.get(i).getAlbum_name());
                edt.putString("SEL_ARTIST", model_topalbums.get(i).getAlbum_artist());
                edt.putString("SEL_ALBUMPIC", model_topalbums.get(i).getAlbum_pic());
                edt.apply();

                edt.commit();
                Intent intent=new Intent(inflter.getContext(), Album_main.class);
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

