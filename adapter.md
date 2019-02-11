# Adapters Anatomy

An adapter is something able to communicate with a device (using whatever technology
is required) and "adapt" the write, read and method calls available via OPC-UA from and
to the device.

An adapter is instantiated for each device and, following the configuration, will create OPC-UA nodes
which expose machine variables (readable and or writable).

Probably, for each created node, the adapter will attach an attribute delegate
(documentation needed) to listed for read and write operation and do whatever is needed
on the device.

(I'm missing, for example, when a read should be actually accomplished on the machine or can
return a cached value - my fault I don't know enough OPC-UA).
