##############################################
GEODATA COMPENDIUM
##############################################

Comprehensive guide for geodata, by Tryskell and Hasha.

I	- How to configure it
		a - Prerequisites
		b - Make it work
II	- Addendum

##############################################
I - How to configure it
##############################################

----------------------------------------------
a - Prerequisites
----------------------------------------------

* A 64bits Windows/Java JDK is a must-have to run server with geodata. Linux servers don't have the issue.
* The server can start (hardly) with -Xmx1800m. At least -Xmx2g is recommended.

----------------------------------------------
b - Make it work
----------------------------------------------

To make geodata working:
* unpack your geodata files into "/data/geodata" folder (or any other folder)
* open "/config/geoengine.properties" with your favorite text editor and then edit following configs:
	- GeoDataPath = set path to your geodata, if elsewhere than "/data/geodata"
	- GeoDataType = set the geodata format, which you are using
* [optional] scroll down to the bottom and quote the regions, which you don't want to load (e.g. custom servers with certain areas only)

##############################################
II - Addendum
##############################################

* A map named "Interlude-real-geodata.jpg", updated by RooT, is shared on "/data/geodata" folder.
* It shows all region names, regions in red aren't supported by L2 client and don't exist as L2OFF.
* You can use that map to easily track regions.
