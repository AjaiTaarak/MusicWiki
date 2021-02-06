package taarak.greedygames.musicwiki.Models;

/**
 * Created by Taarak's Legion on 11-08-2019.
 */

public class Model_genre {


    public String genre_name;
    public int genre_imgid;

    public Model_genre() {
    }

    public Model_genre(String genre_name, int genre_imgid) {
        this.genre_name = genre_name;
        this.genre_imgid = genre_imgid;
    }

    public String getGenre_name() {
        return genre_name;
    }

    public int getGenre_imgid() {
        return genre_imgid;
    }


}