#! /bin/sh

PID=`cat application.pid`
echo ${PID}

kill -15 ${PID}
rm application.pid
