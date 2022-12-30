create table courses(
	courseId integer(3) primary key,
    courseName varchar(20) unique,
    duration integer(2),
    fee integer(3) check(fee between 100 and 500)
    );
    
select * from courses;

create table students(
	sid integer(5) primary key,
    sname varchar(20) not null,
    age integer(2) check(age between 15 and 30),
    doj datetime default now(),
    doc datetime,
    courseId integer(3),
    foreign key(courseId) references courses(courseId) on delete cascade
);

select * from students;