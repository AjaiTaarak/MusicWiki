package taarak.greedygames.musicwiki;

import android.os.AsyncTask;
import android.util.Log;

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

import taarak.greedygames.musicwiki.Models.Model_Trackmain;
import taarak.greedygames.musicwiki.Models.Model_toptracks;

class JsonTask_artisttracks extends AsyncTask<String, String, String>     {

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


            JSONObject jarray=(new JSONObject(result)).getJSONObject("toptracks");
            JSONArray albums=jarray.getJSONArray("track");
            int metadata=jarray.getJSONObject("@attr").getInt("total");
            if(metadata>1000) {
                float temp =metadata / 1000;
                Genre_main.txt_tracks.setText(String.format("%.0f", temp)+"K");
            }else{
                Genre_main.txt_tracks.setText(metadata+"");
            }
            List<Model_Trackmain> model_toptracks=new ArrayList<>();
            for (int i = 0; i < albums.length(); i++) {
                JSONObject album=albums.getJSONObject(i);
                String imageurl=(new JSONObject(String.valueOf(album.getJSONArray("image").get(1)))).getString("#text");
                Model_Trackmain model_toptrack=new Model_Trackmain(album.getString("name"),imageurl,String.valueOf(album.getJSONObject("artist").get("name")));
                model_toptracks.add(model_toptrack);

            }
            try {
                Artist_main.adaptTopAtracks(model_toptracks);
            }catch (Exception e){
                Log.e("Error",e.getMessage());
            }
        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.getMessage());
        }

    }

}