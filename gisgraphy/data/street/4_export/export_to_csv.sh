#!/bin/bash


for i in *.zip
do
	countrycode=`echo ${i}| cut -d "." -f1`
	echo "export $i to CSV" 
	psql -U postgres  -d gisgraphy -h 127.0.0.1 <<COPYTEST 
\f '	' 
\a
\t 
\o $countrycode.csv 
select * from openstreetmap where countrycode='$countrycode' ;
\q 
COPYTEST
done




