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

CURRDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
DIR="$CURRDIR/asena"
RUNSCRIPT="$DIR/run.sh"
APPLICATION_PROPERTIES_PATH="$DIR/.env"
GITHUB_BASE_URL="https://raw.githubusercontent.com/exAphex/asena/master"

function checkAsenaInstallationExists() {
    if [ ! -d "$DIR" ]
    then
        echo "Cannot find Asena installation at $DIR."
        exit 1
    fi
}

function checkAsenaInstallationDoesNotExist() {
    if [ -d "$DIR" ]
    then
        echo "Asena is already installed at $DIR."
        exit 1
    fi
}

function showCommands() {
cat << EOT
Available commands:
    install - starts a new installation routine
    build - builds local sources and starts installation routine
    start - starts the docker container
    restart - restarts the docker container
    stop - stops the docker container
    uninstall - deletes the container
    help
EOT
}

function prepareInstallationFolder() {
    checkAsenaInstallationDoesNotExist
    mkdir $DIR
}

function writeConfigFile() {
    DRIVER_CLASS_NAME="ASENA_DRIVER=org.postgresql.Driver"
    JDBC_URL="ASENA_JDBC_URL=jdbc:postgresql://db:5432/postgres"
    POSTGRES_USER="ASENA_JDBC_USER=asena"
    JPA_DIALECT="ASENA_JDBC_DIALECT=org.hibernate.dialect.PostgreSQL95Dialect"
    SECURITY_KEY="ASENA_KEY=$(openssl rand -base64 32)"
    LOGGING_LEVEL="ASENA_LOGLEVEL=ERROR"
    DEFAULT_SCHEMA="ASENA_SCHEMA=asena"
    DDL_AUTO="ASENA_VALIDATE=validate"
    VOLUME_PATH="ASENA_VOLUME_PATH=$DIR/volume"


    read -p "Choose a password for the database user (postgres): " POSTGRES_USER_PASSWORD
    read -p "Choose a port on which asena will run: " ASENA_PORT
    
    echo "ASENA_PORT=$ASENA_PORT" > $APPLICATION_PROPERTIES_PATH
    echo $DRIVER_CLASS_NAME >> $APPLICATION_PROPERTIES_PATH
    echo $JDBC_URL >> $APPLICATION_PROPERTIES_PATH
    echo $POSTGRES_USER >> $APPLICATION_PROPERTIES_PATH
    echo "ASENA_JDBC_PASSWORD=$POSTGRES_USER_PASSWORD" >> $APPLICATION_PROPERTIES_PATH
    echo $JPA_DIALECT >> $APPLICATION_PROPERTIES_PATH
    echo $DDL_AUTO >> $APPLICATION_PROPERTIES_PATH
    echo $SECURITY_KEY >> $APPLICATION_PROPERTIES_PATH
    echo $LOGGING_LEVEL >> $APPLICATION_PROPERTIES_PATH
    echo $DEFAULT_SCHEMA >> $APPLICATION_PROPERTIES_PATH
    echo $FLYWAY_SCHEMA >> $APPLICATION_PROPERTIES_PATH
    echo $VOLUME_PATH >> $APPLICATION_PROPERTIES_PATH 
}

function downloadLatestVersion() {
    echo "=================================="
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
    echo "Release file downloaded successfully!"
    downloadDockerFiles
}

function copyLocalArtifact() {
    echo "=================================="
    echo "Building file from sources..."
    cd $CURRDIR/../
    mvn clean package -DskipTests -q
    cd $CURRDIR
    mv $CURRDIR/../scimgateway/target/scimgateway.jar $DIR/asena.jar
    echo "File built successfully!"
    copyLocalDockerFiles
}

function copyLocalDockerFiles() {
    echo "=================================="
    echo "Copying local docker files to installation directory..."
    cp $CURRDIR/Dockerfile $DIR/Dockerfile
    cp $CURRDIR/docker-compose.yml $DIR/docker-compose.yml
    cp $CURRDIR/run.sh $RUNSCRIPT
    chmod u+x $RUNSCRIPT
    echo "Done!"
}

function downloadDockerFiles() {
    echo "=================================="
    echo "Downloading latest docker files..."
    curl -s -o $DIR/Dockerfile $GITHUB_BASE_URL/scripts/Dockerfile
    curl -s -o $DIR/docker-compose.yml $GITHUB_BASE_URL/scripts/docker-compose.yml
    curl -s -o $RUNSCRIPT $GITHUB_BASE_URL/scripts/run.sh
    chmod u+x $RUNSCRIPT
    echo "Download successfull!"
}

function installationComplete() {
    echo "=================================="
    echo "INSTALLATION COMPLETE"
    echo "=================================="
}


case $1 in
    "install")
        prepareInstallationFolder
        writeConfigFile
        downloadLatestVersion
        installationComplete
        ;;
    "build")
        prepareInstallationFolder
        writeConfigFile
        copyLocalArtifact
        installationComplete
        ;;
    "start" | "restart")
        cd $DIR
        $RUNSCRIPT start
        ;;
    "stop")
        cd $DIR
        $RUNSCRIPT stop
        ;;
    "uninstall")
        cd $DIR
        $RUNSCRIPT stop
        rm -rf $DIR
        ;;
    *)
        showCommands
esac
