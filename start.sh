#!/bin/sh

scan_folder=""
handled_folder=""
scan_period=0
jar=build/libs/folderscanner-all-1.0-SNAPSHOT.jar

java -jar $jar -m $scan_folder -p $scan_period  -h $handled_folder