#!/bin/sh
#tools.sh
#To start or stop tools.

#base dir of the application
APP_BASE=`pwd`

echo " ******choice tools: ********** "
echo " ****** 1--dts tools ********** "
echo " ****** 2--manager tools ****** "
echo " ****** 3--npc tools ********** "

read CHOICE

case "$CHOICE" in
1)
	APP_NAME=com.poker.dts.DTSDBTools
	;;
2)
	APP_NAME=com.poker.manager.ServerManager
	;;
*)
	APP_NAME=com.poker.dts.DTSDBTools
	;;
esac

echo $APP_NAME
#name of the file record the process id of the application
PROCESS_ID_FILE=$APP_BASE/tools.pid
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
        
        PATH=$CLASSPATH
        for i in $APP_BASE/*.jar;
        do
            PATH="$PATH":$i
        done
        
        for i in $APP_BASE/lib/*.jar;
        do
            PATH="$PATH":$i
        done
        echo $PATH

        $JAVA_HOME/bin/java -cp "$PATH" "$APP_NAME"
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
    *)
        echo "Usage: $0 start|stop"
    ;;
esac
exit 0
