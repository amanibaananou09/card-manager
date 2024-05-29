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


-- public.cm_user definition

-- Drop table

-- DROP TABLE public.cm_user;

CREATE TABLE public.cm_user (
                                actif bool NULL,
                                created_by_id int8 NULL,
                                created_date timestamp(6) NULL,
                                date_status_change timestamp(6) NOT NULL,
                                id int8 NOT NULL,
                                last_connection_date timestamp(6) NULL,
                                last_modified_by_id int8 NULL,
                                last_modified_date timestamp(6) NULL,
                                "version" int8 NULL,
                                username varchar(255) NULL,
                                CONSTRAINT cm_user_pkey PRIMARY KEY (id),
                                CONSTRAINT fk5xio9fvjt7swyijy3jde85af6 FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id),
                                CONSTRAINT fkbrcwokmrrtadj9qrt5l1jepp FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id)
);




-- public.cm_ceiling definition

-- Drop table

-- DROP TABLE public.cm_ceiling;

CREATE TABLE public.cm_ceiling (
                                   id int8 NOT NULL,
                                   created_by_id int8 NULL,
                                   created_date timestamp(6) NULL,
                                   last_modified_by_id int8 NULL,
                                   last_modified_date timestamp(6) NULL,
                                   "version" int8 NULL,
                                   counter_type varchar(31) NOT NULL,
                                   "name" varchar(255) NULL,
                                   value numeric(38, 2) NULL,
                                   daily_limit_value numeric(38, 2) NULL,
                                   condition varchar(255) NULL,
                                   ceiling_type varchar(255) NULL,
                                   limit_type varchar(255) NULL,
                                   CONSTRAINT cm_ceiling_pkey PRIMARY KEY (id),
                                   CONSTRAINT fk_ceiling_last_modified_by_id FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id),
                                   CONSTRAINT fk_ceiling_created_by_id FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id)
);




CREATE TABLE public.cm_bonus (
                                   id int8 NOT NULL,
                                   created_by_id int8 NULL,
                                   created_date timestamp(6) NULL,
                                   last_modified_by_id int8 NULL,
                                   last_modified_date timestamp(6) NULL,
                                   "version" int8 NULL,
                                   counter_type varchar(31) NOT NULL,
                                   "name" varchar(255) NULL,
                                   value numeric(38, 2) NULL,
                                   daily_limit_value numeric(38, 2) NULL,
                                   CONSTRAINT cm_bonus_pkey PRIMARY KEY (id),
                                   CONSTRAINT fk_bonus_created_by FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id),
                                   CONSTRAINT fk_bonus_last_modified_by FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id)
);



-- public.cm_country definition

-- Drop table

-- DROP TABLE public.cm_country;

CREATE TABLE public.cm_country (
                                   created_by_id int8 NULL,
                                   created_date timestamp(6) NULL,
                                   id int8 NOT NULL,
                                   last_modified_by_id int8 NULL,
                                   last_modified_date timestamp(6) NULL,
                                   "version" int8 NULL,
                                   code varchar(255) NULL,
                                   "name" varchar(255) NULL,
                                   CONSTRAINT cm_country_pkey PRIMARY KEY (id),
                                   CONSTRAINT fk6jpcnc6k1jg81sp6rw47t8uv1 FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id),
                                   CONSTRAINT fkoybinp5vr0sil9skqb0k1bbpo FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id)
);


-- public.cm_supplier definition

-- Drop table

-- DROP TABLE public.cm_supplier;

CREATE TABLE public.cm_supplier (
                                    actif bool NULL,
                                    created_by_id int8 NULL,
                                    created_date timestamp(6) NULL,
                                    date_status_change timestamp(6) NOT NULL,
                                    id int8 NOT NULL,
                                    last_modified_by_id int8 NULL,
                                    last_modified_date timestamp(6) NULL,
                                    "version" int8 NULL,
                                    address varchar(255) NULL,
                                    "name" varchar(255) NULL,
                                    origin varchar(255) NULL,
                                    phone varchar(255) NULL,
                                    reference varchar(255) NULL,
                                    CONSTRAINT cm_supplier_origin_check CHECK (((origin)::text = ANY ((ARRAY['CM'::character varying, 'SX'::character varying])::text[]))),
	CONSTRAINT cm_supplier_pkey PRIMARY KEY (id),
	CONSTRAINT fk4fyqqlhj88iolyuibskmibvpm FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id),
	CONSTRAINT fknb1gnie4ibeo4xeaoyif1fpem FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id)
);


