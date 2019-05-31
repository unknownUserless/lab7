/*CREATE TYPE profession AS ENUM (
'MECHANIC',
'PROFESSOR',
'ASTRONOMER',
'ENGINEER',
'ARCHITECT',
'ARTIST',
'MUSICIAN',
'DOCTOR'
);*/
/*
CREATE TYPE location AS ENUM (
  'EARTH',
  'MOON',
  'MARS'
);
*/
/*CREATE TABLE IF NOT EXISTS humans(
  id SERIAL PRIMARY KEY ,
  name TEXT NOT NULL ,
  profession profession
);*/

/*INSERT INTO humans(name, profession) VALUES
  ('Sam', 'MECHANIC'),
  ('John', 'ASTRONOMER'),
  ('Jack', 'DOCTOR'),
  ('Michel', 'ARTIST'),
  ('Tom', 'ENGINEER');
*/


CREATE TABLE IF NOT EXISTS users (
  email VARCHAR NOT NULL  ,
  login VARCHAR PRIMARY KEY NOT NULL ,
  password VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS squads(
  name VARCHAR PRIMARY KEY NOT NULL ,
  members VARCHAR[] NOT NULL  ,
  birthday VARCHAR ,
  location VARCHAR NOT NULL ,
  owner VARCHAR REFERENCES users (login)
);

/*owner TEXT REFERENCES users(login)*/