 #!/bin/bash
 
 dataBkUp="/data/"
 FilesToSave[0]="abuse.txt";
 FileToSave[1]=""
 
 check_vars{
 if [[ -z $SITE_HOME ]]
 then
 echo "please set the SITE_HOME directory first (must ends with a slash) : "
 read site_home
 export SITE_HOME="$site_home" 
 echo "SITE_HOME directory has been set to $SITE_HOME"
 fi
 
 if [[ -z $SAVE_SITE_HOME ]]
 then
 echo "please set the SAVE_SITE_HOME directory first : "
 read save_site_home
 export SAVE_SITE_HOME="$save_site_home" 
 echo "SAVE_SITE_HOME directory has been set to $SAVE_SITE_HOME"
 fi
 }
 
function save_site_data {
	`mkdir $SAVE_SITE_HOME$dataBkUp`
	date=`date "+%Y-%m-%dT%H:%M:%S"`
	#ending slash of $SITE_HOME is important
	rsync -aP --delete --link-dest=$SAVE_SITE_HOME/Backups/current SITE_HOME/ $SAVE_SITE_HOME/Backups/back-$date
	rm -f $HOME/Backups/current
	ln -s $SAVE_SITE_HOME/Backups/back-$date $SAVE_SITE_HOME/Backups/current
}

function dump_site_data {
echo "dump_site_data is not implemented yet "
}

 check_vars
 save_site_data
 dump_site_data
