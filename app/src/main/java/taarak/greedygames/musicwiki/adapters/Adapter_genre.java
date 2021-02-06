package taarak.greedygames.musicwiki.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import taarak.greedygames.musicwiki.Genre_main;
import taarak.greedygames.musicwiki.R;
import taarak.greedygames.musicwiki.Models.Model_genre;

public class Adapter_genre extends RecyclerView.Adapter<Adapter_genre.SimpleViewHolder>{


    int flags;
    List<Model_genre> model_genres;
    LayoutInflater inflter;
    SharedPreferences sp;
    SharedPreferences.Editor edt;


    public Adapter_genre(Context applicationContext,List<Model_genre> upload_genres) {


        this.model_genres =upload_genres;
        this.flags=upload_genres.size();

        inflter = (LayoutInflater.from(applicationContext));
        sp=inflter.getContext().getSharedPreferences("Key",inflter.getContext().MODE_PRIVATE);
        edt=sp.edit();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        TextView txt_genrename;
        ImageView img_genre;

        public SimpleViewHolder(View view) {
            super(view);


            txt_genrename =view.findViewById(R.id.txt_item_genre);
            img_genre =view.findViewById(R.id.img_item_genre);
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int i) {
        String first_letter=String.valueOf(model_genres.get(i).genre_name.charAt(0));
        String temp= model_genres.get(i).genre_name.replaceFirst(first_letter,first_letter.toUpperCase());
        holder.txt_genrename.setText(temp);
        holder.img_genre.setBackgroundResource(model_genres.get(i).genre_imgid);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt.putString("SEL_GENRE", model_genres.get(i).getGenre_name());
                edt.putInt("SEL_GENREPIC",model_genres.get(i).getGenre_imgid());
                edt.apply();

                edt.commit();
                Intent intent=new Intent(inflter.getContext(),Genre_main.class);
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

