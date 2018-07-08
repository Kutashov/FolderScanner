CREATE TABLE IF NOT EXISTS entries (
    id serial PRIMARY KEY,
    content VARCHAR(1024),
    created TIMESTAMP
);


--DROP TABLE entries;

--select * from entries;