--
-- PostgreSQL database dump
--

-- Dumped from database version 12.2
-- Dumped by pg_dump version 12.2

-- Started on 2020-06-18 18:57:32

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 203 (class 1259 OID 46649)
-- Name: credentials; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.credentials (
    id bigint NOT NULL,
    password character varying(100) NOT NULL,
    role character varying(10) NOT NULL,
    user_name character varying(100) NOT NULL,
    user_id bigint
);


ALTER TABLE public.credentials OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 46647)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 46654)
-- Name: project; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project (
    id bigint NOT NULL,
    description character varying(100) NOT NULL,
    name character varying(100) NOT NULL,
    owner_id bigint
);


ALTER TABLE public.project OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 46659)
-- Name: project_members; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_members (
    visible_projects_id bigint NOT NULL,
    members_id bigint NOT NULL
);


ALTER TABLE public.project_members OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 46662)
-- Name: tag; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tag (
    id bigint NOT NULL,
    color character varying(100) NOT NULL,
    description character varying(100) NOT NULL,
    name character varying(100) NOT NULL,
    project_id bigint
);


ALTER TABLE public.tag OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 46667)
-- Name: task; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.task (
    id bigint NOT NULL,
    completed boolean NOT NULL,
    creation_timestamp timestamp without time zone NOT NULL,
    description character varying(100) NOT NULL,
    last_update_timestamp timestamp without time zone NOT NULL,
    name character varying(100) NOT NULL,
    assigneduser_id bigint,
    project_id bigint
);


ALTER TABLE public.task OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 46672)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    creation_timestamp timestamp without time zone NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    last_update_timestamp timestamp without time zone NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 2708 (class 2606 OID 46653)
-- Name: credentials credentials_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.credentials
    ADD CONSTRAINT credentials_pkey PRIMARY KEY (id);


--
-- TOC entry 2712 (class 2606 OID 46658)
-- Name: project project_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT project_pkey PRIMARY KEY (id);


--
-- TOC entry 2714 (class 2606 OID 46666)
-- Name: tag tag_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tag
    ADD CONSTRAINT tag_pkey PRIMARY KEY (id);


--
-- TOC entry 2716 (class 2606 OID 46671)
-- Name: task task_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task
    ADD CONSTRAINT task_pkey PRIMARY KEY (id);


--
-- TOC entry 2710 (class 2606 OID 46678)
-- Name: credentials uk_iruybducdoxd2f0vh3t8g6x5y; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.credentials
    ADD CONSTRAINT uk_iruybducdoxd2f0vh3t8g6x5y UNIQUE (user_name);


--
-- TOC entry 2718 (class 2606 OID 46676)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 2721 (class 2606 OID 46689)
-- Name: project_members fk6qakef2mfjhaoaqep3b9qneea; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_members
    ADD CONSTRAINT fk6qakef2mfjhaoaqep3b9qneea FOREIGN KEY (members_id) REFERENCES public.users(id);


--
-- TOC entry 2720 (class 2606 OID 46684)
-- Name: project fk7tetln4r9qig7tp05lsdqe8xo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project
    ADD CONSTRAINT fk7tetln4r9qig7tp05lsdqe8xo FOREIGN KEY (owner_id) REFERENCES public.users(id);


--
-- TOC entry 2723 (class 2606 OID 46699)
-- Name: tag fkbyy56vice9njgl86752up8120; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tag
    ADD CONSTRAINT fkbyy56vice9njgl86752up8120 FOREIGN KEY (project_id) REFERENCES public.project(id);


--
-- TOC entry 2719 (class 2606 OID 46679)
-- Name: credentials fkcbcgksvnqvqxrrc4dwv3qys65; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.credentials
    ADD CONSTRAINT fkcbcgksvnqvqxrrc4dwv3qys65 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 2724 (class 2606 OID 46704)
-- Name: task fkhwmqnd5wgqfu0i66pu3tvxm7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task
    ADD CONSTRAINT fkhwmqnd5wgqfu0i66pu3tvxm7 FOREIGN KEY (assigneduser_id) REFERENCES public.users(id);


--
-- TOC entry 2725 (class 2606 OID 46709)
-- Name: task fkk8qrwowg31kx7hp93sru1pdqa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task
    ADD CONSTRAINT fkk8qrwowg31kx7hp93sru1pdqa FOREIGN KEY (project_id) REFERENCES public.project(id);


--
-- TOC entry 2722 (class 2606 OID 46694)
-- Name: project_members fkkkowdb1552cnnmu8apvugooo0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_members
    ADD CONSTRAINT fkkkowdb1552cnnmu8apvugooo0 FOREIGN KEY (visible_projects_id) REFERENCES public.project(id);


-- Completed on 2020-06-18 18:57:33

--
-- PostgreSQL database dump complete
--

