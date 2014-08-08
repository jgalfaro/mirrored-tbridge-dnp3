#!/bin/bash

#/usr/lib/jvm/java-7-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 -classpath "/home/user/workspace/tollbridge-dnp3/dist" tollbridge.dnp3.master.ControlCenter
#/usr/lib/jvm/java-7-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 -classpath "/home/user/workspace/tollbridge-dnp3/bin:/home/user/workspace/tollbridge-dnp3/lib/opendnp3-api-1.1.1.RC5.jar:/home/user/workspace/tollbridge-dnp3/lib/opendnp3-bindings-1.1.1.RC5.jar" tollbridge.dnp3.master.ControlCenter
/usr/lib/jvm/java-7-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 -classpath "/home/user/workspace/tollbridge-dnp3/dist:/home/user/workspace/tollbridge-dnp3/lib/opendnp3-api-1.1.1.RC5.jar:/home/user/workspace/tollbridge-dnp3/lib/opendnp3-bindings-1.1.1.RC5.jar" tollbridge.dnp3.master.ControlCenter
