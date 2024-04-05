#! /bin/sh

./mvn-install.sh
./docker-build.sh
docker-compose up -d
