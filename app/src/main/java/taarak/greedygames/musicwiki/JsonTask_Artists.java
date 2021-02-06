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

import taarak.greedygames.musicwiki.Models.Model_topalbums;
import taarak.greedygames.musicwiki.Models.Model_topartists;

class JsonTask_Artists extends AsyncTask<String, String, String> {

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

            JSONObject jarray=(new JSONObject(result)).getJSONObject("topartists");
            JSONArray albums=jarray.getJSONArray("artist");
            int metadata=jarray.getJSONObject("@attr").getInt("total");
            if(metadata>1000) {
                float temp =metadata / 1000;

                Genre_main.txt_artist.setText(String.format("%.0f", temp)+"K");
            }else{
                Genre_main.txt_artist.setText(metadata+"");
            }
            List<Model_topartists> model_topalbums=new ArrayList<>();
            for (int i = 0; i < albums.length() && i<20; i++) {
                JSONObject album=albums.getJSONObject(i);
                String imageurl=(new JSONObject(String.valueOf(album.getJSONArray("image").get(1)))).getString("#text");
                Model_topartists upload_topAlbums=new Model_topartists(album.getString("name"),imageurl);
                model_topalbums.add(upload_topAlbums);
            }
            try {
                More_fragment.adaptTopArtist(model_topalbums);
            }catch (Exception e){
                Log.e("Error",e.getMessage());
            }
        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.getMessage());
        }

    }

}
