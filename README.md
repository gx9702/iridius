# Iridius

[![Join the chat at https://gitter.im/iridius-server/community](https://badges.gitter.im/iridius-server/community.svg)](https://gitter.im/iridius-server/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Full OPC-UA server based on Milo and public device drivers.

## Discussing

I've opened a [Slack channel](https://iridius.slack.com) but any other
team workspace solution is welcome.

## Objectives of this project

We would like to assemble an OPC-UA server configurable and ready to use in a 
factory using all the wonderful components already available in the open source
community. 

More than a development project is a component selection and organization to make
them easy to use and integrated.

The core part of this system is [Milo](https://github.com/eclipse/milo), 
the open source OPC-UA stack (plus the client and server SDK). 

## Documentation

See the [Iridius Wiki pages](https://github.com/stefanolissa/iridius/wiki).



## Simulators and clients

For sure we need simulator to test the drivers or better to test the drivers integration
with the server.

### OPC-UA

- https://www.unified-automation.com/downloads/opc-ua-clients.html

### Modbus

- https://sourceforge.net/projects/modbuspal/
- http://www.plcsimulator.org/

### MQTT

- https://github.com/eclipse/paho.mqtt-spy

## Server Management User Interface

Probably at least a minimal user interface to manage the server is required. Of course
we need at least a light authentication system to start it.

I don't know if server could be controlled directly with an OPC-UA client, but probably it could.

## Installation

We need a way to install it for example as a service under Windows. There are many open source project
java based with good installers, for example Tomcat.

## Resources

- An interesting webinar by Kepware on the adavantage to have 
[protocol uniformity in accessing the manufaturing data](https://www.kepware.com/en-us/landing-pages/standardized-communications-layer-webinar/thank-you/)
