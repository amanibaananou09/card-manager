-- public.cm_user_history definition

-- Drop table

-- DROP TABLE public.cm_user_history;

CREATE TABLE public.cm_user_history (
                                        activity_date timestamp(6) NULL,
                                        id int8 NOT NULL,
                                        user_id int8 NULL,
                                        "version" int8 NULL,
                                        activity_type varchar(255) NULL,
                                        ip_address varchar(255) NULL,
                                        request_handler varchar(255) NULL,
                                        requesturi varchar(255) NULL,
                                        CONSTRAINT cm_user_history_activity_type_check CHECK (((activity_type)::text = ANY ((ARRAY['POST'::character varying, 'PUT'::character varying, 'DELETE'::character varying, 'PATCH'::character varying, 'GET'::character varying])::text[]))),
	CONSTRAINT cm_user_history_pkey PRIMARY KEY (id)
);


-- public.cm_account definition

-- Drop table

-- DROP TABLE public.cm_account;

CREATE TABLE public.cm_account (
                                   actif bool NULL,
                                   created_by_id int8 NULL,
                                   created_date timestamp(6) NULL,
                                   customer_id int8 NULL,
                                   date_status_change timestamp(6) NOT NULL,
                                   id int8 NOT NULL,
                                   last_modified_by_id int8 NULL,
                                   last_modified_date timestamp(6) NULL,
                                   "version" int8 NULL,
                                   code varchar(255) NULL,
                                   description varchar(255) NULL,
                                   "label" varchar(255) NULL,
                                   CONSTRAINT cm_account_pkey PRIMARY KEY (id)
);


-- public.cm_authorization definition

-- Drop table

-- DROP TABLE public.cm_authorization;

CREATE TABLE public.cm_authorization (
                                         amount numeric(38, 2) NULL,
                                         quantity numeric(38, 2) NULL,
                                         card_id int8 NULL,
                                         created_by_id int8 NULL,
                                         created_date timestamp(6) NULL,
                                         date_time timestamp(6) NULL,
                                         id int8 NOT NULL,
                                         last_modified_by_id int8 NULL,
                                         last_modified_date timestamp(6) NULL,
                                         "version" int8 NULL,
                                         reference varchar(255) NULL,
                                         CONSTRAINT cm_authorization_pkey PRIMARY KEY (id),
                                         CONSTRAINT cm_authorization_reference_key UNIQUE (reference)
);


-- public.cm_card definition

-- Drop table

-- DROP TABLE public.cm_card;

CREATE TABLE public.cm_card (
                                actif bool NULL,
                                "ceiling" numeric(38, 2) NULL,
                                account_id int8 NULL,
                                created_by_id int8 NULL,
                                created_date timestamp(6) NULL,
                                date_status_change timestamp(6) NOT NULL,
                                id int8 NOT NULL,
                                last_modified_by_id int8 NULL,
                                last_modified_date timestamp(6) NULL,
                                "version" int8 NULL,
                                code varchar(255) NULL,
                                status varchar(255) NULL,
                                "type" varchar(255) NULL,
                                CONSTRAINT cm_card_pkey PRIMARY KEY (id),
                                CONSTRAINT cm_card_status_check CHECK (((status)::text = ANY ((ARRAY['FREE'::character varying, 'BLOCKED'::character varying, 'IN_USE'::character varying, 'LOST'::character varying])::text[]))),
	CONSTRAINT cm_card_type_check CHECK (((type)::text = ANY ((ARRAY['QUANTITY'::character varying, 'AMOUNT'::character varying])::text[])))
);


-- public.cm_customer definition

-- Drop table

-- DROP TABLE public.cm_customer;

CREATE TABLE public.cm_customer (
                                    actif bool NULL,
                                    created_by_id int8 NULL,
                                    created_date timestamp(6) NULL,
                                    date_status_change timestamp(6) NOT NULL,
                                    id int8 NOT NULL,
                                    last_modified_by_id int8 NULL,
                                    last_modified_date timestamp(6) NULL,
                                    parent_id int8 NULL,
                                    "version" int8 NULL,
                                    "name" varchar(255) NOT NULL,
                                    CONSTRAINT cm_customer_pkey PRIMARY KEY (id)
);


-- public.cm_movement definition

-- Drop table

