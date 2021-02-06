package taarak.greedygames.musicwiki;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;
import com.vpaliy.last_fm_api.LastFm;
import com.vpaliy.last_fm_api.LastFmService;
import com.vpaliy.last_fm_api.model.Track;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import taarak.greedygames.musicwiki.Models.Model_topalbums;
import taarak.greedygames.musicwiki.Models.Model_toptracks;
import taarak.greedygames.musicwiki.adapters.Adapter_Toptracks;

public class Tracks_fragment extends Fragment {

    public static RecyclerView rv_toptrack;
    public View view;
    public static Context context;
    SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_tracks, container, false);
        rv_toptrack=view.findViewById(R.id.rv_toptracks);
        sp=getActivity().getSharedPreferences("Key",getActivity().MODE_PRIVATE);

        new JsonTask_tracks().execute("http://ws.audioscrobbler.com/2.0/?method=tag.gettoptracks&tag="+sp.getString("SEL_GENRE","disco")+"&api_key="+AppConstants.APIKEY+"&format=json");
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    public static void adapt(List<Model_toptracks> model_toptracks){

        Adapter_Toptracks adapter_toptracks=new Adapter_Toptracks(Genre_main.context,model_toptracks);
        rv_toptrack.setLayoutManager(new LinearLayoutManager(Genre_main.context));
        rv_toptrack.setNestedScrollingEnabled(false);
        rv_toptrack.setAdapter(adapter_toptracks);

    }

}
class JsonTask_tracks extends AsyncTask<String, String, String>     {

    protected void onPreExecute() {
        super.onPreExecute();


    }

    protected String doInBackground(String... params) {


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            return buffer.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {


            JSONObject jarray=(new JSONObject(result)).getJSONObject("tracks");
            JSONArray albums=jarray.getJSONArray("track");
            int metadata=jarray.getJSONObject("@attr").getInt("total");
            if(metadata>1000) {
                float temp =metadata / 1000;
                Genre_main.txt_tracks.setText(String.format("%.0f", temp)+"K");
            }else{
                Genre_main.txt_tracks.setText(metadata+"");
            }
            List<Model_toptracks> model_toptracks=new ArrayList<>();
            for (int i = 0; i < albums.length(); i++) {
                JSONObject album=albums.getJSONObject(i);
                String imageurl=(new JSONObject(String.valueOf(album.getJSONArray("image").get(1)))).getString("#text");
                Model_toptracks model_toptrack=new Model_toptracks(album.getString("name"),imageurl,String.valueOf(album.getJSONObject("artist").get("name")));
                model_toptracks.add(model_toptrack);
            }
            try {
                Tracks_fragment.adapt(model_toptracks);
            }catch (Exception e){
                Log.e("Error",e.getMessage());
            }
        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.getMessage());
        }

    }

}