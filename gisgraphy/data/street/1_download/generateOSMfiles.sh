 #!/bin/bash
 
export PGPASSWORD="mdppostgres";
databaseName="gisgraphyforosmimport"
tableName="osm"
pgUser="postgres"
pgHost="127.0.0.1"
geometryColumnName="shape"
lengthColumnName="length"
locationColumnName="location"
countrycodeFileName="countrycode4test.txt"
urlFileName="urls4test.txt"
typeset -i OK=0
typeset -i KO=1
postgisPath="/usr/share/postgresql-8.3-postgis/"
postgislwFileName="lwpostgis.sql"
postgisSpatialRefSysFileName="spatial_ref_sys.sql"

 

function downloadZipFile {

echo "will download files"
if [[ ! -e $urlFileName ]]
then
	echo "Can not find the file $urlFileName"
	return $KO
fi

if [[ ! -e $countrycodeFileName ]]
then
	echo "Can not find the file $countrycodeFileName"
	return $KO
fi

paste -d: $urlFileName $countrycodeFileName | while read line ; do echo $line| awk -F: '{print "wget "$1" -O "$2}'| sh ; done
echo "download files finished"
return $OK
}

function checkDownloadedZipFile {
echo "Checking number of zipped files....." 
((nbURLs=`cat $urlFileName | wc -l`+1))
((nbZipFile=`ls *.zip | wc -l`))
if [[ $nbURLs > $nbZipFile ]]
then
echo "The number of zipped files is not enough (expected : $nbURLs, but was $nbZipFile)"
exit 1
else
echo "The number of downloaded files is correct"
fi

echo "Checking ziped files....." 
 
((arraypointer=0))
for i in *.zip
do
	echo "checking $i" 
	`unzip -t ${i} >>out.txt 2>&1`
	#echo "status code for $i : $? "
	if  [[ $? != 0 ]] 
	then
       notZipFile[$arraypointer]=$i; 
       arraypointer=$(($arraypointer+1))
	fi 
done
echo "$arraypointer"
if [[ ! $arraypointer -eq 0 ]]
then
   for ((i=0;i<$arraypointer;i++)); do
     echo "     file ${notZipFile[${i}]}";
   done
   return $KO
else
	echo "all zipped files are correct :"
	return $OK
fi
}

function psql_getCountLine {
var_count=`psql -q -t -h$pgHost -U$pgUser -d$databaseName -c "select count(*) from $1"`
echo "$var_count";
}

function psql_runSQLcommandOnDatabase {
psql -h$pgHost -U$pgUser -d$databaseName -c "$1"
executionReturnedCode=$?;
if [[ $executionReturnedCode==0 ]]
then
	echo "execution of '$1' makes errors (see above)"
fi

return $executionReturnedCode
}


function psql_runSQLFileOnDatabase {
echo "run $1 file on $databaseName";
`psql -h $pgHost -U $pgUser -d$databaseName -f $1` 
executionReturnedCode=$?;
if [[ $executionReturnedCode==0 ]]
then
	echo "execution of '$1' makes errors (see above)"
fi

return $executionReturnedCode
}

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

function psql_runSQLcommand {
` psql -h$pgHost -U$pgUser -c "$1" ` 
executionReturnedCode=$?;
if [[ $executionReturnedCode != "0" ]]
then
	echo "execution of '$1' makes errors (status : $executionReturnedCode, see errors above)"
fi
return $executionReturnedCode
}

function init_database {
	echo "Droping Database $databaseName ..."
	psql_runSQLcommand "DROP DATABASE IF EXISTS $databaseName"
	echo "Re-creating $databaseName" 
	psql_runSQLcommand "CREATE DATABASE $databaseName WITH OWNER = $pgUser  ENCODING = 'UTF8'"
	echo "create lang plpgsql"
	createlang -U $pgUser -h $pgHost plpgsql $databaseName 
	
	echo "runingPostgis files ..."
	psql_runSQLFileOnDatabase $postgisPath$postgislwFileName
	psql_runSQLFileOnDatabase $postgisPath$postgisSpatialRefSysFileName
}

function importFiles {
`mkdir imported`
tablecreated=0
for i in *.zip
do
	countrycode=`echo ${i}| cut -d "." -f1`
	echo "countrycode=$countrycode"
    	`unzip ${i} -d $countrycode`
	cd $countrycode
	shapefile=`ls *_highway.shp`
	if [[ -n $shapefile ]]
	then
		if [[ $tablecreated -eq 0 ]]
		then
			echo "will create the table $tableName"
			` shp2pgsql -p $shapefile -g $geometryColumnName $tableName $databaseName |psql -U$pgUser -d $databaseName -h$pgHost `
			echo "will add the countrycode column to $tableName"
			psql_runSQLcommandOnDatabase "ALTER TABLE $tableName ADD COLUMN countrycode character(3)[];"
			tablecreated=1;
		fi
	echo " will process $shapefile";
	` shp2pgsql -a $shapefile -g $geometryColumnName $tableName $databaseName | psql -U$pgUser -d $databaseName -h$pgHost `
	psql_runSQLcommandOnDatabase "update $tableName set countrycode ='$countrycode' where countrycode is null"
	else
	echo "============================================================>no shapeFile in $countrycode"
	fi
	cd ..
	` mv $i imported `	
	rm -rf $countrycode
done
}

