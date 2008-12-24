Here is the step to install Gisgraphy to get the best performances.



<h2>Introduction</h2>
Gisgraphy is composed of two webapp : 
<ul>
<li>The Gisgraphy webapp that contains the geoloc server and the two servlets (fulltext engine that talk to solR and geoloc search engine that is linked with postgis)</li>
<li>The fulltext search engine (Solr)</li>
</ul>

that's why gisgraphy is scalable. you can install it on several machines / webapps that are load balanced. the goal of this tutorial is to explain how to install it on one machine (you can easilly find some other tutorials that explain how to load balance some servers). we will use tomcat for the gisgraphy webapp and Jetty for the solR server. you may wonder why we use two separates servlets containers, the reason is that solr come with an easy way to launch solr and the second is to show too differents installations 

To get the best performances possible, Gisgraphy must be separate in to separate JVM (aka : Java Virtual machine)
<ul>
	<li>The first one for the solR intance (fulltext search engine)</li>
	<li>The second for the Gisgraphy</li>
</ul>

<h2>The postgres + postgis</h2>
	<h3>Install the debian / ubuntu package</h3>
open a shell and type :
<code>sudo apt-get install postgresql-8.3 postgresql-8.3-postgis </code>

<h3> Configure the user / password</h3>
By default the postgres user can connect to postgres with the same password as the Unix one, but we'd like to postgres to ask for a password and not use the UNIX one
1=>change postgres (unix) user

<code>sudo passwd postgres</code>

2=>log to unix with user postgres

<code>su - postgres </code>

and give the new password you've type

3=>check postgresql conf :

vi /etc/postgresql/8.3/main/pg_hba.conf

and edit it in order to have at least those 2 lines (if it is not the case)

local  all     all                                        ident sameuser
host   all     all    127.0.0.1         255.255.255.255   ident sameuser

4=> connect to postgres to change the postgresql password

<code>psql -d template1 -c "alter user postgres with password 'YOURPASSWORD'"</code>

5=>check postgres conf : type

 vi /etc/postgresql/8.3/main/pg_hba.conf
and edit in order to have

local  all     all                                        password
host   all     all    127.0.0.1         255.255.255.255   password
host    all         all         YOURHOST/32    password



restart db :
sudo service postgresql-8.3 restart



<h2>The Gisgraphy webapp</h2>




#POSTGRES

sudo apt-get install postgresql-8.3 postgresql-8.3-postgis nmap sysvconfig
sudo apt-get install  sysvconfig
sudo passwd postgres

su - postgres

sudo vi /etc/postgresql/8.3/main/pg_hba.conf
host 	all	 all 	YOURIP/24	password
local  all     all                                        ident sameuser
host   all     all    127.0.0.1         255.255.255.255   ident sameuser

vi /etc/postgresql/8.3/main/postgresql.conf

listen_addresses='YOURIP'

psql -d template1 -c "alter user postgres with password 'your_passwd'"





service postgresql-8.3 restart

sudo vi etc/hosts=>88.191.92.137 gisgraphy.com 

sudo iptables -A INPUT -p tcp -i eth0 --dport 5432 -d gisgraphy.com -j ACCEPT

#install jvm
sudo apt-get install sun-java6-bin sun-java6-fonts sun-java6-javadb sun-java6-jdk sun-java6-jre sun-java6-source ia32-sun-java6-bin 
valid screen (tab +ok)
accept license (TAB to select yes +OK)

java -version=>
java version "1.6.0_06"
Java(TM) SE Runtime Environment (build 1.6.0_06-b02)
Java HotSpot(TM) 64-Bit Server VM (build 10.0-b22, mixed mode)


sudo update-java-alternatives --set ia32-java-6-sun 
 update-alternatives: Impossible de trouver l'alternative « /usr/lib/jvm/ia32-java-6-sun/jre/plugin/i386/ns7/libjavaplugin_oji.so =>ok because no applet in 64 bits
 
 ls -l  /etc/alternatives/java
 
 /etc/alternatives/java -> /usr/lib/jvm/java-6-sun/jre/bin/java
 
 add this line to .bashrc
 export JAVA_HOME=/usr/lib/jvm/java-6-sun-1.6.0.06/
 
 execute
 source ~/.bashrc or logout and login
 
 type "echo $JAVA_HOME" to check  
/usr/lib/jvm/java-6-sun-1.6.0.06/

#tomcat
download the : <a href=http://mirror.mkhelif.fr/apache/tomcat/tomcat-5/v5.5.27/bin/apache-tomcat-5.5.27.tar.gz">tomcat distrib</a>
 
