#!/bin/sh
#transformserver.sh
#To start or stop transformserver.

#base dir of the application
APP_BASE=`pwd`
echo $APP_BASE

#name of the application
APP_NAME=com.update.UpdateServer
echo $APP_NAME

#name of the config file
CONFIG_FILE=$APP_BASE/update.xml
echo $CONFIG_FILE

#name of the file record the process id of the application
PROCESS_ID_FILE=$APP_BASE/update.pid
echo $PROCESS_ID_FILE

#process id of the application
PROCESS_ID=`cat $PROCESS_ID_FILE`
echo $PROCESS_ID

case "$1" in
    start)
        if [ "$PROCESS_ID" ]; then
            echo "PID file ($PROCESS_ID) found. Is $APP_NAME still running? Start aborted."
            exit 1
        fi
        
        PATH=$PATH
        for i in $APP_BASE/*.jar;
        do
            PATH="$PATH":$i
        done
        
        for i in $APP_BASE/lib/*.jar;
        do
            PATH="$PATH":$i
        done
        echo $PATH
        
        $JAVA_HOME/bin/java -server -cp "$PATH" "$APP_NAME" "$CONFIG_FILE" &
        #$JAVA_HOME/bin/java -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=12345,server=y,suspend=n -server -cp "$PATH" "$APP_NAME" "$CONFIG_FILE" &
        
        echo $! > $PROCESS_ID_FILE
        
        echo "$APP_NAME started!"
    ;;
    stop)
        if [ "$PROCESS_ID" ]; then
            kill "$PROCESS_ID"
            rm -rf $PROCESS_ID_FILE
            echo "----------------------the $APP_NAME been killed------------------"
        else
            echo "----------------------the $APP_NAME is not running----------------"
        fi
    ;;
    shutdown)
        if [ "$PROCESS_ID" ]; then
            kill -9 "$PROCESS_ID"
            rm -rf $PROCESS_ID_FILE
            echo "----------------------the $APP_NAME been shutdown------------------"
        else
            echo "----------------------the $APP_NAME is not running----------------"
        fi
    ;;
    *)
        echo "Usage: $0 start|stop"
    ;;
esac
exit 0
