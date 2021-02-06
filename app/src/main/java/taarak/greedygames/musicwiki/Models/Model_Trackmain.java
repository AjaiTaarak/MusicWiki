package taarak.greedygames.musicwiki.Models;

/**
 * Created by Taarak's Legion on 11-08-2019.
 */

public class Model_Trackmain {


    public String track_name;
    public String track_pic;
    public String track_duration;

    public Model_Trackmain() {
    }

    public Model_Trackmain(String track_name, String track_pic, String track_duration) {
        this.track_name = track_name;
        this.track_pic = track_pic;
        this.track_duration = track_duration;
    }

    public String getTrack_name() {
        return track_name;
    }
    public String getTrack_duration() {
        return track_duration;
    }
    public String getTrack_pic() {
        return track_pic;
    }


}