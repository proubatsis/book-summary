CREATE TABLE book (
	id SERIAL PRIMARY KEY,
	isbn TEXT NOT NULL,
	title TEXT NOT NULL,
	description TEXT,
	summary TEXT
);
