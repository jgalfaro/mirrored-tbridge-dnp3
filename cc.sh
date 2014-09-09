#!/bin/bash

java -Dfile.encoding=UTF-8 -classpath "./dist/tollbridge-dnp3-cc.jar:./lib/opendnp3-api-1.1.1.RC5.jar:./lib/opendnp3-bindings-1.1.1.RC5.jar" tollbridge.dnp3.master.ControlCenter
