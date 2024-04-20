-- public.product definition

-- Drop table

-- DROP TABLE public.product;

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

-- DROP TABLE public.order_request;

CREATE TABLE public.order_request (
	id bigserial NOT NULL,
	requested_by varchar NOT NULL,
	created_on timestamp NOT NULL,
	order_number varchar DEFAULT 1 NOT NULL,
	CONSTRAINT order_request_pk PRIMARY KEY (id)
);
-- public.order_request_products definition

-- Drop table

-- DROP TABLE public.order_request_products;

CREATE TABLE public.order_request_products (
	id int8 DEFAULT nextval('order_products_id_seq'::regclass) NOT NULL,
	id_product int8 DEFAULT nextval('order_products_id_product_seq'::regclass) NOT NULL,
	id_order_request int8 DEFAULT nextval('order_products_id_order_request_seq'::regclass) NOT NULL,
	CONSTRAINT order_products_pk PRIMARY KEY (id),
	CONSTRAINT order_products_order_request_fk FOREIGN KEY (id_order_request) REFERENCES public.order_request(id),
	CONSTRAINT order_products_product_fk FOREIGN KEY (id_product) REFERENCES public.product(id)
);