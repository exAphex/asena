#!/usr/bin/env bash
set -e

cat << "EOF"

    /\
   /  \    ___   ___  _ __    __ _
  / /\ \  / __| / _ \| '_ \  / _` |
 / ____ \ \__ \|  __/| | | || (_| |
/_/    \_\|___/ \___||_| |_| \__,_|

===================================================
Asena SCIM Gateway
by Aydin Tekin (exAphex)
https://github.com/exAphex/asena
===================================================

EOF

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
APPLICATION_PROPERTIES_PATH="$DIR/application.properties"

function writeConfigFile() {
    DRIVER_CLASS_NAME="spring.datasource.driverClassName=org.postgresql.Driver"
    JDBC_URL="spring.datasource.url=jdbc:postgresql://db:5432/postgres"
    POSTGRES_USER="spring.datasource.username=asena"
    JPA_DIALECT="spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQL95Dialect"
    SECURITY_KEY="com.asena.scimgateway.security.key=$(openssl rand -base64 32)"
    LOGGING_LEVEL="logging.level.root=ERROR"
    DEFAULT_SCHEMA="spring.jpa.properties.hibernate.default_schema=asena"
    FLYWAY_SCHEMA="spring.flyway.schemas=asena"
    DDL_AUTO="spring.jpa.hibernate.ddl-auto=validate"


    read -p "Choose a password for the database user (postgres): " POSTGRES_USER_PASSWORD
    read -p "Choose a port on which asena will run: " ASENA_PORT

    echo "server.port=$ASENA_PORT" > $APPLICATION_PROPERTIES_PATH
    echo $DRIVER_CLASS_NAME >> $APPLICATION_PROPERTIES_PATH
    echo $JDBC_URL >> $APPLICATION_PROPERTIES_PATH
    echo $POSTGRES_USER >> $APPLICATION_PROPERTIES_PATH
    echo "spring.datasource.password=$POSTGRES_USER_PASSWORD" >> $APPLICATION_PROPERTIES_PATH
    echo $JPA_DIALECT >> $APPLICATION_PROPERTIES_PATH
    echo $DDL_AUTO >> $APPLICATION_PROPERTIES_PATH
    echo $SECURITY_KEY >> $APPLICATION_PROPERTIES_PATH
    echo $LOGGING_LEVEL >> $APPLICATION_PROPERTIES_PATH
    echo $DEFAULT_SCHEMA >> $APPLICATION_PROPERTIES_PATH
    echo $FLYWAY_SCHEMA >> $APPLICATION_PROPERTIES_PATH
}

function downloadLatestVersion() {
    rm -rf $DIR/artifact
    echo "Resolving latest release file..."
    LATEST_ARTIFACT_URL=$(curl -s https://api.github.com/repos/exAphex/asena/releases/latest | grep browser_download_url | cut -d '"' -f 4)
    echo "Downloading latest release file..."
    curl -L $LATEST_ARTIFACT_URL > $DIR/artifacts.zip
    echo "Done. Unzipping..."
    unzip -qq $DIR/artifacts.zip -d $DIR/artifact
    mv $DIR/artifact/build/asena.jar $DIR/asena.jar
    rm -rf $DIR/artifact
    rm $DIR/artifacts.zip
    echo "File downloaded succesfully!"
}

function copyLocalArtifact() {
    cd $DIR/../
    mvn package -DskipTests
    cd $DIR
    mv $DIR/../scimgateway/target/scimgateway.jar $DIR/asena.jar
}

writeConfigFile
#downloadLatestVersion
copyLocalArtifact
