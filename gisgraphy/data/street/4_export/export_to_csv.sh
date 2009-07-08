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
	
	        for iter  in `seq 1 188`;
	        do
		      echo "extraction de US $iter / 188"
	              psql -U postgres  -d gisgraphystreet -h 127.0.0.1 <<COPYTEST
\f '	' 
\a
\t
\o $countrycode.$iter.csv
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
		#for usfile in `ls US.*.csv` ; do cat $usfile >> concatUS.csv; done
		#rm US.*.csv;
		#mv  concatUS.csv US.csv;
	else
			psql -U postgres  -d gisgraphystreet -h 127.0.0.1 <<COPYTEST
\f '	'
\a
\t
\o $countrycode.csv
select * from openstreetmap where countrycode='$countrycode' ;
\q
COPYTEST
	
	fi
done

#zip all
mkdir csv;
mv *.csv csv/
cd csv
rm US.csv
echo "tar country file"
for csvfile in `ls *.csv` ;
do countrycode=`echo ${csvfile}| cut -d "." -f1`; 
	if [[ $countrycode == 'US' ]]
	then 
        	 tar -jcf US.tar.bz2 US.*.csv
	else 
	         tar -jcf  $countrycode.tar.bz2 $countrycode.csv ;
	fi
 done

echo "tar allcountries.tar.bz2"
tar -jcf  allcountries.tar.bz2 *.csv ;
	 
