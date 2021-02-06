package taarak.greedygames.musicwiki.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import taarak.greedygames.musicwiki.Models.Model_genre;
import taarak.greedygames.musicwiki.R;

public class Adapter_simgenre extends RecyclerView.Adapter<Adapter_simgenre.MyViewHolder> {
    Context context;
    List<Model_genre> model_genres;
    LayoutInflater layoutInflater;

public Adapter_simgenre(Context context, List<Model_genre> model_genre) {
    this.context = context;
    this.model_genres=model_genre;

    layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
}
@NonNull
@Override
public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_simgenre, parent, false);
        return new MyViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    holder.txt_name.setText(model_genres.get(position).genre_name);
    holder.img_genrepic.setBackgroundResource(model_genres.get(position).genre_imgid);


}

@Override
public int getItemCount() {
        return model_genres.size();
        }

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView txt_name;
    ImageView img_genrepic;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_name=itemView.findViewById(R.id.txt_item_simgenre_name);

        img_genrepic=itemView.findViewById(R.id.img_item_simgenre_art);
    }
}
}