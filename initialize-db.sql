CREATE TABLE book (
	id SERIAL PRIMARY KEY,
	title TEXT NOT NULL,
	author TEXT NOT NULL,
	image TEXT NOT NULL,
	description TEXT NOT NULL
);

CREATE TABLE booksum_user (
	id SERIAL PRIMARY KEY,
	email TEXT UNIQUE NOT NULL,
	PASSWORD TEXT NOT NULL
);

CREATE TABLE account (
	id SERIAL PRIMARY KEY,
	username TEXT UNIQUE NOT NULL,
	user_id INTEGER REFERENCES booksum_user(id)
);

CREATE TABLE book_summary (
    id SERIAL PRIMARY KEY,
    book_id INTEGER REFERENCES book(id),
    account_id INTEGER REFERENCES account(id),
    posted_date TIMESTAMP NOT NULL,
    summary TEXT NOT NULL
);

CREATE TABLE book_external_mapping(
	id SERIAL PRIMARY KEY,
	external_id TEXT UNIQUE NOT NULL,
	book_id INTEGER REFERENCES book(id)
);
