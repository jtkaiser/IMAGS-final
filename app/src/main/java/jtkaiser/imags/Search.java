package jtkaiser.imags;

/**
 * Created by jtkai on 1/23/2018.
 */

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class Search {

    public interface View {
        void reset();

        void addData(List<Track> items);
    }

    public interface ActionListener {

        void init(String token);

        String getCurrentQuery();

        void search(String searchQuery);

        void loadMoreResults();

        void selectTrack(Track item);

        void resume();

        void pause();

        void destroy();

    }
}
