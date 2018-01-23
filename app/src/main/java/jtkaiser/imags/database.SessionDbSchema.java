package jtkaiser.imags;

/**
 * Created by amybea on 1/23/2018.
 */

public class SessionDbSchema {
    public static final class SessionTable {
        public static final String NAME = "sessions";

        public static final class Columns {
            public static final String SID = "session ID";
            public static final String PID = "patient ID";
            public static final String URI = "song URI";
            public static final String MED = "medication";
            public static final String LEN = "session duration";
        }
    }

    public static final class PainLog {
        public static final String NAME = "pain logs";

        public static final class Columns {
            public static final String SID = "session ID";
            public static final String START = "time";
            public static final String PAIN = "pain level";
        }
    }
}


