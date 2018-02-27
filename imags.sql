-- IMAGS Data --

drop table Songs cascade constraints;

create table Songs (
	URI		varchar2(50),
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
	constraint Songs_pk primary key (URI)
);

-- Session ID naming convention is
-- 
drop table Sessions cascade constraints;

create table Sessions (
	SessionID		char(36),
	PatientID		varchar2(50),
	SongURI 	varchar2(50),
	BreakMED		varchar2(50),
	SessionDURATION		datetime,
	constraint Session_pk primary key (SessionID),
	constraint Song_fk foreign key (SongURI) references Songs (URI)
);

drop table PainLog cascade constraints;

create table PainLog (
	SessionID		char(36),
	SessionTime			datetime,
	PainLVL			number(2),
	constraint Pain_fk foreign key (SessionID) references Sessions (SessionID),
	constraint PainVal check (PainLVL between 0 and 10)	
);


-- Patient ID naming convention is
-- 
drop table Patients cascade constraints;

create table Patients (
	PatientID		varchar2(50),
	constraint Patient_pk primary key (PatientID),
	constraint Patient_fk foreign key (PatientID) references Sessions (PatientID)
);











