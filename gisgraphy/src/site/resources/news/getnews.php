<head>
<style type="text/css">
body {
color:#444444;
font-family:verdana,arial,helvetica,sans-serif;
font-size:76%;
line-height:1em;
text-align:left;
}
</style>
</head>
<body>
News from Gisgraphy<br/>
<hr/>
<div>
<?php
$version =$HTTP_GET_VARS["version"];
 ?>
Last news from gisgraphy : <br/>
<?php if ($version =="2.0-final"){ ?>
<ul>
<li><span style="color:#FF0000">Important news</span> : Due to a recent changes in geonames the importer will fail (no problems if the import is already done), see <a href="http://www.gisgraphy.com/forum/viewtopic.php?f=3&t=261" target="gisgraphy"> the post on gisgraphy forum</a> and the one on <a href="http://forum.geonames.org/gforum/posts/list/2140.page" target="gisgraphy">geonames one</a> to see how to esolve the problem.

</li>
<li><span style="color:#FF0000">Your version is <b>not</b> up to date. version 2.1 is <a href="http://www.gisgraphy.com/download" target="gisgraphy">available</a></span></li>
</ul>
<hr/>
<?php }else{ ?>
<ul>
<li>
Your version is up to date
</li>
</ul>
<?php } ?> 
</div>
</body>
