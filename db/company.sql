CREATE TABLE company
(
    id integer NOT NULL,
    name character varying,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE person
(
    id integer NOT NULL,
    name character varying,
    company_id integer references company(id),
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

insert into company (id, name) values(1, 'Horns and hooves');
insert into company (id, name) values(2, 'Муму и Герасим');
insert into company (id, name) values(3, 'Чип и Дейл');
insert into company (id, name) values(4, 'Пинки и Брейн');
insert into company (id, name) values(5, 'Джипперс и Крипперс');

insert into person (id, name, company_id) values(1, 'Остап Бендер', 1);
insert into person (id, name, company_id) values(2, 'Тургенев', 2);
insert into person (id, name, company_id) values(3, 'Уолт', 3);
insert into person (id, name, company_id) values(4, 'Дисней', 4);
insert into person (id, name, company_id) values(5, 'Бтольной человек', 5);
insert into person (id, name, company_id) values(6, 'ещё один', 5);
insert into person (id, name, company_id) values(7, 'Микки', 4);

/*
1. В одном запросе получить

- имена всех person, которые не состоят в компании с id = 5;

- название компании для каждого человека.
*/

select person.name as worker, company.name as job from person 
inner join company on company.id = person.company_id where
person.company_id != 5;

/*
2. Необходимо выбрать название компании с максимальным количеством человек + количество человек в этой компании.
Нужно учесть, что таких компаний может быть несколько.
*/

SELECT company.name, count(person.company_id) AS counter
FROM company
join person 
on person.company_id = company.id
group by company.name
having count(person.company_id) = (
SELECT MAX(mycount) 
FROM (
SELECT COUNT(person.company_id) AS mycount 
FROM person 
GROUP BY company_id) as maximum)