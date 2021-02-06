package taarak.greedygames.musicwiki;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vpaliy.last_fm_api.LastFm;
import com.vpaliy.last_fm_api.LastFmService;
import com.vpaliy.last_fm_api.model.Tag;
import com.vpaliy.last_fm_api.model.TagPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import taarak.greedygames.musicwiki.Models.Model_genre;
import taarak.greedygames.musicwiki.Models.Model_topalbums;
import taarak.greedygames.musicwiki.Models.Model_topartists;
import taarak.greedygames.musicwiki.adapters.Adapter_Topalbums;
import taarak.greedygames.musicwiki.adapters.Adapter_Topartist;
import taarak.greedygames.musicwiki.adapters.Adapter_simgenre;

public class More_fragment extends Fragment {

    public static RecyclerView rv_topalbums,rv_topartists;
    TextView txt_simgenre_warn;
    ViewPager2 viewPager_simgenre;
    public View view;
    SharedPreferences sp;
    String str_genre;
    List<Model_genre> model_genres;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        view = inflater.inflate(R.layout.fragment_more,container,false);
        initviews();

        sp=getActivity().getSharedPreferences("Key",getActivity().MODE_PRIVATE);
        model_genres=new ArrayList<>();
        str_genre=sp.getString("SEL_GENRE","disco");
        new JsonTask_Albums().execute("http://ws.audioscrobbler.com/2.0/?method=tag.gettopalbums&tag="+str_genre+"&api_key="+AppConstants.APIKEY+"&format=json");
        new JsonTask_Artists().execute("http://ws.audioscrobbler.com/2.0/?method=tag.gettopartists&tag="+str_genre+"&api_key="+AppConstants.APIKEY+"&format=json");

        LastFmService service= LastFm.create(AppConstants.APIKEY)
                .createService(view.getContext());

        service.fetchSimilarTags(str_genre)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    TagPage similartag=response.result;
                    for(int i=0;i<similartag.perPage && i<8;i++){
                        model_genres.add(new Model_genre(String.valueOf(similartag.tag),AppConstants.STATIC_IMGS[i]));
                        Log.e("TEST1",model_genres.get(i).genre_name);
                    }
                    if(similartag.total>0) {
                        Adapter_simgenre adapter_simgenre = new Adapter_simgenre(getActivity(), model_genres);
                        viewPager_simgenre.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                        viewPager_simgenre.setPageTransformer(new ZoomOutPageTransformer());
                        viewPager_simgenre.setAdapter(adapter_simgenre);
                    }else{
                        txt_simgenre_warn.setVisibility(View.VISIBLE);
                    }
                });
        return view;
    }
    public void initviews(){
        rv_topalbums=view.findViewById(R.id.rv_topalbums);
        rv_topartists=view.findViewById(R.id.rv_topartists);
        viewPager_simgenre=view.findViewById(R.id.vp_frammore_simgenre);
        txt_simgenre_warn=view.findViewById(R.id.txt_frammore_simgenre_warn);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public static void adaptTopArtist(List<Model_topartists> model_topartists){

        Adapter_Topartist adapter_topartist=new Adapter_Topartist(Genre_main.context,model_topartists);
        LinearLayoutManager HorizontalLayout1 = new LinearLayoutManager(Genre_main.context, LinearLayoutManager.HORIZONTAL, false);
        rv_topartists.setLayoutManager(HorizontalLayout1);
        rv_topartists.setNestedScrollingEnabled(false);
        rv_topartists.setAdapter(adapter_topartist);

    }
    public static void adaptTopAlbums(List<Model_topalbums> model_topalbums){

        Adapter_Topalbums adapter_topalbums=new Adapter_Topalbums(Genre_main.context,model_topalbums);
        GridLayoutManager layoutManager =
                new GridLayoutManager(Genre_main.context, 2, GridLayoutManager.HORIZONTAL, false);
        rv_topalbums.setLayoutManager(layoutManager);
        rv_topalbums.setNestedScrollingEnabled(false);
        rv_topalbums.setAdapter(adapter_topalbums);
    }

}
