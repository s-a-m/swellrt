#!/bin/bash
VERSION=0.1.0-p2pv-SNAPSHOT

# Left URL and REPOID empty for local repo installation

#URL=http://archiva.comunes.org/repository/comunes-internal/
URL=http://grasia.fdi.ucm.es/p2pvalue/artifactory/p2pvalue-snapshot/
#REPOID=comunes-internal
REPOID=p2pvalue-snapshot

#ant dist-api dist-libraries dist-proto dist-pst dist-robot-client-api dist-pst-dep dist-pst dist-server-dep dist-server 
for i in `cat kune-artifacts-alone.txt` 
do 
  cd dist
  if [ -z $URL ]; then
  mvn install:install-file -DgroupId=org.waveprotocol -DartifactId=$i -Dversion=$VERSION -Dfile=$i.jar -Dpackaging=jar 
  else
  mvn deploy:deploy-file -DgroupId=org.waveprotocol -DartifactId=$i -Dversion=$VERSION -Dfile=$i.jar -Dpackaging=jar -Durl=$URL -DrepositoryId=$REPOID
  fi	
  cd ..
done

