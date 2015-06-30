#!/bin/sh
### BEGIN INIT INFO
# Provides:          labspy_client
# Required-Start:    $local_fs $network $all
# Required-Stop:     $local_fs $network
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start LabSpy Student at boot time
# Description:       Start LabSpy Student version at boot time to monitor the student computer
### END INIT INFO
SERVICE_NAME=labspy_client
PATH_TO_JAR=/var/lib/LabSpy/Student.jar
PID_PATH_NAME=/tmp/labspy_client_pid
case $1 in
    start)
        echo "Starting $SERVICE_NAME ..."
        if [ ! -f $PID_PATH_NAME ]; then
            export DISPLAY=""
            while [ -z "$DISPLAY" ]; do
                export DISPLAY=`ps aux | grep 'X :' | grep -v grep | grep -E -i -w 'X\s+\:[[:digit:]]+' -o | sed 's/X //'`
                sleep 1
            done;
            
            export DISPLAY=`echo $DISPLAY | cut -d'.' -f1`
            export DISPLAY="$DISPLAY.0"
            export XAUTHORITY=""
            while [ -z "$XAUTHORITY" ]; do
                for USER in `who | cut -f1 -d " "`; do 
                    HM=$( getent passwd "$USER" | cut -d: -f6 )
                    XA="$HM/.Xauthority"
                    RES=`xauth -f $XA info | grep "File new:" | grep "no" | wc -l`
                    if [ "$RES" != "0" ]; then
                        export XAUTHORITY="$XA"
                        break;
                    fi;
                done;
            done;
            
            STATUS = 1
            while [ "$STATUS" != "0" ]; do
                nohup java -jar $PATH_TO_JAR /tmp 2>> /var/log/labspy_stdout >> /var/log/labspy_stderr &
                PID=$!
                echo $PID > $PID_PATH_NAME
                sleep 10
                kill -0 $PID
                STATUS=$?
            done
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
            
            export DISPLAY=""
            while [ -z "$DISPLAY" ]; do
                export DISPLAY=`ps aux | grep 'X :' | grep -v grep | grep -E -i -w 'X\s+\:[[:digit:]]+' -o | sed 's/X //'`
                sleep 1
            done;
            
            export DISPLAY=`echo $DISPLAY | cut -d'.' -f1`
            export DISPLAY="$DISPLAY.0"
            export XAUTHORITY=""
            while [ -z "$XAUTHORITY" ]; do
                for USER in `who | cut -f1 -d " "`; do 
                    HM=$( getent passwd "$USER" | cut -d: -f6 )
                    XA="$HM/.Xauthority"
                    RES=`xauth -f $XA info | grep "File new:" | grep "no" | wc -l`
                    if [ "$RES" != "0" ]; then
                        export XAUTHORITY="$XA"
                        break;
                    fi;
                done;
            done;
            
            STATUS = 1
            while [ "$STATUS" != "0" ]; do
                nohup java -jar $PATH_TO_JAR /tmp 2>> /var/log/labspy_stdout >> /var/log/labspy_stderr &
                PID=$!
                echo $PID > $PID_PATH_NAME
                sleep 10
                kill -0 $PID
                STATUS=$?
            done
            echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
esac 
