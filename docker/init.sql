-- DROP SCHEMA public;
CREATE SCHEMA IF NOT EXISTS public AUTHORIZATION pg_database_owner;
-- public.product definition

-- Drop table
--DROP TABLE IF EXISTS public.product;
CREATE TABLE public.product (
	description varchar NOT NULL,
	active bool DEFAULT false NULL,
	price numeric DEFAULT 0.0 NULL,
	created_on timestamp NOT NULL,
	category varchar NOT NULL,
	id bigserial NOT NULL,
	CONSTRAINT product_pk PRIMARY KEY (id)
);
-- public.order_request definition

-- Drop table
--DROP TABLE IF EXISTS public.order_request;
CREATE TABLE public.order_request (
	id bigserial NOT NULL,
	requested_by varchar NOT NULL,
	created_on timestamp NOT NULL,
	order_number varchar DEFAULT 1 NOT NULL,
	status varchar DEFAULT 'CREATED'::character varying NOT NULL,
	CONSTRAINT order_request_pk PRIMARY KEY (id)
);
-- public.order_request_products definition

-- Drop table
--DROP TABLE public.order_request_products;
CREATE TABLE public.order_request_products (
	id serial8 NOT NULL,
	id_product bigserial NOT NULL,
	id_order_request bigserial NOT NULL,
	CONSTRAINT order_request_products_pk PRIMARY KEY (id)
);
ALTER TABLE public.order_request_products ADD CONSTRAINT order_request_products_product_fk FOREIGN KEY (id_product) REFERENCES public.product(id);
ALTER TABLE public.order_request_products ADD CONSTRAINT order_request_products_order_request_fk FOREIGN KEY (id_order_request) REFERENCES public.order_request(id);
