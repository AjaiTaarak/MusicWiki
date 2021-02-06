package taarak.greedygames.musicwiki;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;
import com.vpaliy.last_fm_api.LastFm;
import com.vpaliy.last_fm_api.LastFmService;
import com.vpaliy.last_fm_api.model.Album;
import com.vpaliy.last_fm_api.model.Track;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import taarak.greedygames.musicwiki.Models.Model_Trackmain;
import taarak.greedygames.musicwiki.adapters.Adapter_TrackMain;

public class Album_main extends AppCompatActivity {

    CardView crd_back;
    ImageView img_albumart;
    public  static ImageView img_track_art;
    TextView txt_artist,txt_albumname,txt_details,txt_genre;
    LinearLayout lay_btmtrackinfo_close;
    public static TextView txt_trackname,txt_timeartist,txt_summary,txt_date,txt_plays,txt_albumbtm;
    RecyclerView rv_tracks;
    SharedPreferences sp;
    public static BottomSheetBehavior bottomSheetBehavior_comment;
    public static Context context;
    List<Model_Trackmain> model_toptracks;
    public static String str_artist,str_album,str_albart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_main);
        initviews();
        sp=getApplicationContext().getSharedPreferences("Key",Album_main.MODE_PRIVATE);

        LastFmService service= LastFm.create(AppConstants.APIKEY)
                .createService(this);
        model_toptracks=new ArrayList<>();
        str_album=sp.getString("SEL_ALBUM",null);
        str_artist=sp.getString("SEL_ARTIST",null);
        str_albart=sp.getString("SEL_ALBUMPIC",null);
        lay_btmtrackinfo_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                triggerTrackinfo("dummy");
            }
        });
        service.fetchAlbumInfo(str_artist,str_album)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Album album=response.result;
                    txt_albumname.setText(String.valueOf(album.name));
                    txt_artist.setText("Album from "+ album.artist.name);
                    txt_genre.setText(sp.getString("SEL_GENRE",null));
                    txt_genre.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Album_main.this,Genre_main.class));
                        }
                    });
                    String temp_trackdetail;
                    if(album.playcount>1000){
                        if(album.playcount>1000000){
                            float temp=album.playcount/1000000;
                            temp_trackdetail= album.releaseDate + " - Played: " + temp + "M times";
                            if(isNOTNullOrEmpty(album.releaseDate)){
                                txt_details.setText(temp_trackdetail);
                            }else {
                                txt_details.setText("Played: " + temp + "M times");
                            }
                        }else {
                            float temp = album.playcount / 1000;
                            temp_trackdetail= album.releaseDate + " - Played: " + temp + "K times";
                            if(isNOTNullOrEmpty(album.releaseDate)){
                                txt_details.setText(temp_trackdetail);
                            }else {
                                txt_details.setText("Played: " + temp + "K times");
                            }
                        }
                    }else {
                        temp_trackdetail= album.releaseDate + " - Played: " + album.playcount+" times";
                        if(isNOTNullOrEmpty(album.releaseDate)){
                            txt_details.setText(temp_trackdetail);
                        }else {
                            txt_details.setText("Played: " + album.playcount+ " times");
                        }

                    }
                    try {
                        Picasso.get().load(str_albart).error(R.drawable.ic_default).into(img_albumart);
                    }catch (Exception e){
                        img_albumart.setBackgroundResource(R.drawable.ic_default);
                    }


                    for(int i=0;i<album.tracks.result.size();i++){
                        model_toptracks.add(new Model_Trackmain(String.valueOf(album.tracks.result.get(i).name),String.valueOf(album.tracks.result.get(i).image),String.valueOf(album.tracks.result.get(i).duration)));
                    }

                    Adapter_TrackMain adapter_trackMain=new Adapter_TrackMain(getApplicationContext(),model_toptracks);
                    rv_tracks.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rv_tracks.setAdapter(adapter_trackMain);
                });

    }

    public void initviews(){
        img_albumart=findViewById(R.id.img_albummain_art);
        txt_albumname=findViewById(R.id.txt_albummain_name);
        txt_artist=findViewById(R.id.txt_albummain_artist);
        txt_details=findViewById(R.id.txt_albummain_details);
        txt_genre=findViewById(R.id.txt_albummain_genre);
        crd_back=findViewById(R.id.crd_back);
        crd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txt_trackname=findViewById(R.id.txt_btm_albummain_trackname);
        txt_timeartist=findViewById(R.id.txt_btm_albummain_artist);
        txt_summary=findViewById(R.id.txt_btm_albummain_summary);
        txt_date=findViewById(R.id.txt_btm_albummain_date);
        txt_albumbtm=findViewById(R.id.txt_btm_albummain_album);
        txt_plays=findViewById(R.id.txt_btm_albummain_plays);
        img_track_art=findViewById(R.id.img_btm_trackinfo_art);
        lay_btmtrackinfo_close=findViewById(R.id.lay_btmtrackinfo_close);
        rv_tracks=findViewById(R.id.rv_albumtracks);
        context=getApplicationContext();
        final FrameLayout mBottom_trackinfo_expanded =findViewById(R.id.player_exp_comment);
        final FrameLayout mBottom_trackinfo_collapsed =findViewById(R.id.player_col_comment);
        final LinearLayout mBottomSheet_trackinfo = findViewById(R.id.bottom_sheet_trackinfo);
        bottomSheetBehavior_comment = BottomSheetBehavior.from(mBottomSheet_trackinfo);
        bottomSheetBehavior_comment.setPeekHeight(2);
        bottomSheetBehavior_comment.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_EXPANDED:{

                        mBottom_trackinfo_expanded.setVisibility(View.VISIBLE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED:{

                        mBottom_trackinfo_expanded.setVisibility(View.GONE);
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }
    public static   void triggerTrackinfo(String trackname){

        if(bottomSheetBehavior_comment.getState()!=BottomSheetBehavior.STATE_EXPANDED)
        {
            LastFmService service= LastFm.create(AppConstants.APIKEY)
                    .createService(Album_main.context);
            Log.e("TESTT",trackname);
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
                                txt_plays.setText(temp +"M plays");
                            }else {
                                float temp = track.playcount / 1000;
                                txt_plays.setText(temp +"K plays");
                            }
                        }else {
                            txt_plays.setText(String.valueOf(track.playcount)+" plays");
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
            bottomSheetBehavior_comment.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        else {
            //closed
            bottomSheetBehavior_comment.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public static boolean isNOTNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }
}
