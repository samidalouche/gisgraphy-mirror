 #!/bin/bash
 
export PGPASSWORD="mdppostgres";
databaseName="gisgraphyforosmimport"
tableName="osm"
pgUser="postgres"
pgHost="127.0.0.1"
geometryColumnName="shape"
host="localhost"
countrycodeFileName="countrycode4test.txt"
urlFileName="urls4test.txt"
typeset -i OK=0
typeset -i KO=1
 

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
var_count=`psql -q -t -h$host -U$pgUser -d$databaseName -c "select count(*) from $1"`
echo "$var_count";
}

function psql_runSQLcommandOnDatabase {
`psql -h$host -U$pgUser -d$databaseName -c "$1" `
executionReturnedCode=$?;
if [[ $executionReturnedCode==0 ]]
then
	echo "execution of '$1' makes errors (see above)"
fi

return $executionReturnedCode
}

function psql_runSQLcommand {
psql -h$host -U$pgUser -c "$1" 
executionReturnedCode=$?;
if [[ $executionReturnedCode != "0" ]]
then
	echo "execution of '$1' makes errors (status : $executionReturnedCode, see errors above)"
fi
return $executionReturnedCode
}

function importFiles {
`mkdir imported`
tablecreated=0
for i in *.zip
do
	countrycode=`echo ${i}| cut -d "." -f1`
	echo "countrycode =$countrycode"
    	`unzip ${i} -d $countrycode`
	cd $countrycode
	shapefile=`ls *_highway.shp`
	if [[ -n $shapefile ]]
	then
		if [[ $tablecreated -eq 0 ]]
		then
			echo "will create the table $tableName"
			`shp2pgsql -p $shapefile -g $geometryColumnName $tableName $databaseName |psql -U$pgUser -d $databaseName -h$pgHost`
			echo "will add the countrycode column to $tableName"
			psql_runSQLcommandOnDatabase "ALTER TABLE $tableName ADD COLUMN countrycode character(3)[];"
			$tablecreated=1;
		fi
	echo " will process $shapefile";
	#`shp2pgsql -a $shapefile -g $geometryColumnName $table $db > $countrycode.sql`
	`shp2pgsql -a $shapefile -g $geometryColumnName $tableName $databaseName |psql -U$pgUser -d $databaseName -h$pgHost`
	echo "update $tableName set countrycode ='$countrycode' where countrycode is null" | psql -U$pgUser -d $databaseName -h$pgHost
	mv $i imported/	
	else
	echo "no shapeFile in $countrycode"
	fi
	cd ..
	rm -rf $countrycode
done
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

psql_runSQLcommand "DROP DATABASE IF EXISTS $databaseName" 
psql_runSQLcommand "CREATE DATABASE $databaseName WITH OWNER = $pgUser  ENCODING = 'UTF8'"

importFiles

#psql_runSQLcommand "update language set name='e' where 1"
#downloadZipFile && checkDownloadedZipFile
#numbline=`psql_getCountLine`
#echo "$numbline"