-- DROP TABLE public.cm_movement;

CREATE TABLE public.cm_movement (
                                    amount numeric(38, 2) NULL,
                                    account_id int8 NULL,
                                    created_by_id int8 NULL,
                                    created_date timestamp(6) NULL,
                                    date_time timestamp(6) NULL,
                                    id int8 NOT NULL,
                                    last_modified_by_id int8 NULL,
                                    last_modified_date timestamp(6) NULL,
                                    "version" int8 NULL,
                                    "type" varchar(255) NULL,
                                    CONSTRAINT cm_movement_pkey PRIMARY KEY (id),
                                    CONSTRAINT cm_movement_type_check CHECK (((type)::text = ANY ((ARRAY['BANK_TRANSFER'::character varying, 'CARD_PAYMENT'::character varying, 'CASH'::character varying])::text[])))
);


-- public.cm_product definition

-- Drop table

-- DROP TABLE public.cm_product;

CREATE TABLE public.cm_product (
                                   actif bool NULL,
                                   created_by_id int8 NULL,
                                   created_date timestamp(6) NULL,
                                   date_status_change timestamp(6) NOT NULL,
                                   id int8 NOT NULL,
                                   last_modified_by_id int8 NULL,
                                   last_modified_date timestamp(6) NULL,
                                   "version" int8 NULL,
                                   code varchar(255) NULL,
                                   "name" varchar(255) NULL,
                                   CONSTRAINT cm_product_pkey PRIMARY KEY (id)
);


-- public.cm_transaction definition

-- Drop table

-- DROP TABLE public.cm_transaction;

CREATE TABLE public.cm_transaction (
                                       amount numeric(38, 2) NULL,
                                       quantity numeric(38, 2) NULL,
                                       authorization_id int8 NULL,
                                       card_id int8 NULL,
                                       created_by_id int8 NULL,
                                       created_date timestamp(6) NULL,
                                       date_time timestamp(6) NULL,
                                       id int8 NOT NULL,
                                       last_modified_by_id int8 NULL,
                                       last_modified_date timestamp(6) NULL,
                                       product_id int8 NULL,
                                       "version" int8 NULL,
                                       CONSTRAINT cm_transaction_pkey PRIMARY KEY (id)
);


-- public.cm_user definition

-- Drop table

-- DROP TABLE public.cm_user;

CREATE TABLE public.cm_user (
                                actif bool NULL,
                                created_by_id int8 NULL,
                                created_date timestamp(6) NULL,
                                customer_id int8 NULL,
                                date_status_change timestamp(6) NOT NULL,
                                id int8 NOT NULL,
                                last_connection_date timestamp(6) NULL,
                                last_modified_by_id int8 NULL,
                                last_modified_date timestamp(6) NULL,
                                "version" int8 NULL,
                                tag varchar(255) NULL,
                                username varchar(255) NULL,
                                CONSTRAINT cm_user_pkey PRIMARY KEY (id)
);


-- public.cm_account foreign keys

ALTER TABLE public.cm_account ADD CONSTRAINT fk1n91vd48nrb8t0m4bf9j0kulj FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id);
ALTER TABLE public.cm_account ADD CONSTRAINT fkgvkuxhk9ivlbgi9wf6gd4otur FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id);
ALTER TABLE public.cm_account ADD CONSTRAINT fkhm5disd47dx1cbh7t77wbs3j FOREIGN KEY (customer_id) REFERENCES public.cm_customer(id);


-- public.cm_authorization foreign keys

ALTER TABLE public.cm_authorization ADD CONSTRAINT fk4j1qtk8yrhqdp1wpalv85vf4y FOREIGN KEY (card_id) REFERENCES public.cm_card(id);
ALTER TABLE public.cm_authorization ADD CONSTRAINT fklm6y5ip8edaskpjbnq0brf9qj FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id);
ALTER TABLE public.cm_authorization ADD CONSTRAINT fkloq5vly055na6nmu52ww24gca FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id);


-- public.cm_card foreign keys

ALTER TABLE public.cm_card ADD CONSTRAINT fket8nqg1arn4pagekeu48swm05 FOREIGN KEY (account_id) REFERENCES public.cm_account(id);
ALTER TABLE public.cm_card ADD CONSTRAINT fkrlwnskvctmn3hs8k5dfv35lbr FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id);
ALTER TABLE public.cm_card ADD CONSTRAINT fktag6k0whipyxfajhcu19k3oig FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id);


