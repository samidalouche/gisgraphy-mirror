 #!/bin/bash


export PGPASSWORD="mdppostgres";
databaseName="gisgraphy"
tableName="osm"
pgUser="postgres"
pgHost="127.0.0.1"
dataset_size="1000"

function psql_exportToCSV {
psql -U $pgUser  -d$databaseName -h$pgHost <<COPYTEST
\f '	' 
\a
\t
\o $2
$1
\q
COPYTEST
}


psql_exportToCSV "select replace(replace(astext(location), 'POINT(',''),')','') from gisfeature g where location is not null limit $dataset_size ;" "features_location.csv"
psql_exportToCSV "select featureid from gisfeature g where location is not null limit $dataset_size ;" "featureid.csv"
psql_exportToCSV "select name from gisfeature g where location is not null limit $dataset_size ;" "features_name.csv"
psql_exportToCSV "select replace(replace(astext(location), 'POINT(',''),')','') from openstreetmap g where location is not null limit $dataset_size ;" "streets_location.csv"

