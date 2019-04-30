Use moviedb;
DELIMITER $$
CREATE PROCEDURE addgenres(IN genrename varchar(100), IN movieId varchar(100)) 
BEGIN 
declare gid int(11);

select @genre_exist:=count(id) from moviedb.genres where name=genrename;
if @genre_exist=0
then
begin
select @movie_exist:=count(id) from moviedb.movies where id=movieId;
if @movie_exist=0
then
begin
select @genre_id:=Max(id) from moviedb.genres;
set @genre_id=@genre_id+1;
insert into moviedb.genres values(@genre_id, genrename);
insert into moviedb.genres_in_movies values(@genre_id, movieId);
end;
else
begin
select @genre_id:=Max(id) from moviedb.genres;
set @genre_id=@genre_id+1;
insert into moviedb.genres values(@genre_id, genrename);
insert into moviedb.genres_in_movies values(@genre_id,movieId);
end;
end if;
end;
else
begin
select @gid:=id from moviedb.genres where name=genrename;
select @combo_exist:=count(genreId) from moviedb.genres_in_movies where genreId=@gid and movieId=movieId;
if @combo_exist=0
then
begin
insert into moviedb.genres_in_movies values(@gid,movieId);
end;
end if;
end;
end if;
END
$$

DELIMITER ;

#Drop PROCEDURE addgenres;
