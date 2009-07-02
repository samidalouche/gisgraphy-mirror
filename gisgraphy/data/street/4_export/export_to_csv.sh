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
	              psql -U postgres  -d gisgraphystreet -h 127.0.0.1 <<COPYTEST
\f '	' 
\a
\t
\o $countrycode.$iter.txt
select * from openstreetmap where countrycode='$countrycode' offset $offset limit $limit ;
\q
COPYTEST
 
	                if [[ $iter == '1'  ]]
	                then
	                        ((offset =  $offset + 1))
	                fi
	                ((offset= offset + limit ));
	        done
		us_treated=1;
		#for usfile in `ls US.*.txt` ; do cat $usfile >> concatUS.txt; done
		#rm US.*.txt;
		#mv  concatUS.txt US.txt;
	else
			psql -U postgres  -d gisgraphystreet -h 127.0.0.1 <<COPYTEST
\f '	'
\a
\t
\o $countrycode.txt
select * from openstreetmap where countrycode='$countrycode' ;
\q
COPYTEST
	
	fi
done

#zip all
mkdir csv;
mv *.txt csv/
cd csv
for csvfile in `ls *.txt` ; do countrycode=`echo ${csvfile}| cut -d "." -f1`;  zip  $countrycode.zip $countrycode.txt ; done

