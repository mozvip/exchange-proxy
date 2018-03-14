# exchange-proxy

An ActiveSync proxy to use between any device and an Exchange EWS server.

It can alter various things on the fly.

It is implemented in Java and is a standalone app (it uses DropWizard).

# Installation

## Server setup

Install git if you don't have it:

    sudo apt-get install git

Install the Jdk 8 if you don't have it:

    sudo apt-get install openjdk-8-jdk-headless

Install a text editor if you need one:

    sudo apt-get install nano

Create a user to execute the process

    sudo adduser --system --shell /bin/bash --gecos 'User for managing the Exchange Proxy' --group --disabled-password --home /home/exchange-proxy exchange-proxy

## Certificate installation

It is highly probable you want to use a certificate to secure data exchange from devices to the proxy.

    sudo add-apt-repository ppa:certbot/certbot
    sudo apt-get update

Because I'm using nginx:

    sudo apt-get install python-certbot-nginx
    sudo certbot --nginx --register-unsafely-without-email -d host.example.com

### Test

Ensure that you can access https://host.example.com successfully with no certificate error.


## Installation of exchange-proxy

    su exchange-proxy
    cd
    git clone https://github.com/mozvip/exchange-proxy.git

You then build the jar file with the provided maven wrapper:

    chmod +x ./mvnw
    ./mvnw

## Configuration

Edit `defaultConfig.yml` to fit your needs.


# Running

Start it with 

    java -jar target/exchange-proxy-0.1.0.jar server defaultConfig.yml
    

