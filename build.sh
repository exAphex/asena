#!/bin/sh
mvn clean package
rm -rf ./build
mkdir build
cp ./scimgateway/target/scimgateway.jar ./build/asena.jar
cp ./installer/target/installer.jar ./build/install.jar
zip -r asena-$(git describe --tags --abbrev=0).zip -r ./build
