#!/bin/bash

cd ../gisgraphy-commons
mvn clean install -Dmaven.test.skip
cd ../gisgraphy-utils
mvn clean install -Dmaven.test.skip
cd ../universalserializer
mvn clean install -Dmaven.test.skip
