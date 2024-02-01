#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/skyware/Back
cd $REPOSITORY

APP_NAME=Back
#JAR_NAME=$(ls $REPOSITORY/target/ | grep 'SNAPSHOT.jar' | tail -n 1)
#JAR_PATH=$REPOSITORY/target/$JAR_NAME
#echo "JAR_NAME: $JAR_NAME"
#echo "JAR_PATH: $JAR_PATH"

JAR_PATH=$(ls $REPOSITORY/target/*.jar | tail -n 1)
echo "JAR_PATH: $JAR_PATH"

echo "> 현재 구동중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -f $APP_NAME)

echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z $CURRENT_PID ]
then
  echo "> 종료할 애플리케이션이 없습니다."
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> Deploy - $JAR_PATH "
nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &