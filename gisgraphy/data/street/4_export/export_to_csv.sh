#!/bin/bash

us_treated=0;
for i in *.zip
do
	countrycode=`echo ${i}| cut -d "." -f1`
	echo "export $i to CSV" 
	if [[ $countrycode == 'US' && ($us_treated == 0)]]
	then
	        echo "countrycode is US : $countrycode"
	        ((offset = 0));
	        ((limit = 100000));
	
	        for iter  in `seq 1 290`;
	        do
		      echo "extraction de US $iter / 290"
	              psql -U postgres  -d gisgraphy -h 127.0.0.1 <<COPYTEST
\f '	' 
\a
\t
\o $countrycode.$iter.csv
select * from openstreetmap where countrycode='$countrycode' offset 1 limit 1 ;
\q
COPYTEST
 
	                if [[ $iter == '1'  ]]
	                then
	                        ((offset =  $offset + 1))
	                fi
	                ((offset= offset + limit ));
	        done
us_treated=1;
for usfile in `ls US.*.csv` ; do cat $usfile >> concatUS.csv; done
mv  concatUS.csv US.csv;
	else
			psql -U postgres  -d gisgraphy -h 127.0.0.1 <<COPYTEST
\f '	'
\a
\t
\o $countrycode.csv
select * from openstreetmap where countrycode='$countrycode' limit 1;
\q
COPYTEST
	
	fi
done