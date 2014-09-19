# README #

**tollbridge** is a test bed for DNP3 (SCADA Network Protocol).
It implements :
* a control center,
* a Lego EV3 bridge, (Not Yet)
* a Lego EV3 toll, (Not yet)
* a Simulated toll.

## References ##
* EV3 API [Lejos](http://www.lejos.org)
* DNP3 API [opendnp3](http://www.automatak.com/opendnp3/)

## How do I get set up EV3? ##

help for installation of Lejos environnement on : http://thinkbricks.net/?p=826

1. Configuration of Home Computer
	1. Configure Eclipse as mentionned by Lejos
	2. Git clone project
	3. Open project in eclipse

2. Configuration of EV3 :
	1. Follow instructions to start Lejos :
		a. prepare SD card (sd500.img)
		b. copy Lejos files
		c. copy [Embedded JRE](http://java.com/legomindstorms)
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

3. Configuration of Control Center :
	1. with Eclipse, compile and launch "ControlCenter"


## FAQ ##
* How to change IP address/range

	1. Login on EV3 :
```
# ssh root@10.0.1.1
```
	2. edit /home/root/lejos/bin/netaddress
```
	    # change IP address
```
	3. edit /home/root/lejos/bin/udhcpd.conf
```
	    # change IP range fo the DHCP server
```
	4. restart EV3
```
		# reboot
```

## With Ant ##
[Debian configuration](http://eclipsedriven.blogspot.fr/2011/08/how-to-fix-ant-build-error-not-load.html)
### Compiling and launching Control Center ###
```
ant -f build_CC.xml jar
./cc.sh
```

### Compiling and launching Sim Toll ###
```
ant -f build_RTU_EV3.xml jar
./simtoll.sh
```

### Compiling and launching EV3 Toll ###
```
ant -f build_RTU_EV3.xml copy
```
On the EV3 :
TODO Fill the command 
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
Then copy the jar produced :
```
cp dnp-1.1.x/java/api/targets/*.jar dnp-1.1.x/java/bindings/targets/*.jar tollbridge-dnp3/lib
```



## Opendnp3 1.1.x - Cross Compiling ##
Compiling app for EV3
EV3 is armv5 instruction set

Environment for cross compiling : [Configuration help](https://wiki.debian.org/EmdebianToolchain)
Add repositories
```
sudo apt-get install g++-4.4-arm-linux-gnueabi xapt
```

Libs environment for opendnp3
```
sudo xapt -a armel -m libboost-all-dev 
??? sudo xapt -a armel -m openjdk-7-jdk 
??? sudo apt-get install openjdk-7-dbg
```

---autre solution
Get jdk from : http://www.oracle.com/technetwork/java/javase/downloads/jdk7-arm-downloads-2187468.html
Extract JDK-7 for ARM
cp ./includes/jni_md.h ./includes
env CPPFLAGS="-I/usr/arm-linux-gnueabi/include" ./configure --host=arm-linux-gnueabi --build=x86_64-linux-gnu --with-boost-libdir=/usr/arm-linux-gnueabi/lib LDFLAGS="-lpthread" CXXFLAGS=-Os --enable-java --with-jni-include-path=/path-ofjdk-extracted/includes

---fin autre


Libs are installed in /usr/arm-linux-gnueabi/lib/
Includes are located in /usr/arm-linux-gnueabi/include

Compile opendnp3
```
cd dnp-1.1.x
autoreconf -f -i
env CPPFLAGS="-I/usr/arm-linux-gnueabi/include" ./configure --host=arm-linux-gnueabi --build=x86_64-linux-gnu --with-boost-libdir=/usr/arm-linux-gnueabi/lib LDFLAGS="-lpthread" CXXFLAGS=-Os --enable-java
make -j 3
```

Copy files to the EV3
```
/usr/arm-linux-gnueabi/lib/libboost_system.so.1.49.0
/usr/arm-linux-gnueabi/lib/libboost_prg_exec_monitor.so.1.49.0
/usr/arm-linux-gnueabi/lib/libboost_unit_test_framework.so.1.49.0
scp * root@toll1:
```
Sur EV3 :
```
mv libboost_* /usr/lib
cd /usr/lib
ln -s libboost_system.so.1.49.0 libboost_system.so
```

Idem avec .libs compilé (libopendnp3.so et libopendnp3java.so)
Depuis environnement de compilation :
```
scp ./.libs/libopendnp3.so.1.0.1 ./.libs/libopendnp3java.so.1.0.1 root@10.0.10.11:/usr/lib
```
Depuis EV3 :
```
cd /usr/lib
ln -s libopendnp3java.so libopendnp3java.so.1.0.1
ln -s libopendnp3java.so.1 libopendnp3java.so.1.0.1
ln -s libopendnp3.so libopendnp3.so.1.0.1
ln -s libopendnp3.so.1 libopendnp3.so.1.0.1


```


Idem avec .jar compilé

```
scp *.jar root@toll1:
```
