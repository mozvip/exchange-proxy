#!/bin/sh
cd /home/exchange-proxy
if [ -f "exchange-proxy.pid" ]
then
	echo "Killing currently running exchange-proxy process"
	PID=`cat exchange-proxy.pid`
	running=`ps -o pid= -p $PID`
	if [ $running ]; then
		kill -9 $PID
	fi
	rm exchange-proxy.pid
fi
