package jtkaiser.imags.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import jtkaiser.imags.SongData;
import jtkaiser.imags.database.IMAGSDbSchema.SongTable;

/**
 * Created by jtkai on 3/2/2018.
 */

public class SongCursorWrapper extends CursorWrapper{
    public SongCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public SongData getSong(){
        SongData song = new SongData();
        song.URI = getString(getColumnIndex(SongTable.Cols.URI));
        song.SessionID = getString(getColumnIndex(SongTable.Cols.SID));
        song.Acousticness = getFloat(getColumnIndex(SongTable.Cols.ACOUS));
        song.AnalysisURL = getString(getColumnIndex(SongTable.Cols.AURL));
        song.Danceability = getFloat(getColumnIndex(SongTable.Cols.DANC));
        song.Duration = getInt(getColumnIndex(SongTable.Cols.DUR));
        song.Energy = getFloat(getColumnIndex(SongTable.Cols.NRG));
        song.SongID = getString(getColumnIndex(SongTable.Cols.SONGID));
        song.Instrumentalness = getFloat(getColumnIndex(SongTable.Cols.INS));
        song.SongKey = getInt(getColumnIndex(SongTable.Cols.KEY));
        song.Liveness = getFloat(getColumnIndex(SongTable.Cols.LIVE));
        song.Loudness = getFloat(getColumnIndex(SongTable.Cols.LOUD));
        song.SongMode = getInt(getColumnIndex(SongTable.Cols.MODE));
        song.Speechiness = getFloat(getColumnIndex(SongTable.Cols.SPCH));
        song.Tempo = getFloat(getColumnIndex(SongTable.Cols.TMPO));
        song.TimeSignature = getInt(getColumnIndex(SongTable.Cols.TIME));
        song.Trackhref = getString(getColumnIndex(SongTable.Cols.HREF));
        song.Valence = getFloat(getColumnIndex(SongTable.Cols.VLNC));

        return song;
    }
}
