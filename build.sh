#!/bin/bash
IMAGE_NAME="querky:0.0.1"
if [ ! -e .env ]; then
  echo 'OAUTH2_CLIENT_ID=dummy
OAUTH2_CLIENT_SECRET=dummy
POSTGRES_USER=sa
POSTGRES_PASSWORD=sa
POSTGRES_DB=querky-db' > .env
fi
mvn clean install #-DskipTests
docker-compose down -v --rmi all
docker build -t "$IMAGE_NAME" .
docker-compose up