-- public.cm_supplier_users definition

-- Drop table

-- DROP TABLE public.cm_supplier_users;

CREATE TABLE public.cm_supplier_users (
                                          supplier_id int8 NOT NULL,
                                          user_id int8 NOT NULL,
                                          CONSTRAINT cm_supplier_users_pkey PRIMARY KEY (supplier_id, user_id),
                                          CONSTRAINT cm_supplier_users_user_id_key UNIQUE (user_id),
                                          CONSTRAINT fk863mylq2ey7xtqyj1jd02qatd FOREIGN KEY (supplier_id) REFERENCES public.cm_supplier(id),
                                          CONSTRAINT fkdm4xhtc60bdnxmna6luejq95c FOREIGN KEY (user_id) REFERENCES public.cm_user(id)
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
                                    supplier_id int8 NULL,
                                    "version" int8 NULL,
                                    "name" varchar(255) NOT NULL,
                                    CONSTRAINT cm_customer_pkey PRIMARY KEY (id),
                                    CONSTRAINT fkh2qb78gtdqsxhfrfihsgu2dja FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id),
                                    CONSTRAINT fkh5fujhy63xwxxxdbfthkoppa5 FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id),
                                    CONSTRAINT fklvjs2u1hu320gu9sl295w5a7i FOREIGN KEY (supplier_id) REFERENCES public.cm_supplier(id)
);


-- public.cm_customer_users definition

-- Drop table

-- DROP TABLE public.cm_customer_users;

CREATE TABLE public.cm_customer_users (
                                          customer_id int8 NOT NULL,
                                          user_id int8 NOT NULL,
                                          CONSTRAINT cm_customer_users_pkey PRIMARY KEY (customer_id, user_id),
                                          CONSTRAINT cm_customer_users_user_id_key UNIQUE (user_id),
                                          CONSTRAINT fk4rd05ytkhews2qxc9948qjqak FOREIGN KEY (user_id) REFERENCES public.cm_user(id),
                                          CONSTRAINT fkd3flmmm6fyqkpwotsms1t86l FOREIGN KEY (customer_id) REFERENCES public.cm_customer(id)
);


-- public.cm_product definition

-- Drop table

-- DROP TABLE public.cm_product;

CREATE TABLE public.cm_product (
                                   created_by_id int8 NULL,
                                   created_date timestamp(6) NULL,
                                   id int8 NOT NULL,
                                   last_modified_by_id int8 NULL,
                                   last_modified_date timestamp(6) NULL,
                                   supplier_id int8 NULL,
                                   "version" int8 NULL,
                                   code varchar(255) NULL,
                                   "name" varchar(255) NULL,
                                   CONSTRAINT cm_product_pkey PRIMARY KEY (id),
                                   CONSTRAINT fk1dnkwirn6wgw7p2ugtrjegehs FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id),
                                   CONSTRAINT fkfy753i6xlfku1tqragemoiuu3 FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id),
                                   CONSTRAINT fkm6a81no8xe5b6nqokyie2009b FOREIGN KEY (supplier_id) REFERENCES public.cm_supplier(id)
);


-- public.cm_sale_point definition

-- Drop table

-- DROP TABLE public.cm_sale_point;

