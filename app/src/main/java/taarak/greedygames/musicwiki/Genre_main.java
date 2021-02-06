package taarak.greedygames.musicwiki;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.vpaliy.last_fm_api.LastFm;
import com.vpaliy.last_fm_api.LastFmService;
import com.vpaliy.last_fm_api.model.Tag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import taarak.greedygames.musicwiki.adapters.TabAdapter;
import taarak.greedygames.musicwiki.ui.main.SectionsPagerAdapter;


public class Genre_main extends AppCompatActivity {

    TextView txt_title,txt_summary;
    LinearLayout laygenremain_header;
    ImageView img_genreart;
    public static Context context;
    public static TextView txt_alb,txt_artist,txt_tracks;
    SharedPreferences sp;
    ImageButton img_back;
    SharedPreferences.Editor edt;
    String str_genre_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_main);
        context=getApplicationContext();
        initviews();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Tracks"));
        tabLayout.addTab(tabLayout.newTab().setText("More"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
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
                    laygenremain_header.setVisibility(View.GONE);
                } else if (isShow) {
                    isShow = false;
                    laygenremain_header.setVisibility(View.VISIBLE);
                }
            }
        });

        sp=getApplication().getSharedPreferences("Key",Genre_main.MODE_PRIVATE);
        edt=sp.edit();

        str_genre_name=sp.getString("SEL_GENRE",null);
        img_genreart.setBackgroundResource(sp.getInt("SEL_GENREPIC",R.drawable.ic_default));
        final TabAdapter adapter = new TabAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        txt_title.setText(str_genre_name);
        LastFmService service=LastFm.create(AppConstants.APIKEY).createService(getApplicationContext());
        service.fetchTagInfo(str_genre_name).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Tag tag=response.result;
                    String str_summary=tag.wiki.summary.substring(0,tag.wiki.summary.indexOf("."));
                    txt_summary.setText(str_summary);
                });


    }
    public void initviews(){
        txt_title=findViewById(R.id.txt_genremain_title);
        txt_summary=findViewById(R.id.txt_genremain_summary);
        txt_alb=findViewById(R.id.txt_genremain_no_albums);
        txt_artist=findViewById(R.id.txt_genremain_no_artist);
        txt_tracks=findViewById(R.id.txt_genremain_no_tracks);
        img_genreart=findViewById(R.id.img_genremain_art);
        img_back=findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        laygenremain_header=findViewById(R.id.laygenremain_header);
    }


}
