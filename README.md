# README #

"tollbridge" is a test bed for DNP3 (SCADA Network Protocol).
It implements :
* a control center,
* a Lego EV3 bridge, (Not Yet)
* a Lego EV3 toll, (Not yet)
* a Simulated toll.

### How do I get set up? ###

help for installation of Lejos environnement on : http://thinkbricks.net/?p=826

A. Configuration of Home Computer
	1. Configure Eclipse as mentionned by Lejos
	2. Git clone project
	3. Open project in eclipse

B. Configuration of EV3 :
	1. Follow instructions to start Lejos :
		a. prepare SD card (sd500.img)
		b. copy Lejos files
		c. copy Embedded JRE
		d. Start EV3 with Micro SD
		e. Proceed to configuration (see FAQ)
	2. Configure Lejos
		a. connect to Lejos (USB/Bluetooth/WIFI)
			after having plugged the EV3 :
			ssh root@10.0.1.1
		b. configure correct IP
	3. Configure Toll/Bridge Device
		a. with Eclipse, compile and upload 
		b. edit Device.ini to satisfy parameters
		c. launch tollbridge.jar (by EV3 or by Eclipse)

C. Configuration of Control Center :
	1. with Eclipse, compile and launch "ControlCenter"


FAQ :
* How to change IP address/range

	1. Login on EV3 :
		# ssh root@10.0.1.1
	2. edit /home/root/lejos/bin/netaddress
	    # change IP address
	3. edit /home/root/lejos/bin/udhcpd.conf
	    # change IP range fo the DHCP server
	4. restart EV3
		# reboot

### With Ant ###
[[http://eclipsedriven.blogspot.fr/2011/08/how-to-fix-ant-build-error-not-load.html|Debian configuration]]


### Opendnp3 1.1.x - Compile for Debian ###
<code>
apt-get install libboost-all-dev
cd dnp-1.1.x
autoconf -f -i
./configure
make -j 3
make install
</code>

### Opendnp3 1.1.x - Cross Compiling ###
Compiling app for EV3

Environment for cross compiling : [[https://wiki.debian.org/EmdebianToolchain|Confi help]]
Add repositories
<code>
sudo apt-get install g++-4.4-arm-linux-gnueabi xapt
</code>
Includes are located in /usr/arm-linux-gnueabi/include

Libs environment for opendnp3
<code>
sudo xapt -a armel -m libboost-all-dev
</code>
Libs are installed in /usr/arm-linux-gnueabi/lib/

Compile opendnp3
<code>
cd dnp-1.1.x
autoconf -f -i
env CPPFLAGS="-I/usr/arm-linux-gnueabi/include" ./configure --host=arm-linux-gnueabi --build=x86_64-linux-gnu --with-boost-libdir=/usr/arm-linux-gnueabi/lib LDFLAGS="-lpthread" CXXFLAGS=-Os
make -j 3
</code>

Copy files to the EV3
<code>
/usr/arm-linux-gnueabi/lib/libboost_system.so.1.49.0
/usr/arm-linux-gnueabi/lib/libboost_prg_exec_monitor.so.1.49.0
/usr/arm-linux-gnueabi/lib/libboost_unit_test_framework.so.1.49.0

scp * root@toll1:
</code>

Sur EV3 :
<code>
mv libboost_* /usr/lib
cd /usr/lib
ln -s libboost_system.so.1.49.0 libboost_system.so
</code>

Idem avec .libs compilé (libopendnp3)

Idem avec .jar compilé
<code>
scp *.jar root@toll1:
</code>




