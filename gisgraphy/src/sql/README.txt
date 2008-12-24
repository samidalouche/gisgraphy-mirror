 #Here are the command to run to install and init the gisgraphy database 
 
 psql -U YOURUSER  -H YOURIP -c  "CREATE DATABASE gisgraphy WITH TEMPLATE = template0 ENCODING = 'UTF8';"
 
 psql -U YOURUSER -h YOURIP -f create_tables-POSTGRESVERSION.sql 
 
createlang -U YOURUSER -h YOURIP plpgsql gisgraphy 

psql -U YOURUSER -h YOURIP -d gisgraphy -f /usr/share/postgresql-8.3-postgis/lwpostgis.sql
psql -U YOURUSER -h YOURIP -d gisgraphy -f /usr/share/postgresql-8.3-postgis/spatial_ref_sys.sql

 psql -U YOURUSER -h YOURIP -f createGISTIndex.sql
 
 psql -U YOURUSER -h YOURIP -f insert_users.sql
 
 # here are the 2 command line to reset the database after an import failure :
 psql -U YOURUSER -h YOURIP -f resetdb.sql
 psql -U YOURUSER -h YOURIP -f create_tables-POSTGRESVERSION.sql
 