CREATE TABLE public.cm_sale_point (
                                      country_id int8 NULL,
                                      created_by_id int8 NULL,
                                      created_date timestamp(6) NULL,
                                      id int8 NOT NULL,
                                      last_modified_by_id int8 NULL,
                                      last_modified_date timestamp(6) NULL,
                                      supplier_id int8 NULL,
                                      "version" int8 NULL,
                                      area varchar(255) NULL,
                                      city varchar(255) NULL,
                                      "name" varchar(255) NULL,
                                      CONSTRAINT cm_sale_point_pkey PRIMARY KEY (id),
                                      CONSTRAINT fkb6sarcq140kd8cwywxyvgv8ev FOREIGN KEY (country_id) REFERENCES public.cm_country(id),
                                      CONSTRAINT fki9eemkfmjuqb77i4cwf38yng6 FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id),
                                      CONSTRAINT fkkn8hoojlmlie9lanl8xmtfur1 FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id),
                                      CONSTRAINT fksgmt1a5v8hkqs94525rkewvd FOREIGN KEY (supplier_id) REFERENCES public.cm_supplier(id)
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
                                   CONSTRAINT cm_account_pkey PRIMARY KEY (id),
                                   CONSTRAINT fk1n91vd48nrb8t0m4bf9j0kulj FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id),
                                   CONSTRAINT fkgvkuxhk9ivlbgi9wf6gd4otur FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id),
                                   CONSTRAINT fkhm5disd47dx1cbh7t77wbs3j FOREIGN KEY (customer_id) REFERENCES public.cm_customer(id)
);


-- public.cm_card_group definition

-- Drop table

-- DROP TABLE public.cm_card_group;

CREATE TABLE public.cm_card_group (
                                      actif bool NULL,
                                      created_by_id int8 NULL,
                                      created_date timestamp(6) NULL,
                                      customer_id int8 NULL,
                                      date_status_change timestamp(6) NOT NULL,
                                      id int8 NOT NULL,
                                      last_modified_by_id int8 NULL,
                                      last_modified_date timestamp(6) NULL,
                                      "version" int8 NULL,
                                      "condition" varchar(255) NULL,
                                      "name" varchar(255) NULL,
                                      CONSTRAINT cm_card_group_pkey PRIMARY KEY (id),
                                      CONSTRAINT fk3ywvpql5g994pxu18lrk7tot FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id),
                                      CONSTRAINT fkcgvhh2txrd0baxlukv8imlwd9 FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id),
                                      CONSTRAINT fkq57nq7hcwjt40w1430fo0vcqv FOREIGN KEY (customer_id) REFERENCES public.cm_customer(id)
);


CREATE TABLE public.cm_card_group_ceilings (
                                               card_group_id int8 NOT NULL,
                                               ceiling_id int8 NOT NULL,
                                               CONSTRAINT cm_card_group_ceilings_id_key UNIQUE (ceiling_id),
                                               CONSTRAINT cm_card_group_ceilings_pkey PRIMARY KEY (card_group_id, ceiling_id),
                                               CONSTRAINT fk_card_group_ceilings_ceiling_id FOREIGN KEY (ceiling_id) REFERENCES public.cm_ceiling(id),
                                               CONSTRAINT fk_card_group_ceilings_card_group_id FOREIGN KEY (card_group_id) REFERENCES public.cm_card_group(id)
);


CREATE TABLE public.cm_card_group_bonuses (
                                              card_group_id int8 NOT NULL,
                                              bonus_id int8 NOT NULL,
                                              CONSTRAINT cm_card_group_bonuses_id_key UNIQUE (bonus_id),
                                              CONSTRAINT cm_card_group_bonuses_pkey PRIMARY KEY (card_group_id, bonus_id),
                                              CONSTRAINT fk_card_group_bonuses_bonus_id FOREIGN KEY (bonus_id) REFERENCES public.cm_bonus(id),
                                              CONSTRAINT fk_card_group_bonuses_card_group_id FOREIGN KEY (card_group_id) REFERENCES public.cm_card_group(id)
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
                                    CONSTRAINT cm_movement_type_check CHECK (((type)::text = ANY ((ARRAY['BANK_TRANSFER'::character varying, 'CARD_PAYMENT'::character varying, 'CASH'::character varying])::text[]))),
	CONSTRAINT fk10a2dkkqrf677ltbmw4pa2sj8 FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id),
	CONSTRAINT fk4hpgu37cqwm41v96d9i7bhdmr FOREIGN KEY (account_id) REFERENCES public.cm_account(id),
	CONSTRAINT fkeq8ur2t4rtbovcix20xgjeq04 FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id)
);


