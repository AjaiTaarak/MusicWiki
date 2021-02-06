package taarak.greedygames.musicwiki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import taarak.greedygames.musicwiki.Models.Model_Trackmain;
import taarak.greedygames.musicwiki.Models.Model_topalbums;
import taarak.greedygames.musicwiki.Models.Model_topartists;
import taarak.greedygames.musicwiki.adapters.Adapter_ArtistToptracks;
import taarak.greedygames.musicwiki.adapters.Adapter_Topalbums;
import taarak.greedygames.musicwiki.adapters.Adapter_Topartist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;
import com.vpaliy.last_fm_api.LastFm;
import com.vpaliy.last_fm_api.LastFmService;
import com.vpaliy.last_fm_api.model.Artist;
import com.vpaliy.last_fm_api.model.Track;

import java.util.ArrayList;
import java.util.List;

public class Artist_main extends AppCompatActivity {

    public static RecyclerView rv_topalbums, rv_toptracks;
    RecyclerView rv_similarartist;
    TextView txt_artist,txt_details,txt_summry;
    ImageView img_artist;
    public  static ImageView img_track_art;
    LinearLayout lay_btmtrackinfo_close;
    SharedPreferences sp;
    CardView crd_back;
    public static BottomSheetBehavior bottomSheetBehavior_trackinfo;
    public static Context context;

