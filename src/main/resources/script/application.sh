#!/bin/bash
JAR_PATH="./../../../../target/cp-1.0-SNAPSHOT-jar-with-dependencies.jar"
SERVICE_NAME="copy-paste"
MEM_OPTS="-Xms3G -Xmx3G -Xmn2G"
JAVA_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:InitiatingHeapOccupancyPercent=50"
PID_PATH_NAME=/tmp/${SERVICE_NAME}-pid
LOG_FILE_NAME="-Dlog.file.name=cp-service"
JAVA_BIN_ARG="${JAVA_OPTS} ${MEM_OPTS} ${JMX_OPTS} ${APP_OPTS} ${LOG_FILE_NAME}"

start(){
  echo "starting process ${PID_PATH_NAME}"
    if [ ! -f ${PID_PATH_NAME} ]; then
      echo "argument detail ${JAVA_BIN_ARG}${JAR_PATH}"
      nohup java -jar ${JAVA_BIN_ARG} ${JAR_PATH} > /dev/null 2>&1 &
      echo $ > $PID_PATH_NAME

    else
      echo "process running"
    fi
}

stop(){
    
   if [ -f ${PID_PATH_NAME} ]; then
        echo " stopping process"
        pid=$(cat $PID_PATH_NAME)
        kill -9 pid
        if [ ! -z "$pid" ]; then
            kill -9 $pid
            rm ${PID_PATH_NAME}
        fi
   fi
}

case $1 in
   start)
      start
  ;;
  stop)
      stop
  ;;
  *) echo "usage $0 {start|stop|status}"
esac



