
#CREATE TABLE "openstreetmap" (gid serial PRIMARY KEY,"type" varchar(20),"name" varchar(68),"oneway" varchar(9),"shape" geometry NOT NULL,"countrycode" varchar(3),"length" integer,"location" geometry );

`mkdir done`
for i in *.zip
do
	countrycode=`echo ${i}| cut -d "." -f1`
	db=gisgraphy
	table=openstreetmap
	echo "countrycode =$countrycode"
    	`unzip ${i} -d $countrycode`
	cd $countrycode
	shapefile=`ls *_highway.shp`
	if [[ -n $shapefile ]]
	then
	echo " will process $shapefile";
	#`shp2pgsql -a $shapefile -g shape $table $db > $countrycode.sql`
	`shp2pgsql -a $shapefile -g shape $table $db |psql -Upostgres -d $db -h127.0.0.1`
	`echo "update $table set countrycode ='$countrycode' where countrycode is null" | psql -Upostgres -d $db -h127.0.0.1`
	else
	echo "no shapeFile in $countrycode"
	fi
	cd ..
	rm -rf $countrycode
	mv $i done/	
done