sudo  mv apache-tomcat-5.5.27.tar.gz /usr/local/; cd /usr/local
tar zxvf apache-tomcat-5.5.27.tar.gz; mv apache-tomcat-5.5.27 tomcat

sudo bash
adduser tomcat && addgroup tomcat; cd /usr/local/tomcat

 Enter new UNIX password: 
Retype new UNIX password: 

Enter the new value, or press ENTER for the default
	Full Name [tomcat]: Tomcat
	Room Number [tomcat]: 00
	Work Phone [00]: 00
	Home Phone [00]: 00
	Other []: 00
Is the information correct? [y/N] y


 chown -R tomcat:tomcat webapps; chmod -R 775 webapps
 
  usermod -aG tomcat www-data
  
  
  OPTIONAL : cd conf; chmod g+w server.xml
  cd /usr/share/tomcat-5.5.25/bin/; chmod 755 shutdown.sh startup.sh
  
  
  cd /usr/local/tomcat/bin/; chmod 755 shutdown.sh startup.sh
  
  
  vim /etc/apache2/workers.properties
  
  # This file provides minimal jk configuration properties needed to
# connect to Tomcat.
#
# We define a worker named ‘default’
#workers.tomcat_home=/usr/share/tomcat5.5/
workers.java_home=/usr/lib/jvm/java-6-sun-1.6.0.06/
ps=/
worker.list=default
worker.default.port=8009
worker.default.host=localhost
worker.default.type=ajp13
worker.default.lbfactor=1
  
sudo vim /etc/apache2/apache2.conf
in apache2.conf :

NameVirtualHost *:80

JkWorkersFile /etc/apache2/workers.properties
 #-------------------------------------------
  vim /etc/apache2/sites-available/000-default
  
  <VirtualHost *:80 >
        ServerAdmin davidmasclet@gisgraphy.com
        ServerName www.gisgraphy.com
        DocumentRoot /var/www/
        
        ErrorDocument 404 /404.php
        ErrorDocument 403 /403.php
        ErrorDocument 500 /500.php
        
        <Directory />
                AllowOverride None
                Options FollowSymLinks
                Order allow,deny
                Allow from all
        </Directory>
        <Directory /var/www/>
                Options Indexes FollowSymLinks MultiViews
                AllowOverride None
                Order allow,deny
                allow from all
        </Directory>

        ScriptAlias /cgi-bin/ /usr/lib/cgi-bin/
        <Directory "/usr/lib/cgi-bin">
                AllowOverride None
                Options +ExecCGI -MultiViews +SymLinksIfOwnerMatch
                Order allow,deny
                Allow from all
        </Directory>

        ErrorLog /var/log/apache2/error.log

        # Possible values include: debug, info, notice, warn, error, crit,
        # alert, emerg.
        LogLevel warn

        CustomLog /var/log/apache2/access.log combined
        ServerSignature On

    Alias /doc/ "/usr/share/doc/"
    <Directory "/usr/share/doc/">
        Options Indexes MultiViews FollowSymLinks
        AllowOverride None
        Order deny,allow
        Deny from all
        Allow from 127.0.0.0/255.0.0.0 ::1/128
    </Directory>

</VirtualHost>
  
  
  
  vim /etc/apache2/sites-available/services
  
<virtualHost *:80 >
ServerName services.gisgraphy.com
ServerAdmin davidmasclet@gisgraphy.com

JkMount /* default
DirectoryIndex index.jsp index.html
# Globally deny access to the WEB-INF directory
<LocationMatch ‘.*WEB-INF.*’>
AllowOverride None
Order allow,deny
allow from all
</LocationMatch>
</VirtualHost>



  
sudo a2ensite services
  
alias psqlg='psql -Upostgres '
alias tomcatlog='tail -f -n 200 /usr/local/tomcat/logs/catalina.out'
alias apachelog='tail -f -n 200 /var/log/apache2/error.log'
alias tomcatstart='cd /home/gisgraphy/services;/usr/local/tomcat/bin/startup.sh'
alias tomcatstop='/usr/local/tomcat/bin/shutdown.sh'
alias solrstart='cd /home/gisgraphy/services/;nohup ./launch.sh & ; solrlog'
alias solrlog='tail -f /home/gisgraphy/services/nohup.out'
alias psj='ps auxf |grep java'

iptables :
sudo iptables -A INPUT -p tcp -s 0/0  -d 88.191.92.137  --dport 8080 -m state --state NEW,ESTABLISHED -j REJECT
sudo iptables -A INPUT -p tcp -s 0/0  -d 88.191.92.137  --dport 8983 -m state --state NEW,ESTABLISHED -j REJECT




  

