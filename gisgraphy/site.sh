#!/bin/bash

cd src/skin
mvn clean install
cd ../../
mvn clean site -Dmaven.test.skip -Dresetdb=false -fae -Ddependency.locations.enabled=false
mv target/site/index.html target/site/index_old.html
grep "end header" target/site/index_old.html -B 9999 > target/site/include/header.inc
grep "start footer" target/site/index_old.html -A 9999 > target/site/include/footer.inc
sed 's/gisgraphy - About/Free Open source Geocoder via REST webservices (for geonames and openstreetmap data)/g' target/site/include/header.inc > header.inc
mv header.inc target/site/include/header.inc
rm _rf target/site/index_old.html