-- public.cm_card definition

-- Drop table

-- DROP TABLE public.cm_card;

CREATE TABLE public.cm_card (
                                actif bool NULL,
                                account_id int8 NULL,
                                card_group_id int8 NULL,
                                created_by_id int8 NULL,
                                created_date timestamp(6) NULL,
                                date_status_change timestamp(6) NOT NULL,
                                id int8 NOT NULL,
                                last_modified_by_id int8 NULL,
                                last_modified_date timestamp(6) NULL,
                                "version" int8 NULL,
                                status varchar(255) NULL,
                                tag varchar(255) NULL,
                                "type" varchar(255) NULL,
                                CONSTRAINT cm_card_pkey PRIMARY KEY (id),
                                CONSTRAINT cm_card_status_check CHECK (((status)::text = ANY ((ARRAY['FREE'::character varying, 'BLOCKED'::character varying, 'IN_USE'::character varying, 'LOST'::character varying])::text[]))),
	CONSTRAINT cm_card_type_check CHECK (((type)::text = ANY ((ARRAY['QUANTITY'::character varying, 'AMOUNT'::character varying])::text[]))),
	CONSTRAINT fket8nqg1arn4pagekeu48swm05 FOREIGN KEY (account_id) REFERENCES public.cm_account(id),
	CONSTRAINT fknoq0jad3ufxwf58njrvh11syn FOREIGN KEY (card_group_id) REFERENCES public.cm_card_group(id),
	CONSTRAINT fkrlwnskvctmn3hs8k5dfv35lbr FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id),
	CONSTRAINT fktag6k0whipyxfajhcu19k3oig FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id)
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
                                         CONSTRAINT cm_authorization_reference_key UNIQUE (reference),
                                         CONSTRAINT fk4j1qtk8yrhqdp1wpalv85vf4y FOREIGN KEY (card_id) REFERENCES public.cm_card(id),
                                         CONSTRAINT fklm6y5ip8edaskpjbnq0brf9qj FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id),
                                         CONSTRAINT fkloq5vly055na6nmu52ww24gca FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id)
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
                                       CONSTRAINT cm_transaction_pkey PRIMARY KEY (id),
                                       CONSTRAINT fk2mwfk67uwa8d9xvvh63pfvjhb FOREIGN KEY (product_id) REFERENCES public.cm_product(id),
                                       CONSTRAINT fk7e6cdlxxapdhirrndllb5s922 FOREIGN KEY (authorization_id) REFERENCES public.cm_authorization(id),
                                       CONSTRAINT fkel96ojcf4exmagphnrfcdrxyw FOREIGN KEY (last_modified_by_id) REFERENCES public.cm_user(id),
                                       CONSTRAINT fknf8xlg2imv2cr1es7fc1vx13s FOREIGN KEY (card_id) REFERENCES public.cm_card(id),
                                       CONSTRAINT fksbtb2s0bls33759wyqw5d5oc5 FOREIGN KEY (created_by_id) REFERENCES public.cm_user(id)
);


-- public.cm_ceiling_seq definition

-- DROP SEQUENCE public.cm_ceiling_seq;

CREATE SEQUENCE public.cm_ceiling_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;


-- public.cm_bonus_seq definition

-- DROP SEQUENCE public.cm_bonus_seq;

CREATE SEQUENCE public.cm_bonus_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;

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


-- public.cm_card_group_seq definition

-- DROP SEQUENCE public.cm_card_group_seq;

CREATE SEQUENCE public.cm_card_group_seq
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




-- public.cm_country_seq definition

-- DROP SEQUENCE public.cm_country_seq;

CREATE SEQUENCE public.cm_country_seq
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


-- public.cm_sale_point_seq definition

-- DROP SEQUENCE public.cm_sale_point_seq;

CREATE SEQUENCE public.cm_sale_point_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;


-- public.cm_supplier_seq definition

-- DROP SEQUENCE public.cm_supplier_seq;

CREATE SEQUENCE public.cm_supplier_seq
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
