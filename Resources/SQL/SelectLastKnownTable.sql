SSQLite
BBB

SELECT MAX(TimeOfMessage) AS TimeOfMessage 
FROM (
    %SELECT MAX(TimeOfMessage) AS TimeOfMessage FROM chatXXX
    where numberOFmessage is not null
	-F	UNION ALL -F %
);
