package jtkaiser.imags;

import android.content.Context;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

import jtkaiser.imags.database.DataManager;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.AudioFeaturesTrack;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.client.Response;

/**
 * Created by jtkai on 1/29/2018.
 */

public class SongDataManager {
    private static SongDataManager sSongDataManager;

    private Context mContext;
    private Track mTrack;
    private boolean mIsNewTrack;
    private SpotifyService mService;
    private AudioFeaturesTrack mFeatures;

    public static SongDataManager get(Context context, SpotifyService service){
        if (sSongDataManager == null) {
            sSongDataManager = new SongDataManager(context, service);
        }
        return sSongDataManager;
    }

    private SongDataManager(Context context, SpotifyService service){
        mContext = context.getApplicationContext();;
        mService = service;
        mIsNewTrack = false;
    }

    public void setTrack(Track track){
        mTrack = track;
        mService.getTrackAudioFeatures(mTrack.id, new SpotifyCallback<AudioFeaturesTrack>() {
            @Override
            public void failure(SpotifyError spotifyError) {
            }

            @Override
            public void success(AudioFeaturesTrack audioFeaturesTrack, Response response) {
                mFeatures = audioFeaturesTrack;
                DataManager dm = DataManager.get(mContext);
                dm.createSongEntry(mContext, mService);
            }
        });
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

    public float getAcousticness(){
        return mFeatures.acousticness;
    }

    public String getAnalysisURL(){
        return mFeatures.analysis_url;
    }

    public float getDanceability(){
        return mFeatures.danceability;
    }

    public float getDuration(){
        return mFeatures.duration_ms;
    }

    public float getEnergy(){
        return mFeatures.energy;
    }

    public String getSongID(){
        return mTrack.id;
    }

    public float getInstrumentalness(){
        return mFeatures.instrumentalness;
    }

    public int getSongKey(){
        return mFeatures.key;
    }

    public float getLiveness(){
        return mFeatures.liveness;
    }

    public float getLoudness(){
        return mFeatures.loudness;
    }

    public int getSongMode(){
        return mFeatures.mode;
    }

    public float getSpeechiness(){
        return mFeatures.speechiness;
    }

    public float getTempo(){
        return mFeatures.tempo;
    }

    public int getTimeSignature(){
        return mFeatures.time_signature;
    }

    public String gethref(){
        return mFeatures.track_href;
    }

    public float getValence(){
        return mFeatures.valence;
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
