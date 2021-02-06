package taarak.greedygames.musicwiki.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import taarak.greedygames.musicwiki.More_fragment;
import taarak.greedygames.musicwiki.Tracks_fragment;

public class TabAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public TabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Tracks_fragment tracksFragment = new Tracks_fragment();
                return tracksFragment;
            case 1:
                More_fragment albumfragment = new More_fragment();
                return albumfragment;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}