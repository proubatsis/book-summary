CREATE TABLE book (
	id SERIAL PRIMARY KEY,
	isbn TEXT UNIQUE NOT NULL,
	title TEXT NOT NULL,
	author TEXT NOT NULL,
	image TEXT,
	description TEXT
);

CREATE TABLE book_summary (
    id SERIAL PRIMARY KEY,
    book_id INTEGER REFERENCES book(id),
    summary TEXT NOT NULL
);

INSERT INTO book (isbn, title, author, description) VALUES
	('1234', 'Harry Potter', 'JK Rowling', 'https://i.jeded.com/i/harry-potter-and-the-goblet-of-fire.11171.jpg', 'Magic stuff!'),
	('5678', 'Sword of Truth', 'Terry Goodkind', 'https://upload.wikimedia.org/wikipedia/en/thumb/3/3b/The_Sword_of_Truth_cover.jpg/220px-The_Sword_of_Truth_cover.jpg', 'Richard - Seeker of Truth');


INSERT INTO book_summary (book_id, summary) VALUES
	(1, 'Harry does stuff to save the world'),
	(2, 'Richard Rahl goes on wonderful adventures'),
	(1, 'Harry kills Voldemort');
