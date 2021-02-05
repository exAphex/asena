#!/bin/sh
mvn clean package -DskipTests
rm -rf ./build
mkdir build
cp ./scimgateway/target/scimgateway.jar ./build/asena.jar
cp ./installer/target/installer.jar ./build/install.jar
cp LICENSE ./build/LICENSE
zip -r asena.zip -r ./build
