#!/bin/bash

DNAME=$(dirname "$PWD")
JAVA_HOME="/data/jdk1.8.0_65"
APP_LOG_HOME=${DNAME}/log
EXEC="${JAVA_HOME}/bin/java"
CLASSPATH="-cp ../lib/*:../config/"
JAVA_MAIN_CLASS="com.lujianbo.app.shadowsocks.Application"
JAVA_OPTS=""
eval "${EXEC} ${JAVA_OPTS} ${CLASSPATH} ${JAVA_MAIN_CLASS} $* >> ${APP_LOG_HOME}/out 2>&1 &"