function clean_data {
	echo "clean type"
	psql_runSQLcommandOnDatabase "update $tableName  set type = regexp_replace(type, '.*:', '') where type like 'plan.at%';"
	
	echo "update type to remove less frequent type"
	psql_runSQLcommandOnDatabase "update $tableName set type= NULL where type not in (select lessFrequentType.type from (select o.type,count(type) as count from $tableName o group by type order by count) as lessFrequentType where lessFrequentType.count < 1000)"
	psql_runSQLcommandOnDatabase "update $tableName set type= NULL where type= 'unclassified';"
	#check select count(oneway) as c, oneway from $tableName group by oneway order by C desc
	
	echo "clean oneway phase 1 "
	psql_runSQLcommandOnDatabase "update $tableName set oneway='false' where oneway <> '1' and oneway <> 'yes' and oneway <> 'true'"
	echo "clean oneway phase 2 "
	psql_runSQLcommandOnDatabase "update $tableName set oneway='true' where oneway = 'yes' or oneway = '1'"
	echo "clean oneway phase 3 "
	psql_runSQLcommandOnDatabase "update $tableName set oneway='false' where oneway <> 'false' and oneway <> 'true'"
	echo "clean oneway phase 4 "
	psql_runSQLcommandOnDatabase "update $tableName set oneway='false' where oneway is null"
	
	echo "add $locationColumnName column"
	psql_runSQLcommandOnDatabase "ALTER TABLE $tableName ADD COLUMN \"$locationColumnName\" geometry;"
	echo "update MidPoint (location)" 
	psql_runSQLcommandOnDatabase "update $tableName set $locationColumnName=line_interpolate_point(LineMerge($geometryColumnName),0.5)"
	
	echo "add $lengthColumnName column"
	psql_runSQLcommandOnDatabase "ALTER TABLE $tableName ADD COLUMN $lengthColumnName double precision"
	echo "update length"
	psql_runSQLcommandOnDatabase "update $tableName set $lengthColumnName=distance(startpoint($geometryColumnName),endpoint($geometryColumnName))"
}


function export_data {
	cd imported
	mkdir exported
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
		             psql_exportToCSV "select * from $tableName where countrycode='$countrycode' offset $offset limit $limit;" "$countrycode.$iter.txt"
	 
		                if [[ $iter == '1'  ]]
		                then
		                        ((offset =  $offset + 1))
		                fi
		                ((offset= offset + limit ));
		        done
			us_treated=1;
		else
				psql_exportToCSV "select * from $tableName where countrycode='$countrycode'" "$countrycode.txt"
		fi
		mv *.txt exported
	done
}

function tar_data {
pwd
	#remove US.txt because it must not be treated
	rm US.txt
	mkdir txt_file_already_tared
	cp ../readme.txt ./exported
	cd exported
	echo "tar country file"
	for csvfile in `ls *.txt` ;
	do 
		if [[ $csvfile == 'readme.txt' ]]
		then 
			echo "readme should not be processed"
		    continue
		fi
		countrycode=`echo ${csvfile}| cut -d "." -f1`; 
		if [[ $countrycode == 'US' ]]
		then 
	        	 echo "found a US file that must not be processed now: ${csvfile}"
		else 
			 echo "taring $countrycode.txt"
		         tar -jcf  $countrycode.tar.bz2 $countrycode.txt readme.txt;
		         mv $countrycode.txt ../txt_file_already_tared/
		fi
	 done
	
	echo "tar US file" 
	tar -jcf US.tar.bz2 US.*.txt readme.txt
	mv US.*.txt ../txt_file_already_tared/
	
	#echo "tar allcountries.tar.bz2"
	#tar -jcf  allcountries.tar.bz2 *.txt ;
	rm readme.txt
	cd ..
	mv exported file_to_be_uploded
}


echo "----------------------------------------------------------------------------------------------"
echo "DOWNLOAD FILES"
echo "----------------------------------------------------------------------------------------------"

downloadZipFile

echo "----------------------------------------------------------------------------------------------"
echo "CHECK FILES"
echo "----------------------------------------------------------------------------------------------"

checkDownloadedZipFile

echo "----------------------------------------------------------------------------------------------"
echo "IMPORT FILES"
echo "----------------------------------------------------------------------------------------------"

init_database
importFiles

#numbline=`psql_getCountLine`
#echo "$numbline"

echo "----------------------------------------------------------------------------------------------"
echo "clean Data"
echo "----------------------------------------------------------------------------------------------"

clean_data

echo "----------------------------------------------------------------------------------------------"
echo "EXPORT DATA"
echo "----------------------------------------------------------------------------------------------"

export_data

echo "----------------------------------------------------------------------------------------------"
echo "TAR FILES"
echo "----------------------------------------------------------------------------------------------"

tar_data