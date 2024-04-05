#! /bin/sh

cd config
mvn clean install -DskipTests
cd ..

cd discovery
mvn clean install -DskipTests
cd ..

cd gateway
mvn clean install -DskipTests
cd ..

cd user
mvn clean install -DskipTests
cd ..

#cd user-api
#mvn clean install -DskipTests
#cd ..
#
#cd user-jwt-parser
#mvn clean install -DskipTests
#cd ..
#
#cd jwt-util
#mvn clean install -DskipTests
#cd ..
#
#cd jwt-helper-webflux
#mvn clean install -DskipTests
#cd ..
#
#cd email
#mvn clean install -DskipTests
#cd ..
#
#cd email-api
#mvn clean install
#cd ..
