#!/bin/bash
IMAGE_NAME="querky:0.0.1"
if [ ! -e .env ]; then
  cp .env-default .env
fi
mvn clean install #-DskipTests
docker-compose down -v #--rmi all
docker image rm -f "$IMAGE_NAME"
docker build -t "$IMAGE_NAME" .
docker-compose up
