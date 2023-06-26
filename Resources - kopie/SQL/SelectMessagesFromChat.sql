SSQLite
BBB
SELECT 
    a.chatName AS SenderName,
    c.senderUUID,
    c.message,
    c.TimeOfMessage,
    c.numberOFmessage,
    c.MessageUUID
FROM 
    (
    SELECT 
        userUUID AS senderUUID,
        message,
        TimeOfMessage,
        numberOFmessage,
        MessageUUID
    FROM chatXXX
    ORDER BY TimeOfMessage DESC
    -- LIMIT 15
    ) AS c
left JOIN AdministrationXXX AS a 
ON a.userUUID = c.senderUUID
ORDER BY c.TimeOfMessage DESC;
