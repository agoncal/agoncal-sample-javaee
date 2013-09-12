INSERT INTO book(id, version, isbn, title, author, description, price, nbofpage, publisher) VALUES (1000, 0, '1234-5678', 'Beginning Java EE 6', 'Antonio Goncalves', 'Best Java EE book ever', 49, 450, 'APress')
INSERT INTO book(id, version, isbn, title, author, description, price, nbofpage, publisher) VALUES (1001, 0, '5678-9012', 'Beginning Java EE 7', 'Antonio Goncalves', 'No, this is the best ', 53, 550, 'APress')
INSERT INTO book(id, version, isbn, title, author, description, price, nbofpage, publisher) VALUES (1010, 0, '9012-3456', 'The Lord of the Rings', 'Tolkien', 'One ring to rule them all', 23, 222, 'Pinguin')

INSERT INTO speaker(id, version, firstname, surname, bio, twitter) VALUES (1000, 0, 'Antonio', 'Goncalves', 'Lorepsum', 'agoncal')
INSERT INTO speaker(id, version, firstname, surname, bio, twitter) VALUES (1001, 0, 'Arun', 'Gupta', 'Lorepsum', 'arungupta')

INSERT INTO talk(id, version, title, description, room) VALUES (1000, 0, 'Come and Play! with Java EE 7', 'Great Java EE 7 talk', 'Moscone')
INSERT INTO talk(id, version, title, description, room) VALUES (1001, 0, 'HOL Java EE 7', 'Great Java EE 7 HOL', 'Moscone')

INSERT INTO talk_speaker(talk_id, speakers_id) VALUES (1000, 1000)
INSERT INTO talk_speaker(talk_id, speakers_id) VALUES (1000, 1001)
INSERT INTO talk_speaker(talk_id, speakers_id) VALUES (1001, 1001)
