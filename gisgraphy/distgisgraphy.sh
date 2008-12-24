#!/bin/bash

mvn clean javadoc:javadoc war:war hibernate3:hbm2ddl assembly:assembly -Dmaven.test.skip  -Dresetdb=false -Pgisgraphy
