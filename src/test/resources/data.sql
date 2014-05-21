INSERT INTO session(title, presenter, conference, session_date, active, number) VALUES ('Introduction To BlueMix', 'Ryan Baxter', 'BlueMix Meetup', '2014-05-06 00:00:00', true, '+16176761757')
INSERT INTO session(title, presenter, conference, session_date, active, number) VALUES ('Introduction To BlueMix', 'Ryan Baxter', 'QCON', '2014-05-06 00:00:00', false, '+6174354714')

INSERT INTO question(content, email, number, session_id, answered) VALUES('How do I deploy an app?', 'rjbaxter@us.ibm.com', '+6174354714', 2, false)