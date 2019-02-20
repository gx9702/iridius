# Devices Anatomy

A _device_ is something able to communicate with a real machine (using whatever technology
is required) and "adapt" the write, read and method calls available via OPC-UA from and
to the device.

A _device_ is instantiated following the configuration and creates OPC-UA nodes
which expose machine variables (readable and/or writable).

The OPC-UA server keeps a map between nodes and devices which manage them notifying the device
when there is a write or mothod call operation.

Special adapters can be written to add special feature to a device which are not actually
implemented in the device itself.

## Base class

The base class for a device is `OpsiDevice` which simply store the set of settings coming from the
[configuration](configuration.md) file and the list of tags which are the real machine values we can read and/or
write and which are exposed via OPC-UA nodes.

How to expose OPC-UA methods is still not defined.

## MQTT Devices

Those are devices which send and receive data using MQTT.

The configuration should be very simple: the endpoint and the topic to listen to. Probably we can
support JSON payload but to have a node for each JSON property we need to declare it and
probably describe it in terms of human readable name and data type.

For single valued payload, the data type should suffice.

## MTConnect Devices

Those are devices which expose (read-only) data using MTConnect.  

There is an [OPC-UA MTConnect companion specification](https://www.mtconnect.org/opc-ua-companion-specification/)
that we should read.

## CSV Devices

Those are devices which expose data in a CSV format. The CSV file or stream could be retrieved via
shared folder, FTP, HTTP and so on.

