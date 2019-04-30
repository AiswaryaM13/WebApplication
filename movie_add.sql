Use moviedb;
DELIMITER $$
CREATE PROCEDURE movie_add(IN movietitle varchar(100), IN movieyear INT, IN moviedirector varchar(100), IN starname varchar(100), 
IN genrename varchar(32), INOUT param INT) 
BEGIN 
declare mid int(11);
declare gid int(11);
declare sid int(11);

select @movie_exist:=count(id) from moviedb.movies where title=movietitle and director=moviedirector and year=movieyear;
if @movie_exist=0
then
begin
set @movieid='';
select @movieid:=Max(id) from moviedb.movies;
set mid=substr(@movieid,4)+1;
set @movieid=CONCAT('tt0',mid);
insert into moviedb.movies values(@movieid, movietitle,movieyear,moviedirector);

set @genre_exist=0;
select @genreid:= Max(id) from moviedb.genres;
set gid=@genreid+1;
select @genre_exist:=count(id) from moviedb.genres where genres.name=genrename;


set @star_exist=0;
select @starid:= Max(id) from moviedb.stars;
set sid=substr(@starid,3)+1;
set @starid=CONCAT('nm',sid);
select @star_exist:=count(id) from moviedb.stars where stars.name=starname;

if @genre_exist=0
then
begin 
insert into moviedb.genres values(gid, genrename);
insert into moviedb.genres_in_movies values(gid,@movieid);
end;

else
begin

select @gnum:=id from moviedb.genres where genres.name=genrename;
insert into moviedb.genres_in_movies values(@gnum, @movieid);
end;
end if;

if @star_exist=0
then
begin
insert into moviedb.stars(id,name) VALUES(@starid,starname);
insert into moviedb.stars_in_movies VALUES(@starid, @movieid);
end;

else
begin
select @snum:=id from moviedb.stars where stars.name=starname;
insert into moviedb.stars_in_movies VALUES(@snum,@movieid);
end;

end if;

insert into moviedb.ratings values(@movieid,9.0,100);

set param=1;
end;

else
begin
set param=2;
end;
end if;


END
$$

DELIMITER ;

#Drop PROCEDURE movie_add;
#call movie_add('bahubali',2010,'Rajmouni','Prabhat','Action');

