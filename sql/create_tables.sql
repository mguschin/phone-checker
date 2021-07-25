create table if not exists table1
(
	id bigserial not null constraint table1_pk primary key,
	phone varchar(11) not null,
	created timestamp default CURRENT_TIMESTAMP not null
);

alter table table1 owner to postgres;

create unique index if not exists table1__i1
	on table1 (phone);

create table if not exists table2
(
	id bigserial not null constraint table2_pk primary key,
	phone varchar(10) not null,
	created timestamp default CURRENT_TIMESTAMP not null
);

alter table table2 owner to postgres;

create unique index if not exists table2__i1
	on table2 (phone);

INSERT INTO table1 (phone) VALUES ('79876543211');
INSERT INTO table1 (phone) VALUES ('79876543213');

INSERT INTO table2 (phone) VALUES ('9876543213');
INSERT INTO table2 (phone) VALUES ('9876543212');

commit;