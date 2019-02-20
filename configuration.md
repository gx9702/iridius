# Configuration

The configuration file should define all the devices to activate and contain
the configuration each device implementation need to start up.

The configuration could contain the definition of one or more OPC-UA namespace and,
of course, a set of server specific settings.

Below a proposal for a JSON format.

Of course it would be useful to have the configuration distributed on separated files
to make it more readable.

Each device definition has a `type` which is simply the class to be instantiated and which
later initialize itself with the `config` parameters block.

```json
{
    "server": {
        "setting1": "",
        "setting2": ""
    },
    "namespaces": [
        
        {
            "name": "Namespace 1",
            "uri": "urn:opsi:namespace1",
            
            "devices": [
                {
                    "name": "Device 1",
                    "type": "it.itere.opsi.devices.DummyDevice",
                    "config" : {
                        "ip": "120.0.0.1",
                        "key1": "value 1"
                    }
                },
                
                {
                    "name": "Device 2",
                    "type": "it.itere.opsi.devices.MQTTDevice",
                    "config" : {
                        "url": "tcp://test.mosquitto.org:1883",
                        "topic": "/Boiler/#",
                        "payload": "json"
                    }
                }
            ]
        }
    ]
}
```