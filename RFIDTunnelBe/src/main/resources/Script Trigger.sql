DROP TABLE IF EXISTS public.package_sent_ws;

CREATE TABLE public.package_sent_ws
(
    id_send integer,
    gtinbox character varying COLLATE pg_catalog."default",
    codewo character varying COLLATE pg_catalog."default",
    codearticle character varying COLLATE pg_catalog."default",
    nbtu character varying COLLATE pg_catalog."default",
    tid_list character varying COLLATE pg_catalog."default",
    nbarticle integer,
    sent boolean DEFAULT false
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.package_sent_ws
    OWNER to postgres;


-- FUNCTION: public.extractcodart(character varying)

DROP FUNCTION IF EXISTS public.extractcodart(character varying);

CREATE OR REPLACE FUNCTION public.extractcodart(
	xxx character varying)
    RETURNS character varying
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
xpos1 character varying;
xpos2 character varying;
xpos3 character varying;
search_string character varying;
BEGIN
search_string := '(01)';
select position(search_string in xxx)::character varying into xpos1;
select position('(' in substring(xxx,xpos1::integer+length(search_string),length(xxx))) into xpos2;
                if (xpos2::integer = 0) then
                               xpos2=length(xxx);
                end if;
if (xpos1::integer = 0) then 
	xpos3 = '' ;
else
	select substring(xxx,xpos1::integer+length(search_string),xpos2::integer -1) into xpos3;
end if;
 
RAISE NOTICE 'length total %',length(xxx);
RAISE NOTICE 'xpos1 %',xpos1;
RAISE NOTICE 'xpos2 %',xpos2;
RAISE NOTICE 'xpos3 %',xpos3;
 
 
return xpos3;
end;
$BODY$;

ALTER FUNCTION public.extractcodart(character varying)
    OWNER TO postgres;



-- FUNCTION: public.extractgtinbox(character varying)

DROP FUNCTION IF EXISTS public.extractgtinbox(character varying);

CREATE OR REPLACE FUNCTION public.extractgtinbox(
	xxx character varying)
    RETURNS character varying
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
xpos1 character varying;
xpos2 character varying;
xpos3 character varying;
search_string character varying;
BEGIN
search_string := '(00)';
select position(search_string in xxx)::character varying into xpos1;
select position('(' in substring(xxx,xpos1::integer+length(search_string),length(xxx))) into xpos2;
                if (xpos2::integer = 0) then
                               xpos2=length(xxx);
                end if;
if (xpos1::integer = 0) then 
	xpos3 = '' ;
else
	select substring(xxx,xpos1::integer+length(search_string),xpos2::integer -1) into xpos3;
end if;
 
RAISE NOTICE 'length total %',length(xxx);
RAISE NOTICE 'xpos1 %',xpos1;
RAISE NOTICE 'xpos2 %',xpos2;
RAISE NOTICE 'xpos3 %',xpos3;
 
 
return xpos3;
end;
$BODY$;

ALTER FUNCTION public.extractgtinbox(character varying)
    OWNER TO postgres;



-- FUNCTION: public.extractqty(character varying)

DROP FUNCTION IF EXISTS public.extractqty(character varying);

CREATE OR REPLACE FUNCTION public.extractqty(
	xxx character varying)
    RETURNS character varying
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
xpos1 character varying;
xpos2 character varying;
xpos3 character varying;
search_string character varying;
BEGIN
search_string := '(37)';
select position(search_string in xxx)::character varying into xpos1;
select position('(' in substring(xxx,xpos1::integer+length(search_string),length(xxx))) into xpos2;
                if (xpos2::integer = 0) then
                               xpos2=length(xxx);
                end if;
if (xpos1::integer = 0) then 
	xpos3 = '0' ;
else
	select substring(xxx,xpos1::integer+length(search_string),xpos2::integer -1) into xpos3;
end if;

 
RAISE NOTICE 'length total %',length(xxx);
RAISE NOTICE 'xpos1 %',xpos1;
RAISE NOTICE 'xpos2 %',xpos2;
RAISE NOTICE 'xpos3 %',xpos3;
 
 
return xpos3;
end;
$BODY$;

ALTER FUNCTION public.extractqty(character varying)
    OWNER TO postgres;
	
	
	
-- FUNCTION: public.extractwo(character varying)

DROP FUNCTION IF EXISTS public.extractwo(character varying);

CREATE OR REPLACE FUNCTION public.extractwo(
	xxx character varying)
    RETURNS character varying
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
xpos1 character varying;
xpos2 character varying;
xpos3 character varying;
search_string character varying;
BEGIN
search_string := '(10)';
select position(search_string in xxx)::character varying into xpos1;
select position('(' in substring(xxx,xpos1::integer+length(search_string),length(xxx))) into xpos2;
                if (xpos2::integer = 0) then
                               xpos2=length(xxx);
                end if;
if (xpos1::integer = 0) then 
	xpos3 = '' ;
else
	select substring(xxx,xpos1::integer+length(search_string),xpos2::integer -1) into xpos3;
end if;
 
RAISE NOTICE 'length total %',length(xxx);
RAISE NOTICE 'xpos1 %',xpos1;
RAISE NOTICE 'xpos2 %',xpos2;
RAISE NOTICE 'xpos3 %',xpos3;
 
 
return xpos3;
end;
$BODY$;

ALTER FUNCTION public.extractwo(character varying)
    OWNER TO postgres;



-- FUNCTION: public.isnumeric(text)

DROP FUNCTION IF EXISTS public.isnumeric(text);

CREATE OR REPLACE FUNCTION public.isnumeric(
	text)
    RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    IMMUTABLE STRICT PARALLEL UNSAFE
AS $BODY$
DECLARE x NUMERIC;
BEGIN
    x = $1::NUMERIC;
    RETURN TRUE;
EXCEPTION WHEN others THEN
    RETURN FALSE;
END;
$BODY$;

ALTER FUNCTION public.isnumeric(text)
    OWNER TO postgres;
	
	
	
	
-- Trigger: expt_ean128

DROP TRIGGER IF EXISTS  expt_ean128 ON public.scanner_stream;


	
	
-- Trigger: package_send

DROP TRIGGER IF EXISTS package_send ON public.scanner_stream;



-- FUNCTION: public.expected()

DROP FUNCTION IF EXISTS public.expected();

CREATE FUNCTION public.expected()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
declare righe integer;
BEGIN
select extractqty(new.package_data)::integer into righe;
 
if (righe) >0 then
				delete from reader_stream_atteso where package_data =new.package_data;
                insert into reader_stream_atteso (package_data,epc)
                select new.package_data,'EEEEEEEEEEEEEEEEEEEEEEEE' from generate_series(1,righe,1);
end if;
RETURN NEW;
END;
$BODY$;

ALTER FUNCTION public.expected()
    OWNER TO postgres;


-- FUNCTION: public.ws_package()

DROP FUNCTION IF EXISTS public.ws_package();

CREATE FUNCTION public.ws_package()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
declare current_wo character varying;
declare previous_wo character varying;
declare seq_pack integer;
BEGIN
select extractwo(new.package_data) into current_wo;

select extractwo((select package_data from scanner_stream where time_stamp < new.time_stamp and elaborated=false order by time_stamp desc limit 1)) into previous_wo;

raise notice '-------------------------------------';

raise notice 'current WO %',current_wo;

raise notice 'previous WO %',previous_wo;

raise notice '-------------------------------------';

if (current_wo = '') then
	update scanner_stream ss set elaborated = true 
	where extractwo(ss.package_data) = current_wo;
else
	if (current_wo <> previous_wo) then
		if (previous_wo <> '') then
			seq_pack := nextval('seq_package_ws');
			insert into package_sent_ws 
			select seq_pack,
			extractgtinbox(ss.package_data),extractwo(ss.package_data),
			extractcodart(ss.package_data),extractqty(ss.package_data) 
			,string_agg(rs.tid,','),count(distinct rs.tid),false
			from scanner_stream ss join reader_stream rs on ss.id = pack_id
			where extractwo(ss.package_data) = previous_wo and elaborated = false
			group by ss.id,ss.package_data,extractgtinbox(ss.package_data),extractwo(ss.package_data),
			extractcodart(ss.package_data),extractqty(ss.package_data);
		end if;	
		update scanner_stream ss set elaborated = true 
		where extractwo(ss.package_data) = previous_wo;
	end if;
end if;
RETURN NEW;
--	EXCEPTION
--		WHEN others THEN 
--			return new;
	END;
$BODY$;

ALTER FUNCTION public.ws_package()
    OWNER TO postgres;

-- Trigger: expt_ean128
CREATE TRIGGER expt_ean128
    BEFORE INSERT
    ON public.scanner_stream
    FOR EACH ROW
    EXECUTE PROCEDURE public.expected();
	
-- Trigger: package_send	
CREATE TRIGGER package_send
    BEFORE INSERT
    ON public.scanner_stream
    FOR EACH ROW
    EXECUTE PROCEDURE public.ws_package();
	