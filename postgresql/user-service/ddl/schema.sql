CREATE SCHEMA IF NOT EXISTS app;

CREATE TABLE IF NOT EXISTS app.users
(
    uuid uuid NOT NULL,
    dt_create timestamp(3) without time zone,
    dt_update timestamp(3) without time zone,
    mail character varying(255),
    fio character varying(255),
    role character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    password character varying(255),
    CONSTRAINT users_pkey PRIMARY KEY (uuid),
    CONSTRAINT users_mail_unique UNIQUE (mail)
);

CREATE TABLE app.verification
(
    uuid uuid NOT NULL,
    mail text NOT NULL,
    dt_create timestamp without time zone NOT NULL,
    CONSTRAINT verification_pkey PRIMARY KEY (uuid),
    CONSTRAINT verification_mail_unique UNIQUE (mail)
)