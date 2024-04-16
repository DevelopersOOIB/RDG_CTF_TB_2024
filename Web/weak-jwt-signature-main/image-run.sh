#!/usr/bin/env bash

IMAGE_NAME="weak-jwt-signature"
CONTAINER_NAME="weak-jwt-signature"

docker container stop $CONTAINER_NAME
docker container rm $CONTAINER_NAME
docker run --name $CONTAINER_NAME -p 8080:8080 -d $IMAGE_NAME
