create table if not exists table1
(
	id bigserial not null constraint table1_pk primary key,
	phone varchar(11) not null,
	created timestamp default CURRENT_TIMESTAMP not null
);

create unique index if not exists table1__i1
	on table1 (phone);

create table if not exists table2
(
	id bigserial not null constraint table2_pk primary key,
	phone varchar(10) not null,
	created timestamp default CURRENT_TIMESTAMP not null
);

create unique index if not exists table2__i1
	on table2 (phone);

create table if not exists requestlog
(
	id bigserial not null constraint requestlog_pk primary key,
	phone varchar(11) not null,
	requestid varchar2(20) not null,
	result varchar2(10),
	created timestamp default CURRENT_TIMESTAMP not null
);

