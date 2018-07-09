# FolderScanner

App scans custom folder, searching for xml files with a given pattern:

```xml
<Entry>
    <!--string up to 1024 symbols-->
    <content>Record content</content>
    <!--content creation date-->
    <creationDate>2014-01-01 00:00:00</creationDate>
</Entry>
```

Proper records are written to db and files are moved other custom folder.

## Steps to run:
1. Build app with **gradlew buildJar** command
2. Create a table for records with **entries_postgresql.sql** script.
   Expected DB credentials are in **database.properties**
3. Run either start.bat or start.sh script. Remember to fix required params