SET FOREIGN_KEY_CHECKS = 0;
drop table if exists Participants;
SET FOREIGN_KEY_CHECKS = 1;

create table if not exists Participants (
	ParticipantID varchar(50),
	primary key (ParticipantID)
);

SET FOREIGN_KEY_CHECKS = 0;
drop table if exists Sessions;
SET FOREIGN_KEY_CHECKS = 1;

create table if not exists Sessions (
	SessionID char(36),
	ParticipantID varchar(50),
	startTime datetime,
	SessionDURATION	int(5),
	primary key (sessionID),
	FOREIGN KEY (ParticipantID)
		REFERENCES Participants (ParticipantID)
		ON UPDATE CASCADE ON DELETE CASCADE
);

SET FOREIGN_KEY_CHECKS = 0;
drop table if exists Songs;
SET FOREIGN_KEY_CHECKS = 1;

create table if not exists Songs (
	URI varchar(50),
	SessionID char(36),
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
	Trackhref varchar(100),
	Valence float(5),
	primary key (URI),
	foreign key (SessionID) 
		references Sessions (SessionID)
		ON UPDATE CASCADE ON DELETE CASCADE
);

SET FOREIGN_KEY_CHECKS = 0;
drop table if exists PainLog;
SET FOREIGN_KEY_CHECKS = 1;

create table if not exists PainLog (
	SessionID char(36),
	TimeRecorded datetime,
	PainLVL int(2),
	FOREIGN KEY (SessionID) 
		REFERENCES Sessions (SessionID)
		ON UPDATE CASCADE ON DELETE CASCADE
);

SET FOREIGN_KEY_CHECKS = 0;
drop table if exists MedicationData;
SET FOREIGN_KEY_CHECKS = 1;

create table if not exists MedicationData (
	SessionID char(36),
	TookMed bool,
	Name varchar(50),
	Dosage varchar(100),
	FOREIGN KEY (SessionID)
		REFERENCES Sessions (SessionID)
		ON UPDATE CASCADE ON DELETE CASCADE
);













