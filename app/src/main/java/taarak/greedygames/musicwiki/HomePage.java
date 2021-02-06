package taarak.greedygames.musicwiki;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.appbar.AppBarLayout;
import com.vpaliy.last_fm_api.LastFm;
import com.vpaliy.last_fm_api.LastFmService;
import com.vpaliy.last_fm_api.auth.LastFmAuth;
import com.vpaliy.last_fm_api.model.Session;
import com.vpaliy.last_fm_api.model.TagPage;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import taarak.greedygames.musicwiki.Models.Model_genre;
import taarak.greedygames.musicwiki.adapters.Adapter_genre;

public class HomePage extends AppCompatActivity {

    TextView txt_homesub,txt_loadmore;
    RecyclerView rv_genres;
    LinearLayout lay_genreheader;
    List<Model_genre> upload_genres;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        initviews();
        upload_genres=new ArrayList<>();
        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    lay_genreheader.setVisibility(View.VISIBLE);
                } else if (isShow) {
                    isShow = false;
                    lay_genreheader.setVisibility(View.GONE);
                }
            }
        });

        LastFmService service= LastFm.create(AppConstants.APIKEY)
                .createService(this);

        service.fetchChartTopTags(1,16)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    TagPage album = response.result;
                    try {

                        for (int i = 0; i < album.tag.size() && i<16; i++) {
                            upload_genres.add(new Model_genre(album.tag.get(i).name,AppConstants.STATIC_IMGS[i]));
//                            Log.e("Artist", album.tag.get(i).name + " ");
                        }
                        List<Model_genre> upload_genrestop=new ArrayList<>();
                        upload_genrestop.addAll(upload_genres.subList(0,10));
                        Adapter_genre adapter_genre=new Adapter_genre(getApplicationContext(),upload_genrestop);

//                        rv_genres.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 0));

                        rv_genres.setLayoutManager(new GridLayoutManager(getApplicationContext(),2, LinearLayoutManager.VERTICAL,false));
                        rv_genres.setAdapter(adapter_genre);
                        txt_loadmore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Adapter_genre adapter_genre=new Adapter_genre(getApplicationContext(),upload_genres);
                                rv_genres.setAdapter(null);
                                rv_genres.setLayoutManager(new GridLayoutManager(getApplicationContext(),2, LinearLayoutManager.VERTICAL,false));
                                rv_genres.setAdapter(adapter_genre);
                                txt_loadmore.setVisibility(View.GONE);
                            }
                        });
                    }catch (Exception e){
                        Log.e("Error Home",e.getMessage());
                    }
                });
    }
    public void initviews(){
        txt_homesub=findViewById(R.id.txt_homesub);
        rv_genres=findViewById(R.id.rv_genres);
        lay_genreheader=findViewById(R.id.lay_genreheader);
        txt_loadmore=findViewById(R.id.txt_genre_loadmore);
    }

    @Override
    public void onBackPressed() {

    }
}
