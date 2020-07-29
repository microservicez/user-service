CREATE TABLE USER (ID INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(45));
CREATE SEQUENCE user_id_seq;

INSERT INTO USER (ID, name) VALUES (user_id_seq.nextval, 'James Holden');
INSERT INTO USER (ID, name) VALUES (user_id_seq.nextval, 'Mike Ross');
