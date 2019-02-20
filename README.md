# opsi
Full OPC-UA server based on Milo and public device drivers

Note: opsi is just a temporary name, no checks have been made to see if it is a
copyrighted word or like. Assume this name will be changed.

## Discussing

I've opened a [Slack channel](https://opsigroup.slack.com) but any other
team workspace suggest is welcome.

## Objectives of this project

We would like to assemble an OPC-UA server configurable and ready to use in a 
factory using all the wonderful components already available in the open source
community. 

More than a development project is a component selection and organization to make
them easy to use and integrated.

The core part of this system is [Milo](https://github.com/eclipse/milo), 
the open source OPC-UA stack (plus the client and server SDK). 

Then we need drivers to communicate with devices. Fanuc, Siemens S7, Hakko,
Schneider, general Modbus and so on. This is the discovery part not yet started
and for which we need your help!

Of course a driver wrapper must be defined, so a driver can be loaded and
initialized by the server. At minimum, the driver should be able to be configured
to get and set values from the device and expose read and write methods that match
the write and read of OPC-UA.

Then there is the configuration part: we need to define the configuration used to
define the namespaces to create, the devices to connect to, the drivers to use and 
the specific configurations for those drivers.

Last we need to develop the real server or better that part of the server which loads
the configuration, creates the namespaces and the OPC-UA nodes. Here an important knowledge
of Milo and the OPC-UA standard is mandatory. Anyone?

## Adapters

Here a list of projects which could have interesting parts to build <a href="adapters.md">adapters</a>.

### Modbus

- https://sourceforge.net/projects/easymodbustcp-udp-java/

### Siemens S7

- http://snap7.sourceforge.net/

### Fanuc

I have some working Java code which is able to send a full g-code part program to a
Fanuc CNC and the machine status can be read with MT-Connect if the interface is available
or directly. The code is based on Fanuc official libraries and parts of the Android Java code
available with those libraries. Of course I cannot publish it here.

The Java code loads the Windows Fanuc DLLs or the Linux Fanuc library to send the part program,
while reading the machine status is more easy and can be done directly.

- https://www.inventcom.net/fanuc-focas-library/general/fwlib32

### CSV

Could sound old style but there are machines which expose data as CSV file. We need to read
it incrementally and expose the available fields. Probably this is the easy-to-write driver
(of course read only).

### MQTT

Again, is a machine is a publisher we can have a driver which is a subscriber for that machine and
expose the published data. Since OPC-UA can work in a publisher-subscriber fashion... to be explored.

### MT-Connect

A standard for machines, specially CNC, to expose data. Well defined should be easy to
interface with. Usually MT-Connect has an agent which is actually a digital twin of the machine
and exposes the data knowing how to communicate with the devices, possibly with proprietary protocol.

- https://www.mtconnect.org
- https://github.com/mtconnect
- https://www.mtconnect.org/opc-ua-companion-specification/

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
