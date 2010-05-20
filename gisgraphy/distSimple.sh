#!/bin/bash

mvn clean  war:war  assembly:assembly -Dmaven.test.skip  -Dresetdb=false -Pprod
