#!/bin/bash

cd src/skin
mvn clean install
cd ../../
mvn clean site -Dmaven.test.skip -Dresetdb=false -fae -Ddependency.locations.enabled=false
mv target/site/index.html target/site/index_old.html