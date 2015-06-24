#!/bin/sh
SERVICE_NAME=labspy_client
PATH_TO_JAR=/var/lib/LabSpy/Student.jar
PID_PATH_NAME=/tmp/labspy_client_pid
USER_AUTHORITY_PATH="$HOME/.Xauthority"
case $1 in
    start)
        echo "Starting $SERVICE_NAME ..."
        if [ ! -f $PID_PATH_NAME ]; then
            export DISPLAY=:0
            export XAUTHORITY=$USER_AUTHORITY_PATH
            until xwininfo -root > /dev/null
            do
              sleep 5
            done
            nohup java -jar $PATH_TO_JAR /tmp 2>> /var/log/labspy_stdout >> /var/log/labspy_stderr &
                        echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is already running ..."
        fi
    ;;
    stop)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stoping ..."
            kill $PID;
            echo "$SERVICE_NAME stopped ..."
            rm $PID_PATH_NAME
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
    restart)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stopping ...";
            kill $PID;
            echo "$SERVICE_NAME stopped ...";
            rm $PID_PATH_NAME
            echo "$SERVICE_NAME starting ..."
            export DISPLAY=:0
            export XAUTHORITY=$USER_AUTHORITY_PATH
            until xwininfo -root > /dev/null
            do
              sleep 5
            done
            nohup java -jar $PATH_TO_JAR /tmp 2>> /dev/null >> /dev/null &
                        echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
esac 
