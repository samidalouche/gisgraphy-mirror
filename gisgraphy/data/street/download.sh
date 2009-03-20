 #!/bin/bash
 
 paste -d: urls.txt countrycode.txt | while read line ; do echo $line| awk -F: '{print "wget "$1" -O "$2}'| sh ; done
 
echo "Checking files....." 
 
for i in *.zip
do
	echo "checking $i" 
	`unzip -t ${i}`
done