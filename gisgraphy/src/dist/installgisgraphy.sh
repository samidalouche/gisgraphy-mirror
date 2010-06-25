#!/bin/bash

postgres_version="8.4";
postgres_default_server="127.0.0.1"
postgres_default_user="postgres"
postgres_default_database="gisgraphy"


postgres_password=
postgres_server=
postgres_user=

function check_psql_installed {
	echo "Checking if psql exists..."
	which psql >/dev/null

	if [[ $? != 0 ]]
	then
		echo "Postgres does not seems to be installed, it is not necessary if Postgres is on an other server."
		read -p "Do you want to install Postgres (it is not necessary if you you postgres on an other serveur) [Y/n] " installpsql

		if [[ $installpsql == "Y" || $installpsql == "y" || $installpsql == "" ]]
		then
			installPostgres		
			else
			echo "Postgres is not available, it is not necessary if Postgres Server is on an other server, if it is not the case, please install it !"
		fi
	else
		echo "Postgres is install !" 
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
			else 
				echo "try to install postgres with yum"
			fi
		else
		echo "apt is found !"
		echo "Trying to install postgres and postgis with apt..."
		sudo -p "Enter password for root (unix) user : " apt-get install postgresql-$postgres_version postgresql-$postgres_version-postgis 
		fi
	} 
	
	
function install_java {
echo "trying to install java"
	echo "checking if java is already installed..."
	which java >/dev/null
		if [[ $? != 0 ]]
		then
			echo "java not installed..."
			echo "will install it, note that during the installation you have to accept the license"
			sudo -p "Enter password for root (unix) user : " apt-add-repository "deb http://archive.canonical.com/ubuntu lucid partner"
			sudo -p "Enter password for root (unix) user : " apt-get update
			sudo -p "Enter password for root (unix) user : " apt-get install sun-java6-bin sun-java6-fonts sun-java6-javadb sun-java6-jdk sun-java6-jre sun-java6-source
		else
		echo "Java is already installed"
		fi
}

function configure_postgres_password {
echo "will set postgres (unix) user password"
sudo -p "Enter password for root (unix) user : " echo ""
echo "please enter the password for the postgres (unix) user"
sudo -p "Enter password for root (unix) user : " passwd postgres
read -p "Please enter the postgres (database) user password : " postgresPassword
postgres_password = $postgresPassword
echo "please enter the postgres (unix) user password"
su - postgres -c "echo \" \""
su - postgres -c "psql -d template1 -c \"alter user postgres with password '$postgresPassword'\""
rights_line1="local  all     all                                        password"
rights_line2="host   all     all    127.0.0.1         255.255.255.255   password"
sudo bash -c "sudo echo \"# end of lines added by the gisgraphy installation script\" | cat - /etc/postgresql/$postgres_version/main/pg_hba.conf > /tmp/gisgraphytemp &&  mv /tmp/gisgraphytemp /etc/postgresql/$postgres_version/main/pg_hba.conf"
sudo bash -c "sudo echo \"$rights_line1\" | cat - /etc/postgresql/$postgres_version/main/pg_hba.conf > /tmp/gisgraphytemp &&  mv /tmp/gisgraphytemp /etc/postgresql/$postgres_version/main/pg_hba.conf"
sudo bash -c "sudo echo \"$rights_line2\" | cat - /etc/postgresql/$postgres_version/main/pg_hba.conf > /tmp/gisgraphytemp &&  mv /tmp/gisgraphytemp /etc/postgresql/$postgres_version/main/pg_hba.conf"
sudo bash -c "sudo echo \"# those lines are added by the gisgraphy installation script\" | cat - /etc/postgresql/$postgres_version/main/pg_hba.conf > /tmp/gisgraphytemp &&  mv /tmp/gisgraphytemp /etc/postgresql/$postgres_version/main/pg_hba.conf"
sudo bash -c "/etc/init.d/postgresql-$postgres_version restart"
}

function create_and_configure_database {
	read -p "Please enter the IP or the host name of the postgres serveur [$postgres_default_server] " postgres_server_typed
		if [[  $postgres_server_typed == "" ]]
		then
			postgres_server=$postgres_default_server
		else 
			postgres_server=$postgres_server_typed		
		fi

	read -p "Please enter the name of the postgres user you want to use [$postgres_default_user] " postgres_user_typed
		if [[ $postgres_user_typed == "" ]]
		then
			postgres_user=$postgres_default_user
		else 
			postgres_user=$postgres_user_typed		
		fi

	read -p "Please enter the name of the postgres password you want to use [The sql password you've type before] " postgres_password_typed
		if [[ $postgres_password_typed == "" ]]
		then
			postgres_password=$postgresPassword	
		else 
			postgres_password=$postgres_password_typed
		fi

	read -p "Please enter the name of the database [$postgres_default_database] " postgres_database_typed
		if [[ $postgres_database_typed == "" ]]
		then
			postgres_database=$postgres_default_database
		else
			postgres_database=$postgres_database_typed
		fi

echo "creating the gisgraphy database"
psql -U $postgres_user  -h $postgres_server -c "CREATE DATABASE $postgres_database WITH TEMPLATE = template1 ENCODING = 'UTF8';"
echo "create plpgsql lang"
createlang -U $postgres_user -h $postgres_server plpgsql $postgres_database 
echo "initializing Postgis function"
if [ -e "/usr/share/postgresql-$postgres_version-postgis/lwpostgis.sql" ] 
then 
	psql -U $postgres_user -h $postgres_server -d  $postgres_database -f /usr/share/postgresql-$postgres_version-postgis/lwpostgis.sql 
fi

if [ -e /usr/share/postgresql-$postgres_version-postgis/spatial_ref_sys.sql ] 
then 
	psql -U $postgres_user -h $postgres_server -d  $postgres_database -f /usr/share/postgresql-$postgres_version-postgis/spatial_ref_sys.sql 
fi

if [ -e /usr/share/postgresql/$postgres_version/contrib/postgis.sql ] 
then 
	psql -U $postgres_user -h $postgres_server -d  $postgres_database -f /usr/share/postgresql/$postgres_version/contrib/postgis.sql
fi

if [ -e /usr/share/postgresql/$postgres_version/contrib/postgis.sql ] 
then
 psql -U $postgres_user -h $postgres_server -d  $postgres_database -f /usr/share/postgresql/$postgres_version/contrib/spatial_ref_sys.sql
fi

echo "creating Tables..."
psql -U $postgres_user -h $postgres_server -d  $postgres_database -f ./sql/create_tables.sql

echo "inserting administration users"
psql -U $postgres_user -h $postgres_server -d  $postgres_database -f ./sql/insert_users.sql

echo "#################################################"	 
echo "####   Summary of database settings      ########"
echo "#################################################"
echo ""
echo "Postres server : $postgres_server"
echo "Postres user : $postgres_user"
echo "Postres password : $postgres_password"


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
echo "3 : Install Java if not installed"
echo "4 : Create and initialize Gisgraphy database"
echo ""
read -p "Do you want to continue [Y/n] " installgisgraphy

if [[ $installgisgraphy == "y" || $installgisgraphy == "Y" || $installgisgraphy == "" ]]
then
	echo "Here we go!";
	check_psql_installed ;
	install_java;
	configure_postgres_password
	create_and_configure_database
else
echo "End of the process, if you want to install Gisgraphy by hand, please see the documentation."
fi
}

confirm_gisgraphy_installation
