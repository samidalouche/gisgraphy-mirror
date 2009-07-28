#!/bin/bash

cd `dirname ${BASH_SOURCE[0]}`

java -Dfile.encoding=UTF-8 -Xmx1024m -Xms512m -jar start.jar