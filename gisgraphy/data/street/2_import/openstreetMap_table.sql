# this script is use to create the import of the openstreetmap data.it is not the same schéma as the one use in gisgraphy where more constraint are added

CREATE TABLE openstreetmap
(
  id bigint,
  "name" character varying(500),
  "location" geometry,
  length double precision,
  countrycode character varying(3),
  gid bigint,
  "type" character varying(50),
  oneway character varying(255),
  shape geometry NOT NULL,
  CONSTRAINT openstreetmap_gid_key UNIQUE (gid)
)
WITH (OIDS=FALSE);
ALTER TABLE openstreetmap OWNER TO postgres;

