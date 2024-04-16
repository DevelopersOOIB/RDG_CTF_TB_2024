#!/usr/bin/env bash

IMAGE_NAME="flag-getter"
docker build -t $IMAGE_NAME .
docker image ls | grep $IMAGE_NAME