package jtkaiser.imags.database;

/**
 * Created by jtkai on 3/1/2018.
 */

public class IMAGSDbSchema {
    public static final class SessionTable{
        public static final String NAME = "sessions";

        public static final class Cols{
            //sessions table info
            public static final String SID = "SessionID";
            public static final String PID = "ParticipantID";
            public static final String START = "startTime";
            public static final String DUR = "duration";
        }
    }
    public static final class PainLogTable{
        public static final String NAME = "PainLogs";

        public static final class Cols{
            //pain log table info
            public static final String timeStamp = "time";
            public static final String painLVL = "painLvl";
            public static final String pSID = "painSID";
        }
    }

    public static final class ParticipantTable{
        public static final String NAME = "Participants";

        public static final class Cols{
            public static final String PID = "PID";
        }
    }

    public static final class SongTable{
        public static final String NAME = "songs";

        public static final class Cols{
            public static final String URI = "URI";
            public static final String SID = "SessionID";
            public static final String ACOUS= "Acousticness";
            public static final String AURL= "AnalysisURL";
            public static final String DANC= "Danceability";
            public static final String DUR = "DurationMS";
            public static final String NRG = "ENERGY";
            public static final String SONGID = "SongID";
            public static final String INS = "Instrumentalness";
            public static final String KEY = "SongKey";
            public static final String LIVE = "Liveness";
            public static final String LOUD = "Loudness";
            public static final String MODE = "SongMode";
            public static final String SPCH = "Speechiness";
            public static final String TMPO = "Tempo";
            public static final String TIME = "TimeSignature";
            public static final String HREF = "Trackhref";
            public static final String VLNC = "Valence";
        }
    }

    public static final class MedicationTable{
        public static final String NAME = "medications";

        public static final class Cols{
            public static final String SID = "SessionID";
            public static final String TOOK = "TookMed";
            public static final String MED = "Name";
            public static final String DOSE = "Dosage";
        }
    }
}

