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

#tar all
echo "extract is finish, now taring"
mkdir tar;
rm US.txt
mv *.txt tar/
cd tar
mkdir done
echo "tar country file"
for csvfile in `ls *.txt` ;
do countrycode=`echo ${csvfile}| cut -d "." -f1`; 
	if [[ $countrycode == 'US' ]]
	then 
        	 echo "found an US file that must be process after"
	else 
	         tar -jcf  $countrycode.tar.bz2 $countrycode.txt ;
	         mv $countrycode.txt done/
	fi
 done

echo "tar US file" 
tar -jcf US.tar.bz2 US.*.csv
mv US.*.txt done/

#echo "tar allcountries.tar.bz2"
#tar -jcf  allcountries.tar.bz2 *.txt ;
	 