-- public.cm_customer foreign keys

ALTER TABLE public.cm_customer ADD CONSTRAINT fkgnflmvtqxy9efy5oyt45vk9td FOREIGN KEY (parent_id) REFERENCES public.cm_customer(id);
ALTER TABLE public.cm_customer ADD CONSTRAINT fkh2qb78gtdqsxhfrfihsgu2dja FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id);
ALTER TABLE public.cm_customer ADD CONSTRAINT fkh5fujhy63xwxxxdbfthkoppa5 FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id);


-- public.cm_movement foreign keys

ALTER TABLE public.cm_movement ADD CONSTRAINT fk10a2dkkqrf677ltbmw4pa2sj8 FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id);
ALTER TABLE public.cm_movement ADD CONSTRAINT fk4hpgu37cqwm41v96d9i7bhdmr FOREIGN KEY (account_id) REFERENCES public.cm_account(id);
ALTER TABLE public.cm_movement ADD CONSTRAINT fkeq8ur2t4rtbovcix20xgjeq04 FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id);


-- public.cm_product foreign keys

ALTER TABLE public.cm_product ADD CONSTRAINT fk1dnkwirn6wgw7p2ugtrjegehs FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id);
ALTER TABLE public.cm_product ADD CONSTRAINT fkfy753i6xlfku1tqragemoiuu3 FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id);


-- public.cm_transaction foreign keys

ALTER TABLE public.cm_transaction ADD CONSTRAINT fk2mwfk67uwa8d9xvvh63pfvjhb FOREIGN KEY (product_id) REFERENCES public.cm_product(id);
ALTER TABLE public.cm_transaction ADD CONSTRAINT fk7e6cdlxxapdhirrndllb5s922 FOREIGN KEY (authorization_id) REFERENCES public.cm_authorization(id);
ALTER TABLE public.cm_transaction ADD CONSTRAINT fkel96ojcf4exmagphnrfcdrxyw FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id);
ALTER TABLE public.cm_transaction ADD CONSTRAINT fknf8xlg2imv2cr1es7fc1vx13s FOREIGN KEY (card_id) REFERENCES public.cm_card(id);
ALTER TABLE public.cm_transaction ADD CONSTRAINT fksbtb2s0bls33759wyqw5d5oc5 FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id);


-- public.cm_user foreign keys

ALTER TABLE public.cm_user ADD CONSTRAINT fk239ih55kafmdkfnh9osf7so0t FOREIGN KEY (customer_id) REFERENCES public.cm_customer(id);
ALTER TABLE public.cm_user ADD CONSTRAINT fk5xio9fvjt7swyijy3jde85af6 FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id);
ALTER TABLE public.cm_user ADD CONSTRAINT fkbrcwokmrrtadj9qrt5l1jepp FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id);


-- public.cm_account_seq definition

-- DROP SEQUENCE public.cm_account_seq;

CREATE SEQUENCE public.cm_account_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;


-- public.cm_authorization_seq definition

-- DROP SEQUENCE public.cm_authorization_seq;

CREATE SEQUENCE public.cm_authorization_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;


-- public.cm_card_seq definition

-- DROP SEQUENCE public.cm_card_seq;

CREATE SEQUENCE public.cm_card_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;


-- public.cm_customer_seq definition

-- DROP SEQUENCE public.cm_customer_seq;

CREATE SEQUENCE public.cm_customer_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;


-- public.cm_movement_seq definition

-- DROP SEQUENCE public.cm_movement_seq;

CREATE SEQUENCE public.cm_movement_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;


-- public.cm_product_seq definition

-- DROP SEQUENCE public.cm_product_seq;

CREATE SEQUENCE public.cm_product_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;


-- public.cm_transaction_seq definition

-- DROP SEQUENCE public.cm_transaction_seq;

CREATE SEQUENCE public.cm_transaction_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;


-- public.cm_user_history_seq definition

-- DROP SEQUENCE public.cm_user_history_seq;

CREATE SEQUENCE public.cm_user_history_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;


-- public.cm_user_seq definition

-- DROP SEQUENCE public.cm_user_seq;

CREATE SEQUENCE public.cm_user_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
