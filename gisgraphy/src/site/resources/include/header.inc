<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">










<html>
  <head>
    <title>gisgraphy | Importer, geolocalisation and fulltext services for Geonames</title>
   <link rel="stylesheet" type="text/css" media="all" href="/css/maven-base.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/css/maven-theme.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/css/site.css" />
    <link rel="stylesheet" href="/css/print.css" type="text/css" media="print" />
        
        <META NAME="Author" CONTENT="David Masclet"/>
		<META NAME="Keywords" CONTENT="gisgraphy,geonames,fulltext search,geolocalisation,webservices,gazeteers,postgis,toponyms,lucene,solr,masclet"/>
		<META NAME="Description" CONTENT="Gisgraphy is a free, open source framework that provides access Geonames and ESRI features. It provides an importer to inject the data into a strongly typed Postgres / Postgis database and use them via 2 webservices or a java API"/>
    	<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <script type="text/javascript">
	function expand( item ) {
		var expandIt = document.getElementById( item );
		if( expandIt.style.display == "block" ) {
			expandIt.style.display = "none";
			expandIt.parentNode.className = "collapsed";
		} else {
			expandIt.style.display = "block";
			expandIt.parentNode.className = "expanded";
		}
	}
    </script>
      </head>
  <body>
    <div id="banner">
     <a href="http://www.gisgraphy.com/"><img src="/images/logos/logo_70.png" alt="Gisgraphy" title="gisgraphy" /><span class="gisgraphy">Gisgraphy</span></a>
      <p class="tagline">Importer, geolocalisation and fulltext services for Geonames</p>
      <div class="clear">
        <hr/>
      </div>
    </div>
    <div id="breadcrumbs">
          
  

  
    
  
  
            <div class="xleft">
        Last Published: 06 Jul 2008
                      </div>
            <div class="xright"><form action="http://www.gisgraphy.com/sitesearch.htm" id="cse-search-box">
  <div>
    <input type="hidden" name="cx" value="partner-pub-7203216364107204:d614hs-anxp" />
    <input type="hidden" name="cof" value="FORID:10" />
    <input type="text" name="q" size="31" />
    <input type="submit" name="sa" value="Search" />
  </div>
