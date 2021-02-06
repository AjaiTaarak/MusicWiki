package taarak.greedygames.musicwiki.Models;

/**
 * Created by Taarak's Legion on 11-08-2019.
 */

public class Model_toptracks {


    public String track_name;
    public String track_pic;
    public String track_artist;

    public Model_toptracks() {
    }

    public Model_toptracks(String track_name, String track_pic, String track_artist) {
        this.track_name = track_name;
        this.track_pic = track_pic;
        this.track_artist = track_artist;
    }

    public String getTrack_name() {
        return track_name;
    }
    public String getTrack_artist() {
        return track_artist;
    }
    public String getTrack_pic() {
        return track_pic;
    }


}