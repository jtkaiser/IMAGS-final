create table if not exists Songs (
	URI varchar(50) primary key,
	Acousticness float(5),
	AnalysisURL varchar(100),
	Danceability float(5),
	DurationMS int(10),
	Energy float(5),
	SongID varchar(50),
	Instrumentalness float(5),
	SongKey int(1),
	Liveness float(5),
	Loudness float(5),
	SongMode int(1),
	Speechiness float(5),
	Tempo float(5),
	TimeSignature int(5),
	TrackHref varchar(100),
	Valence float(5),
	Progressions varchar(50),
);


create table if not exists Sessions (
	SessionID char(36) primary key,
	PatientID varchar(50),
	SongURI varchar(50),
	BreakMED varchar(50),
	SessionDURATION	datetime,
	foreign key (SongURI) references Songs(URI)
);


create table if not exists PainLog (
	SessionID char(36),
	SessionTime datetime,
	PainLVL int(2),
	foreign key (SessionID) references Sessions(SessionID)
);



create table if not exists Patients (
	PatientID varchar(50) primary key,
	foreign key (PatientID) references Sessions(PatientID)
);









