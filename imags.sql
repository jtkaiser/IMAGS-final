create table if not exists Songs (
	URI		varchar2(50) primary key,
	Acousticness float(5),
	AnalysisURL varchar2(100),
	Danceability float(5),
	DurationMS number(10),
	Energy float(5),
	SongID varchar2(50),
	Instrumentalness float(5),
	SongKey number(1),
	Liveness float(5),
	Loudness float(5),
	SongMode number(1),
	Speechiness float(5),
	Tempo			float(5),
	TimeSignature number(5),
	TrackHref varchar2(100),
	Valence float(5),
	Progressions varchar2(50),
);


create table if not exists Sessions (
	SessionID		char(36) primary key,
	PatientID		varchar2(50),
	SongURI 	varchar2(50),
	BreakMED		varchar2(50),
	SessionDURATION		datetime,
	foreign key (SongURI) references Songs(URI)
);


create table if not exists PainLog (
	SessionID		char(36),
	SessionTime			datetime,
	PainLVL			number(2),
	foreign key (SessionID) references Sessions(SessionID)
);



create table if not exists Patients (
	PatientID		varchar2(50) primary key,
	foreign key (PatientID) references Sessions(PatientID)
);









