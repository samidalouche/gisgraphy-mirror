Here are the dumps ! If you are not interested in all the countries, 
 or in only Openstreetmap dataset, it will takes less times to inject 
 the data via Gisgraphy importers

The SolR dump must be extract in the solr/data/ directory 
and the second one is a sql file to inject :

psql -U postgres -h 127.0.0.1 -d gisgraphy -f ./THEFILE

Hope it helps!
