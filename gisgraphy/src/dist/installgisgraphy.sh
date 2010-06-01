#!/bin/bash


function check_psql_installed {
	echo "Checking if psql exists..."
	which psql >/dev/null

	if [[ $? != 0 ]]
	then
		echo "Postgres does not seems to be installed"
		read -p "Do you want to install Postgres [Y/n] " installpsql

		if [[ $installpsql == "Y" || $installpsql == "y" || $installpsql == "" ]]
		then
			installPostgres		
			else
			echo "Postgres is not available, please install it !"
		fi
	else
		echo "Postgres is install !" 
		echo "Checking postgres"
	fi
}

function installPostgres {
	echo "trying to install postgres and postgis"
	echo "checking if yum or apt availables..."
	which apt-get >/dev/null
		if [[ $? != 0 ]]
		then
			echo "apt not available, trying yum..."
			which yum >/dev/null
			if [[ $? != 0 ]]
			then
			echo "yum and apt not availables... please install Postgres by hand"
			fi
		else
		echo "apt is found !"
		echo "Trying to install postgres with apt..."
		fi
	} 
	
function confirm_gisgraphy_installation {
clear
echo "#################################################"	 
echo "####   Welcome to gisgraphy installation ########"
echo "#################################################"
echo ""
echo "The installation will do the following step : "
echo "1 : Check if Postgres and Postgis are installed"
echo "2 : Install postgres and postgis if not installed"
echo "3 : Create and initialize Gisgraphy database"
echo ""
read -p "Do you want to continue [Y/n] " installgisgraphy

if [[ $installgisgraphy == "y" || $installgisgraphy == "Y" || $installgisgraphy == "" ]]
then
	echo "Here we go!";
	check_psql_installed ;
else
echo "End of the process, if you want to install Gisgraphy by hand, please see the documentation."
fi
}

confirm_gisgraphy_installation
