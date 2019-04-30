create database IF NOT EXISTS moviedb;
use moviedb;

-- movies table
create table if not exists movies(id varchar(10) NOT NULL PRIMARY KEY, 
								title varchar(100) NOT NULL, year integer NOT NULL, 
                                director varchar(100) NOT NULL);
                                
-- stars table
create table if not exists stars (id varchar(10) NOT NULL PRIMARY KEY, 
								  name varchar(100) NOT NULL, 
                                  birthyear integer);
                                  
-- stars_in_movies table                                  
create table if not exists stars_in_movies(starId varchar(10) not null, 
										movieId varchar(10) not null,
                                        Foreign Key (starId) REFERENCES stars(id) ON DELETE CASCADE ON UPDATE CASCADE, 
                                        FOREIGN KEY (movieId) REFERENCES movies(id) ON DELETE CASCADE ON UPDATE CASCADE);
                                        
-- genres table                                        
create table if not exists genres(id integer NOT NULL PRIMARY KEY AUTO_INCREMENT, 
							      name varchar(32) NOT NULL);
                                  
-- genres_in_movies table                                  
create table if not exists genres_in_movies(genreId integer NOT NULL, 
											movieId varchar(10) NOT NULL, 
                                            Foreign Key (genreId) REFERENCES genres(id) ON DELETE CASCADE ON UPDATE CASCADE, 
                                            FOREIGN KEY (movieId) REFERENCES movies(id) ON DELETE CASCADE ON UPDATE CASCADE);
                                            
-- creditcards table                                            
create table if not exists creditcards(id varchar(20) NOT NULL PRIMARY KEY, 
									   firstName varchar(20) NOT NULL, 
                                       lastName varchar(50) NOT NULL, 
                                       expiration DATE NOT NULL);
                                       
-- customers table                                       
create table if not exists customers(id integer NOT NULL PRIMARY KEY AUTO_INCREMENT, 
									 firstName varchar(50) NOT NULL, 
                                     lastName varchar(50) NOT NULL, 
                                     ccId varchar(20) NOT NULL, 
                                     address varchar(200) NOT NULL, 
                                     email varchar(50) NOT NULL, 
                                     password varchar(20) NOT NULL, 
                                     FOREIGN KEY (ccId) REFERENCES creditcards(id) ON DELETE CASCADE ON UPDATE CASCADE);
                                     
-- sales table                                     
create table if not exists sales(id integer NOT NULL PRIMARY KEY AUTO_INCREMENT, 
								 customerId integer NOT NULL, 
                                 movieId varchar(10) NOT NULL, 
                                 saleDate DATE NOT NULL, 
                                 FOREIGN KEY (customerId) REFERENCES customers(id) ON DELETE CASCADE ON UPDATE CASCADE, 
                                 FOREIGN KEY (movieId) REFERENCES movies(id) ON DELETE CASCADE ON UPDATE CASCADE);
                                 
-- ratings table                                 
create table if not exists ratings(movieId varchar(10) NOT NULL,
								   rating float, 
                                   numVotes integer NOT NULL, 
                                   FOREIGN KEY (movieId) REFERENCES movies(id) ON DELETE CASCADE ON UPDATE CASCADE);