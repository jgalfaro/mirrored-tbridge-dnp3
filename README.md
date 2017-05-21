Tollbridge testbed for [DNP3](http://en.wikipedia.org/wiki/DNP3). It implements:

* a control center,
* a Lego EV3 bridge, 
* a Lego EV3 toll, 
* a Simulated toll.

## References ##
* EV3 API [Lejos](http://www.lejos.org)
* DNP3 API [opendnp3](http://www.automatak.com/opendnp3/)

## How to set up the EV3 bricks? ##

Check this out: [http://thinkbricks.net/?p=826](http://thinkbricks.net/?p=826)

```
1. Home Computer configuration: 
	1. Configure Eclipse as mentionned by Lejos.
	2. Clone the project.
	3. Open the project in eclipse.

2. EV3 configuration:
	1. Follow the instructions to start Lejos:
		a. prepare SD card (sd500.img)
		b. copy Lejos files
		c. copy Embedded JRE at http://java.com/legomindstorms
		d. Start EV3 with the Micro SD card
		e. Proceed to configuration (see FAQ)
	2. Configure Lejos
		a. connect to Lejos (USB/Bluetooth/WIFI)
			after having plugged the EV3:
			ssh root@10.0.1.1
		b. configure the IP address
	3. Configure the Toll/Bridge Device
		a. with Eclipse, compile and upload 
		b. edit Device.ini to satisfy parameters
		c. launch tollbridge.jar (at the EV3 brick, or at Eclipse)

3. Control Center configuration:
	1. with Eclipse, compile and launch "ControlCenter"
```

## FAQ ##
* How to change IP address/range
```
	1. Login on EV3 :
		# ssh root@10.0.1.1
	2. edit /home/root/lejos/bin/netaddress
	    # change IP address
	3. edit /home/root/lejos/bin/udhcpd.conf
	    # change IP range fo the DHCP server
	4. restart EV3
		# reboot
```

## With Ant ##
[Debian configuration](http://eclipsedriven.blogspot.fr/2011/08/how-to-fix-ant-build-error-not-load.html)

### Compiling and launching ControlCenter ###
```
ant -f build_CC.xml jar
./cc.sh
```

### Compiling and launching SimToll ###
```
ant -f build_RTU_EV3.xml jar
./simtoll.sh
```

### Compiling and launching EV3 Toll ###
```
ant -f build_RTU_EV3.xml copy
```
At the EV3:
```
jrun tollbrige-dnp3
```
## Opendnp3 1.1.x - Compile for Debian ##

**Compile Opendnp3**
```
sudo apt-get install libboost-all-dev autoconf libtool g++ m4 automake
cd dnp-1.1.x/
autoreconf -f -i
./configure --enable-java --with-javac=/usr/bin/javac
make -j 3
sudo make install
```

**Compile Java Bindings**
```
sudo apt-get install maven
export JAVA_HOME=/usr/lib/jvm/java-6-openjdk-amd64
cd dnp-1.1.x/java
mvn package
mvn install
```
Then copy the jar:
```
cp dnp-1.1.x/java/api/targets/*.jar dnp-1.1.x/java/bindings/targets/*.jar tollbridge-dnp3/lib
```

## Opendnp3 1.1.x - Cross Compiling ##
Compiling app for EV3
EV3 is armv5 instruction set

Environment for cross compiling: [Configuration help](https://wiki.debian.org/EmdebianToolchain)
Add repositories
```
sudo apt-get install g++-4.4-arm-linux-gnueabi xapt
```

Libs environment for opendnp3
```
sudo xapt -a armel -m libboost-all-dev 
sudo xapt -a armel -m openjdk-7-jdk 
sudo apt-get install openjdk-7-dbg

---2nd solution
Get jdk from : http://www.oracle.com/technetwork/java/javase/downloads/jdk7-arm-downloads-2187468.html
Extract JDK-7 for ARM
cp ./includes/jni_md.h ./includes
env CPPFLAGS="-I/usr/arm-linux-gnueabi/include" ./configure --host=arm-linux-gnueabi --build=x86_64-linux-gnu --with-boost-libdir=/usr/arm-linux-gnueabi/lib LDFLAGS="-lpthread" CXXFLAGS=-Os --enable-java --with-jni-include-path=/path-ofjdk-extracted/includes
---2nd solution

Libs are installed at /usr/arm-linux-gnueabi/lib/
Includes are located at /usr/arm-linux-gnueabi/include

Compile opendnp3
==================
cd dnp-1.1.x
autoreconf -f -i
env CPPFLAGS="-I/usr/arm-linux-gnueabi/include" ./configure --host=arm-linux-gnueabi --build=x86_64-linux-gnu --with-boost-libdir=/usr/arm-linux-gnueabi/lib LDFLAGS="-lpthread" CXXFLAGS=-Os --enable-java
make -j 3


Copy files to the EV3
====================================
/usr/arm-linux-gnueabi/lib/libboost_system.so.1.49.0
/usr/arm-linux-gnueabi/lib/libboost_prg_exec_monitor.so.1.49.0
/usr/arm-linux-gnueabi/lib/libboost_unit_test_framework.so.1.49.0
scp * root@toll1:

At the EV3 brick:
==================
mv libboost_* /usr/lib
cd /usr/lib
ln -s libboost_system.so.1.49.0 libboost_system.so

scp ./.libs/libopendnp3.so.1.0.1 ./.libs/libopendnp3java.so.1.0.1 root@10.0.10.11:/usr/lib

cd /usr/lib
ln -s libopendnp3java.so libopendnp3java.so.1.0.1
ln -s libopendnp3java.so.1 libopendnp3java.so.1.0.1
ln -s libopendnp3.so libopendnp3.so.1.0.1
ln -s libopendnp3.so.1 libopendnp3.so.1.0.1

scp *.jar root@toll1:
```
