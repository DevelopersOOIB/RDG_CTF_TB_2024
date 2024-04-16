#!/usr/bin/env bash

IMAGE_NAME="hard-authorization-v2"

docker build -t $IMAGE_NAME .

# get image info
docker image ls | grep $IMAGE_NAME
