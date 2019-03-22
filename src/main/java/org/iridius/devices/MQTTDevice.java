/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iridius.devices;

import org.iridius.Device;
import org.iridius.DeviceTag;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * The idea behind this adapter is to subscribe an MQTT topic, extract values from the payload (json?)
 * and publish them as device variable in OPC-UA.
 * 
 * @author Stefano Lissa
 */
public class MQTTDevice extends Device implements MqttCallback {

    MqttClient myClient;
    MqttConnectOptions connOpt;
/*
    @Override
    public List<DeviceTag> getTags() {
        
        if (tags != null) return tags;
        tags = new ArrayList();
        DeviceTag tag = new DeviceTag();
        
        tag.setName("value");
        tag.setDevice(this);
        
        tags.add(tag);
        
        return tags;
/*
        List<UaNode> nodes = new ArrayList();

        String name = (String) config.get("name");

        node = new UaVariableNode.UaVariableNodeBuilder(server.getNodeMap())
                //.setNodeId(new NodeId(namespaceIndex, "HelloWorld/ScalarTypes/" + name))
                .setNodeId(NodeId.NULL_GUID)
                .setAccessLevel(ubyte(AccessLevel.getMask(AccessLevel.READ_WRITE)))
                .setUserAccessLevel(ubyte(AccessLevel.getMask(AccessLevel.READ_WRITE)))
                //.setBrowseName(new QualifiedName(namespaceIndex, name))
                .setBrowseName(QualifiedName.NULL_VALUE)
                .setDisplayName(LocalizedText.english((String) config.get("topic")))
                .setDataType(Identifiers.String)
                .setTypeDefinition(Identifiers.BaseDataVariableType)
                .build();

        node.setValue(new DataValue(new Variant("-")));
        
        // TODO: This is not the right way to intercept writes and move the data to the device!
        node.setAttributeDelegate(new DelegatingAttributeDelegate() {

            @Override
            public void setValue(AttributeContext context, VariableNode node, DataValue value) throws UaException {
                super.setValue(context, node, value); //To change body of generated methods, choose Tools | Templates.
                System.out.println(node.getNodeId());
                
                // Externally (from a client) write
                if (context.getSession().isPresent()) {
                    
                }
            }
            
        });

        nodes.add(node);
        
        //tags.put("main", node);

        return nodes;

    }*/

    @Override
    public void setConfig(Map config) {
        super.setConfig(config);
        
        System.out.println(config);
        // setup MQTT Client
        String clientID = "akdjhfkjzsfsfhdj";
        connOpt = new MqttConnectOptions();

        connOpt.setCleanSession(true);
        connOpt.setKeepAliveInterval(30);
        //connOpt.setUserName(M2MIO_USERNAME);
        //connOpt.setPassword(M2MIO_PASSWORD_MD5.toCharArray());

        // Connect to Broker
        try {
            myClient = new MqttClient((String)config.get("url"), clientID);
            myClient.setCallback(this);
            myClient.connect(connOpt);
        } catch (MqttException e) {
            e.printStackTrace();
            //System.exit(-1);
        }

        System.out.println("Connected to " + (String)config.get("url"));

        // setup topic
        // topics on m2m.io are in the form <domain>/<stuff>/<thing>
        String myTopic = (String)config.get("topic");


        // subscribe to topic if subscriber
      
            try {
                int subQoS = 0;
                myClient.subscribe(myTopic, subQoS);
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    @Override
    public void connectionLost(Throwable thrwbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void messageArrived(String string, MqttMessage mm) throws Exception {
        System.out.println(string);
        System.out.println(new String(mm.getPayload()));
        
        tags.get(0).getNode().setValue(new DataValue(new Variant(new String(mm.getPayload()))));
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
