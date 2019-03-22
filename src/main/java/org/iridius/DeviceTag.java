/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iridius;

import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * This represents a generic device we're connecting to the Opsi server using an
 * adapter (which actually is able to communicate with the device).
 * 
 * The adapter is instantiated on server startup and initialized with the specific adapter
 * options for this device (for example the device IP).
 * 
 * The adapter is responsible to create the OPC-UA nodes corresponding to the device exposed data and/or
 * methods.
 * 
 * @author Stefano Lissa
 */
public class DeviceTag {
    private UaVariableNode node;
    private String name;
    private String displayName;
    private NodeId dataType = Identifiers.String;
    private Device device;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UaVariableNode getNode() {
        return node;
    }

    public void setNode(UaVariableNode node) {
        this.node = node;
    }

    public NodeId getDataType() {
        return dataType;
    }

    public void setDataType(NodeId dataType) {
        this.dataType = dataType;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getDisplayName() {
        if (displayName == null) return name;
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
}
