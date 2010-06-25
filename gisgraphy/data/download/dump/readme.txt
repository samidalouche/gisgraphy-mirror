Here are the dumps ! If you are not interested in all the countries, 
 or, only Openstreetmap dataset, it will takes less times to inject 
 the data via Gisgraphy importers

The SolR dump must be extracted in the solr/data/ directory
it is required to use the fulltext webservices 

Two postgres dumps are provided :
-> one for openstreetmap data (for street webservice)
-> one for geonames one (for find nearby webservices)

run the folowing command to inject a dump :

psql -U postgres -h 127.0.0.1 -d gisgraphy -f ./THEFILE

Dumps will only inject datas, it will not create the database and tables (see installation guide).
Gisgraphy users and role will be added, ignore warnning if the users are already sets.

Hope it helps!
