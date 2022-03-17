#!/bin/bash
REPO="querky"
TAG="0.0.1"
IMAGE_NAME="$REPO:$TAG"
mvn clean install
docker rmi -f "$(docker images -q $REPO)"
docker build -t "$IMAGE_NAME" .
docker run -it -p 8080:8080 "$IMAGE_NAME"
