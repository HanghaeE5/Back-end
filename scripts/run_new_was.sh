#!/bin/bash

CURRENT_PORT=$(cat /home/ubuntu/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> Current port of running WAS is ${CURRENT_PORT}."

if [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  TARGET_PORT=8081
else
  echo "> No WAS is connected to nginx"
fi

TARGET_PID=$(lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

if [ ! -z ${TARGET_PID} ]; then
  echo "> Kill WAS running at ${TARGET_PORT}."
  sudo kill -9 ${TARGET_PID}
fi

REPOSITORY=/home/ubuntu/Back-end
# shellcheck disable=SC2164
cd $REPOSITORY

APP_NAME=Back-end #1
# shellcheck disable=SC2010
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

nohup java -jar -Dserver.port=${TARGET_PORT} ${JAR_PATH} &
#nohup java -jar -Dserver.port=${TARGET_PORT} ${JAR_PATH} > /home/ubuntu/Back-end/nohup.out 2>&1 &
echo "> Now new WAS runs at ${TARGET_PORT}."
exit 0