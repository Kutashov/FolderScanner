set scan_folder=""
set handled_folder=""
set scan_period=0
set jar=build/libs/folderscanner-all-1.0-SNAPSHOT.jar

java -jar %jar% -m %scan_folder% -p %scan_period%  -h %handled_folder%