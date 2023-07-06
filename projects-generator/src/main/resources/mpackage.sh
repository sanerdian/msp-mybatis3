#!/bin/bash
cd /data/proj1/src/projects-generated
mvn package -Dmaven.test.skip=true
mv /data/proj1/src/projects-generated/target/jnetdata-msp-generated-1.0-SNAPSHOT.jar /data/web/WEB-INF/lib/jnetdata-msp-generated-1.0-SNAPSHOT.jar