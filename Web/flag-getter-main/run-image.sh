#!/usr/bin/env bash

IMAGE_NAME="flag-getter"
CONTAINER_NAME="flag-getter"

docker container stop $CONTAINER_NAME
docker container rm $CONTAINER_NAME
docker run --name $CONTAINER_NAME -p 8080:8080 $IMAGE_NAME