    public static TextView txt_trackname,txt_timeartist,txt_summary,txt_date,txt_plays,txt_albumbtm;
    public static String str_artist,str_album,str_albart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_main);
        initviews();
        LastFmService service= LastFm.create(AppConstants.APIKEY)
                .createService(getApplicationContext());
        sp=getApplicationContext().getSharedPreferences("Key",Artist_main.MODE_PRIVATE);
        str_artist=sp.getString("SEL_ARTIST",null);

        txt_artist.setText(str_artist);
        service.fetchArtist(str_artist).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {

                    Artist artist=response.result;
                    Picasso.get().load(String.valueOf(artist.image.get(2))).error(R.drawable.ic_default).into(img_artist);
                    txt_summry.setText(artist.bio.summary);
                    String str_details=getShortValue(artist.listeners)+" followers - "+getShortValue(artist.playcount)+" plays";
                    txt_details.setText(str_details);
                    List<Model_topartists> model_similarartist=new ArrayList<>();
                    for(int i=0;i<artist.similar.result.size() && i<20;i++){
                        model_similarartist.add(new Model_topartists(artist.similar.result.get(i).name,String.valueOf(artist.similar.result.get(i).image)));
                    }
                    Adapter_Topartist adapter_topartist=new Adapter_Topartist(Genre_main.context,model_similarartist);
                    LinearLayoutManager HorizontalLayout1 = new LinearLayoutManager(Genre_main.context, LinearLayoutManager.HORIZONTAL, false);
                    rv_similarartist.setLayoutManager(HorizontalLayout1);
                    rv_similarartist.setNestedScrollingEnabled(false);
                    rv_similarartist.setAdapter(adapter_topartist);

                });
        new JsonTask_Artistalbums().execute("http://ws.audioscrobbler.com/2.0/?method=artist.gettopalbums&artist="+str_artist+"&api_key="+AppConstants.APIKEY+"&format=json");
        new JsonTask_artisttracks().execute("http://ws.audioscrobbler.com/2.0/?method=artist.gettoptracks&artist="+str_artist+"&api_key="+AppConstants.APIKEY+"&format=json");
    }
    public static void adaptTopAlbums(List<Model_topalbums> model_topalbums){

        Adapter_Topalbums adapter_topalbums=new Adapter_Topalbums(Artist_main.context,model_topalbums);
        GridLayoutManager layoutManager =
                new GridLayoutManager(Genre_main.context, 2, GridLayoutManager.HORIZONTAL, false);
        rv_topalbums.setLayoutManager(layoutManager);
        rv_topalbums.setAdapter(adapter_topalbums);
    }
    public static void adaptTopAtracks(List<Model_Trackmain> model_toptracks){
        Adapter_ArtistToptracks adapter_trackMain=new Adapter_ArtistToptracks(context,model_toptracks);
        rv_toptracks.setLayoutManager(new LinearLayoutManager(context));
        rv_toptracks.setAdapter(adapter_trackMain);
    }
    public static void triggerTrackinfo(String trackname){

        if(bottomSheetBehavior_trackinfo.getState()!= BottomSheetBehavior.STATE_EXPANDED)
        {
            LastFmService service= LastFm.create(AppConstants.APIKEY)
                    .createService(context);
            service.fetchTrackInfo(trackname,str_artist)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        Track track=response.result;

                        Picasso.get().load(str_albart).error(R.drawable.ic_default).into(img_track_art);
                        txt_trackname.setText(trackname);
                        txt_timeartist.setText(String.valueOf(track.artist.name));
                        txt_albumbtm.setText(str_album);
                        if(track.playcount>1000){
                            if(track.playcount>1000000){
                                float temp=track.playcount/1000000;
                                txt_plays.setText(temp +"M");
                            }else {
                                float temp = track.playcount / 1000;
                                txt_plays.setText(temp +"K");
                            }
                        }else {
                            txt_plays.setText(String.valueOf(track.playcount));
                        }
                        try{
                            txt_summary.setText(String.valueOf(track.wiki.summary));
                        }catch (Exception e){
                            txt_summary.setVisibility(View.GONE);
                        }

                        try{
                            txt_date.setText(String.valueOf(track.wiki.published));
                        }catch (Exception we){
                            txt_date.setVisibility(View.GONE);
                        }

                    });
            bottomSheetBehavior_trackinfo.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        else {

            bottomSheetBehavior_trackinfo.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
    public String getShortValue(long x){
        float temp;
        if(x>1000){
            if(x>1000000){
                temp=x/1000000;
                return temp+"M";
            }else{
                temp=x/1000;
                return temp+"K";
            }
        }
        return String.valueOf(x);
    }
    public void initviews(){
        context=getApplicationContext();
        txt_artist=findViewById(R.id.txt_artist_name);
        txt_details=findViewById(R.id.txt_artistmain_details);
        txt_summry=findViewById(R.id.txt_artistmain_summary);
        rv_topalbums=findViewById(R.id.rv_artistalbums);
        rv_toptracks =findViewById(R.id.rv_artiststracks);
        rv_similarartist=findViewById(R.id.rv_artistssimilar);
        img_artist=findViewById(R.id.img_artistmain_art);
        txt_trackname=findViewById(R.id.txt_btm_albummain_trackname);
        txt_timeartist=findViewById(R.id.txt_btm_albummain_artist);
        txt_summary=findViewById(R.id.txt_btm_albummain_summary);
        txt_date=findViewById(R.id.txt_btm_albummain_date);
        txt_albumbtm=findViewById(R.id.txt_btm_albummain_album);
        txt_plays=findViewById(R.id.txt_btm_albummain_plays);
        img_track_art=findViewById(R.id.img_btm_trackinfo_art);
        lay_btmtrackinfo_close=findViewById(R.id.lay_btmtrackinfo_close);
        context=getApplicationContext();
        crd_back=findViewById(R.id.crd_back);
        crd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final FrameLayout mPlayerExpanded_comment =findViewById(R.id.player_exp_comment);
        final FrameLayout mPlayerCollapsed_comment =findViewById(R.id.player_col_comment);
        final LinearLayout mBottomSheet_trackinfo = findViewById(R.id.bottom_sheet_trackinfo);
        bottomSheetBehavior_trackinfo = BottomSheetBehavior.from(mBottomSheet_trackinfo);
        bottomSheetBehavior_trackinfo.setPeekHeight(2);
        bottomSheetBehavior_trackinfo.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_EXPANDED:{
                        //    mPlayerCollapsed.setVisibility(View.GONE);
                        mPlayerExpanded_comment.setVisibility(View.VISIBLE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED:{
                        //      mPlayerCollapsed.setVisibility(View.GONE);
                        mPlayerExpanded_comment.setVisibility(View.GONE);
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }
}
