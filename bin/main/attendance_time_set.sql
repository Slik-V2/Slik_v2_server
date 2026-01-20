INSERT IGNORE INTO attendance_time_set (
    today,
    session1_1_start,
    session1_1_end,
    session1_2_start,
    session1_2_end,
    session2_1_start,
    session2_1_end,
    session2_2_start,
    session2_2_end
)
VALUES (
           CURDATE(),
           '21:00:00',
           '21:15:00',
           '21:50:00',
           '23:15:00',
           '23:40:00',
           '23:00:00',
           '23:50:00',
           '23:59:00'
       );