SSQLite
BBB
select QuickMessage,TimeOfMessage,chatUUID
FROM (
    %
SELECT 
    m.TimeOfMessage,
    "XXX" as chatUUID, 
    SUBSTR((SELECT chatName FROM AdministrationXXX WHERE userUUID=m.userUUID)
    ||":  "||m.message, 1, 30) as "QuickMessage"
FROM 
    (SELECT * FROM chatXXX WHERE TimeOfMessage = 
        (SELECT MAX(TimeOfMessage) FROM chatXXX)) as m
	-F	UNION ALL -F %
);
order by TimeOfMessage desc;
