#!/bin/bash
VERSION=0.1.0-p2pv-SNAPSHOT

# Leave URL and REPOID empty for local repo installation
URL=http://grasia.fdi.ucm.es/p2pvalue/artifactory/p2pvalue-snapshot/
REPOID=p2pvalue-snapshot

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

