package jtkaiser.imags;

import android.content.Context;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by jtkai on 1/29/2018.
 */

public class TrackData {
    private static TrackData sTrackData;

    private Track mTrack;
    private Boolean mIsNewTrack;

    public static TrackData get(){
        if (sTrackData == null) {
            sTrackData = new TrackData();
        }
        return sTrackData;
    }

    private TrackData(){
        mIsNewTrack = false;

    }

    public void setTrack(Track track){
        mTrack = track;
    }

    public Track getTrack(){
        return mTrack;
    }

    public String getName(){
        return mTrack.name;
    }

    public String getUri(){
        return mTrack.uri;
    }

    public String getArtistNames(){
        List<String> names = new ArrayList<>();
        for (ArtistSimple i : mTrack.artists) {
            names.add(i.name);
        }
        Joiner joiner = Joiner.on(", ");
        return(joiner.join(names));
    }

    public String getAlbumName(){
        return mTrack.album.name;
    }

    public String getImageUrl(){
        Image image = mTrack.album.images.get(0);
        if (image != null) {
            return image.url;
        }
        else return "";
    }

    public Boolean isNewTrack() {
        return mIsNewTrack;
    }

    public void setNewTrack(){
        mIsNewTrack = true;
    }
}