</form>
<script type="text/javascript" src="http://www.google.com/coop/cse/brand?form=cse-search-box&amp;lang=en"></script></div>
      <div class="clear">
        <hr/>
      </div>
    </div>
    <div id="leftColumn">
      &nbsp;&nbsp;<div id="navcolumn">
           
  

  
    
  
  
                   <ul id="menu0" style="display:block">
          </ul>
              <h5 onclick="expand('menu1')">Gisgraphy project</h5>
        <ul id="menu1" style="display:block">
              
    <li class="none">
                    <a href="/index.htm">Home</a>
	          </li>
              
    <li class="none">
                    <a href="/download/index.htm">Download</a>
	          </li>
              
    <li class="none">
                    <a href="/features.htm">Features plan / status</a>
	          </li>
              
    <li class="none">
                    <a href="/dump.htm">Dump</a>
	          </li>
              
    <li class="none">
                    <a href="/bench.htm">Bench</a>
	          </li>
              
    <li class="none">
                    <a href="/logos.htm">Logos</a>
	          </li>
              
    <li class="none">
                    <a href="http://gisgraphy.free.fr/mavenrepo">Maven repository</a>
	          </li>
              
    <li class="none">
                    <a href="/whouse.htm">Wo use Gisgraphy</a>
	          </li>
              
    <li class="none">
                    <a href="mailto:davidmasclet@gisgraphy.com">Contact</a>
	          </li>
              
    <li class="none">
                    <a href="http://en.wikipedia.org/wiki/GeoNames">About geonames</a>
	          </li>
          </ul>
              <h5 onclick="expand('menu2')">WebServices</h5>
        <ul id="menu2" style="display:block">
              
    <li class="none">
                    <a href="/documentation/index.htm#fulltextservice">Fulltext</a>
	          </li>
              
    <li class="none">
                    <a href="/documentation/index.htm#geolocservice">Geolocalisation</a>
	          </li>
              
    <li class="none">
                    <a href="http://services.gisgraphy.com/">Free access</a>
	          </li>
          </ul>
              <h5 onclick="expand('menu3')">Getting help</h5>
        <ul id="menu3" style="display:block">
              
    <li class="none">
                    <a href="/documentation/index.htm#installation">Installation</a>
	          </li>
              
    <li class="none">
                    <a href="/documentation/index.htm">User guide</a>
	          </li>
              
    <li class="none">
                    <a href="/faq.html">FAQ</a>
	          </li>
              
    <li class="none">
                    <a href="/glossary.html">Glossary</a>
	          </li>
              
    <li class="none">
                    <a href="/placetype.htm">Placetypes available</a>
	          </li>
              
    <li class="none">
                    <a href="/screenshots.htm">Screenshots</a>
	          </li>
              
    <li class="none">
                    <a href="/forum/">Forum</a>
	          </li>
          </ul>
              <h5 onclick="expand('menu4')">Project Documentation</h5>
        <ul id="menu4" style="display:block">
              
                    
              
            
            
      
            
      
            
      
            
      
            
      
            
      
            
      
            
      
              
            <li class="expanded">
                    <a onclick="expand('list20')" style="cursor:pointer">Project Information</a>
	                <ul id="list20" style="display:block">
	              
    <li class="none">
                    <a href="/index.html">About</a>
	          </li>
              
    <li class="none">
                    <a href="/integration.html">Continuous Integration</a>
	          </li>
              
    <li class="none">
                    <a href="/dependencies.html">Dependencies</a>
	          </li>
              
    <li class="none">
                    <a href="/issue-tracking.html">Issue Tracking</a>
	          </li>
              
    <li class="none">
                    <a href="/mail-lists.html">Mailing Lists</a>
	          </li>
              
    <li class="none">
                    <a href="/license.html">Project License</a>
	          </li>
              
    <li class="none">
                    <a href="/project-summary.html">Project Summary</a>
	          </li>
              
    <li class="none">
                    <a href="/team-list.html">Project Team</a>
	          </li>
              
    <li class="none">
                    <a href="/source-repository.html">Source Repository</a>
	          </li>
          </ul>
    </li>
              
                    
              
      
            
      
            
      
            
      
            
      
            
      
            
      
              
        <li class="collapsed">
                    <a onclick="expand('list30')" style="cursor:pointer">Project Reports</a>
	                <ul id="list30" style="display:none">
	              
    <li class="none">
                    <a href="/changes-report.html">Changes Report</a>
	          </li>
              
    <li class="none">
                    <a href="/cpd.html">CPD Report</a>
	          </li>
              
    <li class="none">
                    <a href="/apidocs/index.html">JavaDocs</a>
	          </li>
              
    <li class="none">
                    <a href="/surefire-report.html">Maven Surefire Report</a>
	          </li>
              
    <li class="none">
                    <a href="/pmd.html">PMD Report</a>
	          </li>
              
    <li class="none">
                    <a href="/xref/index.html">Source Xref</a>
	          </li>
              
    <li class="none">
                    <a href="/xref-test/index.html">Test Source Xref</a>
	          </li>
          </ul>
    </li>
          </ul>
                 
  

  
    
  
  
         <br/>
       <script type="text/javascript">
      		google_ad_client = "pub-7203216364107204";
		google_ad_slot = "2490879529";
		google_ad_width = 160;
		google_ad_height = 90;
	</script>
		<script type="text/javascript"
		src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
		</script>
       
<br/>
<br/><br/>
      </div>
    </div>
    <div id="bodyColumn">
      <div id="contentBox">
      <!-- end header -->