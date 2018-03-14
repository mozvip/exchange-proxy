#!/bin/sh
cd /home/exchange-proxy
java -jar -server -Djava.awt.headless=true exchange-proxy-0.1.0.jar check config.yml&
