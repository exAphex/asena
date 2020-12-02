BEGIN;

CREATE SEQUENCE public.users_sequence;

CREATE TABLE IF NOT EXISTS public.users (
	user_id bigint NOT NULL,
	department character varying(255),
	first_name character varying(255),
	last_name character varying(255),
	user_name character varying(255),
	PRIMARY KEY(user_id)
);

INSERT INTO public.users (user_id, department, first_name, last_name, user_name) VALUES(nextval('public.users_sequence'), 'SAP Security F&E', 'Aydin', 'Tekin', 'atekin');
INSERT INTO public.users (user_id, department, first_name, last_name, user_name) VALUES(nextval('public.users_sequence'), 'SAP Security Consulting', 'Marcus', 'Schleppe', 'mschleppe');
INSERT INTO public.users (user_id, department, first_name, last_name, user_name) VALUES(nextval('public.users_sequence'), 'SAP Security Customer Success', 'Tobias', 'Mehlhorn', 'tmehlhorn');

COMMIT;