package taarak.greedygames.musicwiki.Models;

/**
 * Created by Taarak's Legion on 11-08-2019.
 */

public class Model_topalbums {


    public String album_name;
    public String album_pic;
    public String album_artist;


    public Model_topalbums() {
    }

    public Model_topalbums(String album_name, String album_pic, String album_artist) {
        this.album_name = album_name;
        this.album_pic = album_pic;
        this.album_artist = album_artist;
    }

    public String getAlbum_name() {
        return album_name;
    }
    public String getAlbum_artist() {
        return album_artist;
    }
    public String getAlbum_pic() {
        return album_pic;
    }


}