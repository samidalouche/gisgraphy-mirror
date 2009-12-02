You can download this data for free.  Due to Gisgraphy success, I must limit the bandwith and simultaneous download 
because the machine host the download server, forum, services, and the Gisgraphy sites.
 I try to keep a good SLA, but it cost me money and i don't plan to buy new machine.
 Thanks for your comprehension :)
The data are preprocess from Openstreetmap (http://www.openstreetmap.org/) dump in order to be inject with gisgraphy (http://www.gisgraphy.com), but you can you use it for anything else

This work is licensed under a Creative Commons Attribution 3.0 License,
see http://creativecommons.org/licenses/by/3.0/
The Data is provided "as is" without warranty or any representation of accuracy, timeliness or completeness.

The data format is tab-delimited text in utf8 encoding and compress with tar and bzip2

Each country have his own file with the following fields

CSV fields
==============================================
1 : id; UniqueId of the street(always null for the moment)
2 : name; The name 
3 : location; The middle point of the street in HEXEWKB
4 : length ; Length of the street in meters
5 : countrycode; The iso3166 Alpha2 Code of the country 
6 : gid ; GlobalId (not use yet)
7 : type; The type of street (see bellow)
8 : oneway; Whether the street is a one way street
9 : shape; The delimitation of the street in HEXEWKB 


Street type
============
BYWAY
MINOR
SECONDARY_LINK
CONSTRUCTION
UNSURFACED
BRIDLEWAY
PRIMARY_LINK
LIVING_STREET
TRUNK_LINK
STEPS
PATH
ROAD
PEDESTRIAN
TRUNK
MOTROWAY
CYCLEWAY
MOTORWAY_LINK
PRIMARY
FOOTWAY
TERTIARY
SECONDARY
TRACK
UNCLASSIFIED
SERVICE
RESIDENTIAL

more questions ?
forum : http://www.gisgraphy.com/forum/
Mail : davidmasclet@free.fr


