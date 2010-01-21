 #!/bin/bash
 
export PGPASSWORD="mdppostgres";
databaseName="gisgraphyforosmimport"
tablename="osm"
pgUser="postgres"
host="localhost"
typeset -i OK=0
typeset -i KO=1
 

function downloadZipFile {

echo "will download files"
if [[ ! -e urls.txt ]]
then
	echo "Can not find the file urls.txt"
	return $KO
fi

if [[ ! -e countrycode.txt ]]
then
	echo "Can not find the file countrycode.txt"
	return $KO
fi

#paste -d: urls.txt countrycode.txt | while read line ; do echo $line| awk -F: '{print "wget "$1" -O "$2}'| sh ; done
echo "download files finished"
return $OK
}

function checkDownloadedZipFile {
echo "Checking zip files....." 
 
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

notZipFileLength=${#notZipFile}
echo "$arraypointer files are not valid zip files :"
for ((i=0;i<$arraypointer;i++)); do
   echo "     file ${notZipFile[${i}]}";
done

if [[ notZipFileLength!=0 ]]
then
	return $KO
fi
return $OK
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

echo "----------------------------------------------------------------------------------------------"
echo "DOWNLOAD FILES"
echo "----------------------------------------------------------------------------------------------"

#downloadZipFile

echo "----------------------------------------------------------------------------------------------"
echo "CHECK FILES"
echo "----------------------------------------------------------------------------------------------"

#checkDownloadedZipFile

echo "----------------------------------------------------------------------------------------------"
echo "IMPORT FILES"
echo "----------------------------------------------------------------------------------------------"

psql_runSQLcommand "DROP DATABASE IF EXISTS $databaseName" 
psql_runSQLcommand "CREATE DATABASE $databaseName WITH OWNER = $pgUser  ENCODING = 'UTF8'"

#psql_runSQLcommand "update language set name='e' where 1"
#downloadZipFile && checkDownloadedZipFile
#numbline=`psql_getCountLine`
#echo "$numbline"

