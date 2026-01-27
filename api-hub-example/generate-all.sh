#!/bin/bash

data_model_src=`cat excel-data-model-source`
dest_dir=`pwd`/src
echo $dest_dir
mvn exec:java -Dexec.mainClass="co.altir.dbmanagement.dataaccess.openapidsl.devprocess.excelimport.ExcelDataModelConverter" \
-Dexec.args="$data_model_src $dest_dir\
 Data model package prefix:co.altir.example.model;\
 API Controller package prefix:co.altir.example.controller"\
 -Dtool -Dexec.cleanupDaemonThreads=false
mvn clean install