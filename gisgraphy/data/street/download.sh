 #!/bin/bash
 
 paste -d: urls.txt countrycode.txt | while read line ; do echo $line| awk -F: '{print "wget "$1" -O "$2}'| sh ; done
 
for i in *.zip
do
	'unzip -t ${i}'
done