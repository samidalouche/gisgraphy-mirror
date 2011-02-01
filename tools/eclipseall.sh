#!/bin/bash

cd ../gisgraphy-commons
mvn clean eclipse:clean eclipse:eclipse
cd ../gisgraphy-utils
mvn clean eclipse:clean eclipse:eclipse
cd ../universalserializer
mvn clean eclipse:clean eclipse:eclipse
cd ../gisgraphy
mvn clean eclipse:clean eclipse:eclipse