#!/bin/bash
REPO="querky"
TAG="0.0.1"
IMAGE_NAME="$REPO:$TAG"
mvn clean install #-DskipTests
docker rmi -f "$(docker images -q $REPO)"
docker build -t "$IMAGE_NAME" .
docker-compose down
docker-compose up
