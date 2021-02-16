#!/usr/bin/env bash
set -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

function dockerComposeDown() {
    if [ $(docker-compose ps | wc -l) -gt 2 ]; then
        docker-compose down
    fi
}

function prepareVolume() {
    if [ ! -d "$DIR/volume" ]
    then
        echo "Creating volume directory $DIR/volume"
        mkdir -p "$DIR/volume"
    fi
}

function dockerComposeUp() {
    prepareVolume
    docker-compose up -d
}

function restart() {
    dockerComposeDown
    dockerComposeUp
}

function stop() {
    dockerComposeDown
}

function build() {
    dockerComposeDown
    docker-compose build
}

case $1 in
    "start" | "restart")
        restart
        ;;
    "stop")
        stop
        ;;
    "build")
        build
        ;;
esac
