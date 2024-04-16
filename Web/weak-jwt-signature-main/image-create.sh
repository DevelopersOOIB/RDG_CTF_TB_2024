#!/usr/bin/env bash

IMAGE_NAME="weak-jwt-signature"

docker build -t $IMAGE_NAME .

# get image info
docker image ls | grep $IMAGE_NAME
