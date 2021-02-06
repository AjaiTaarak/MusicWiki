package taarak.greedygames.musicwiki.Models;

/**
 * Created by Taarak's Legion on 11-08-2019.
 */

public class Model_topartists {


    public String artist_name;
    public String artist_pic;



    public Model_topartists() {
    }

    public Model_topartists(String artist_name, String artist_pic) {
        this.artist_name = artist_name;
        this.artist_pic = artist_pic;

    }
    public String getArtist_name() {
        return artist_name;
    }
    public String getArtist_pic() {
        return artist_pic;
    }


}