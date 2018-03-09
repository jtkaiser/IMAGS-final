
<?php
$user_name = "root";
$password = "";
$server = "localhost";
$db_name = "imags";

$con = mysqli_connect($server,$user_name,$password,$db_name);
if($con)
{
	$query = '';
	if($_POST['updateType'] == 'participant'){
		$participantID = $_POST['participantID'];
		$query = "replace into Participants (ParticipantID) values('".$participantID."');";
	}
	else if($_POST['updateType'] == 'session'){
		$sessionID = $_POST['sessionID'];
		$participantID = $_POST['participantID'];
		$startTime = $_POST['startTime'];
		$sessionDURATION = $_POST['sessionDuration'];
		$query = "REPLACE INTO Sessions (SessionID, ParticipantID, startTime, sessionDURATION)
		VALUES ('$sessionID', '$participantID', '$startTime', '$sessionDURATION');";
	}
	else if($_POST['updateType'] == 'song'){
		$URI = $_POST['URI'];
		$SessionID = $_POST['sessionID'];
		$Acousticness = $_POST['acousticness'];
		$AnalysisURL = $_POST['analysisURL'];
		$Danceability = $_POST['danceability'];
		$DurationMS = $_POST['durationMS'];
		$Energy = $_POST['energy'];
		$SongID = $_POST['songID'];
		$Instrumentalness = $_POST['instrumentalness'];
		$SongKey = $_POST['songKey'];
		$Liveness = $_POST['liveness'];
		$Loudness = $_POST['loudness'];
		$SongMode = $_POST['songMode'];
		$Speechiness = $_POST['speechiness'];
		$Tempo = $_POST['tempo'];
		$TimeSignature = $_POST['timeSignature'];
		$Trackhref = $_POST['trackhref'];
		$Valence = $_POST['valence'];
		$query = "REPLACE INTO Songs (URI, SessionID, Acousticness, AnalysisURL, Danceability,
		DurationMS, Energy, SongID, Instrumentalness, SongKey, Liveness, Loudness, SongMode,
		Speechiness, Tempo, TimeSignature, Trackhref, Valence)
		VALUES ('$URI', '$SessionID', '$Acousticness', '$AnalysisURL', '$Danceability',
		'$DurationMS', '$Energy', '$SongID', '$Instrumentalness', '$SongKey', '$Liveness', '$Loudness', '$SongMode',
		'$Speechiness', '$Tempo', '$TimeSignature', '$Trackhref', '$Valence');";
	}
	else if($_POST['updateType'] == 'painlog'){
		$sessionID = $_POST['sessionID'];
		$timeRecorded = $_POST['timeRecorded'];
		$painLVL = $_POST['painLVL'];
		$query = "REPLACE INTO PainLog (SessionID, TimeRecorded, PAinLVL)
		VALUES ('$sessionID', '$timeRecorded', '$painLVL');";
	}
	else if($_POST['updateType'] == 'med_data'){
		$sessionID = $_POST['sessionID'];
		$tookMed = $_POST['tookMed'];
		$name = $_POST['name'];
		$dosage = $_POST['dosage'];
		$query = "REPLACE INTO MedicationData (SessionID, TookMed, Name, Dosage)
		VALUES ('$sessionID', '$tookMed', '$name', '$dosage');";
	}
	
	
	
	
	$result = mysqli_query($con,$query);
	if($result)
	{
		$status = 'OK';
		
		
	}
	else
	{
		$status = 'FAILED';
		
		
	}
	
}

else { $status = 'FAILED'; }

	echo json_encode(array("response"=>$status));
	

mysqli_close($con);

?>