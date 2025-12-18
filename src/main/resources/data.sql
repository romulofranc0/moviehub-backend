-- Users
-- Password for all users is: 123456
-- Role 0 = USER, 1 = ADMIN

INSERT IGNORE INTO user (email, password, role, username) VALUES ('admin@moviehub.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd.g/nmynIy', 1, 'admin');
INSERT IGNORE INTO user (email, password, role, username) VALUES ('user@moviehub.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd.g/nmynIy', 0, 'user1');
INSERT IGNORE INTO user (email, password, role, username) VALUES ('alice@moviehub.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd.g/nmynIy', 0, 'alice');
INSERT IGNORE INTO user (email, password, role, username) VALUES ('bob@moviehub.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd.g/nmynIy', 0, 'bob');

-- Movies
-- 1. The Shawshank Redemption
INSERT IGNORE INTO movie (imdbid, title, year, director, genre, poster, plot, imdb_rating) VALUES 
('tt0111161', 'The Shawshank Redemption', '1994', 'Frank Darabont', 'Drama', 'https://m.media-amazon.com/images/M/MV5BNDE3ODCxYzMtY2YzZC00NmNlLWJiNDMtZDViZWM2MzIxZDYwXkEyXkFqcGdeQXVyNjAwNDUxODI@._V1_SX300.jpg', 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.', '9.3');

-- 2. The Godfather
INSERT IGNORE INTO movie (imdbid, title, year, director, genre, poster, plot, imdb_rating) VALUES 
('tt0068646', 'The Godfather', '1972', 'Francis Ford Coppola', 'Crime, Drama', 'https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg', 'The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.', '9.2');

-- 3. The Dark Knight
INSERT IGNORE INTO movie (imdbid, title, year, director, genre, poster, plot, imdb_rating) VALUES 
('tt0468569', 'The Dark Knight', '2008', 'Christopher Nolan', 'Action, Crime, Drama', 'https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_SX300.jpg', 'When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.', '9.0');

-- 4. Pulp Fiction
INSERT IGNORE INTO movie (imdbid, title, year, director, genre, poster, plot, imdb_rating) VALUES 
('tt0110912', 'Pulp Fiction', '1994', 'Quentin Tarantino', 'Crime, Drama', 'https://m.media-amazon.com/images/M/MV5BNGNhMDIzZTUtNTBlZi00MTRlLWFjM2ItYzViMjE3YzI5MjljXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg', 'The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.', '8.9');

-- 5. The Lord of the Rings: The Return of the King
INSERT IGNORE INTO movie (imdbid, title, year, director, genre, poster, plot, imdb_rating) VALUES 
('tt0167260', 'The Lord of the Rings: The Return of the King', '2003', 'Peter Jackson', 'Action, Adventure, Drama', 'https://m.media-amazon.com/images/M/MV5BNzA5ZDNlZKKtMTc2Yi00ZTg0LWIwMTYtZDMwMzMxNDZmYjFhXkEyXkFqcGdeQXVyNDMwMTU0Ng@@._V1_SX300.jpg', 'Gandalf and Aragorn lead the World of Men against Sauron''s army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.', '8.9');

-- 6. Fight Club
INSERT IGNORE INTO movie (imdbid, title, year, director, genre, poster, plot, imdb_rating) VALUES 
('tt0137523', 'Fight Club', '1999', 'David Fincher', 'Drama', 'https://m.media-amazon.com/images/M/MV5BMmEzNTkxYjQtZTc0MC00YTVjLTg5ZTEtZWMwOWVlYzY0NWIwXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg', 'An insomniac office worker and a devil-may-care soapmaker form an underground fight club that evolves into much more.', '8.8');

-- 7. Inception
INSERT IGNORE INTO movie (imdbid, title, year, director, genre, poster, plot, imdb_rating) VALUES 
('tt1375666', 'Inception', '2010', 'Christopher Nolan', 'Action, Adventure, Sci-Fi', 'https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_SX300.jpg', 'A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.', '8.8');

-- 8. The Matrix
INSERT IGNORE INTO movie (imdbid, title, year, director, genre, poster, plot, imdb_rating) VALUES 
('tt0133093', 'The Matrix', '1999', 'Lana Wachowski, Lilly Wachowski', 'Action, Sci-Fi', 'https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_SX300.jpg', 'A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.', '8.7');

-- 9. Goodfellas
INSERT INTO movie (imdbid, title, year, director, genre, poster, plot, imdb_rating) VALUES 
('tt0099685', 'Goodfellas', '1990', 'Martin Scorsese', 'Biography, Crime, Drama', 'https://m.media-amazon.com/images/M/MV5BY2NkZjYzM2ItM2YxZC00YzRkLTg4ZjEtNjg2YjI4NjU2OTY1XkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_SX300.jpg', 'The story of Henry Hill and his life in the mob, covering his relationship with his wife Karen Hill and his mob partners Jimmy Conway and Tommy DeVito in the Italian-American crime syndicate.', '8.7');

-- 10. Star Wars: Episode V - The Empire Strikes Back
INSERT INTO movie (imdbid, title, year, director, genre, poster, plot, imdb_rating) VALUES 
('tt0080684', 'Star Wars: Episode V - The Empire Strikes Back', '1980', 'Irvin Kershner', 'Action, Adventure, Fantasy', 'https://m.media-amazon.com/images/M/MV5BYmU1NDRjNDgtMzhiMi00NjZmLTg5NGItZDNiZjU5NTU4OTE0XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg', 'After the Rebels are brutally overpowered by the Empire on the ice planet Hoth, Luke Skywalker begins Jedi training with Yoda, while his friends are pursued by Darth Vader and a bounty hunter named Boba Fett throughout the galaxy.', '8.7');
