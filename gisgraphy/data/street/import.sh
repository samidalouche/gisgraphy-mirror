#CREATE TABLE "openstreetmap" (gid serial PRIMARY KEY,"type" varchar(20),"name" varchar(68),"oneway" varchar(9),"shape" geometry NOT NULL,"countrycode" varchar(3));

for i in *.zip
do
	countrycode=`echo ${i}| cut -d "." -f1`
	echo "countrycode =$countrycode"
    	`unzip ${i} -d $countrycode`
	cd $countrycode
	shapefile=`ls *_highway.shp`
	echo " will process $shapefile";
	#`shp2pgsql -a $shapefile -g shape openstreetmap gisgraphy > $countrycode.sql`
	`shp2pgsql -a $shapefile -g shape openstreetmap gisgraphy |psql -Upostgres -d gisgraphy -h127.0.0.1`
	`echo "update openstreetmap set countrycode ='$countrycode' where countrycode is null" | psql -Upostgres -d gisgraphy -h127.0.0.1`
	cd ..
done


