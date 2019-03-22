/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iridius;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;

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
public abstract class Device {

    protected String name;
    protected Map config;
    protected List<DeviceTag> tags = new ArrayList();
    protected boolean initialized = false;
    private IridiusNamespace namespace = null;
    
    // Should be an ObjectNode interface?
    private UaFolderNode node = null;
    
    /**
     * Called on initialization and called again on reinitialization.
     */
    public void startup() {
        if (initialized) {
            shutdown();
        }
    }
    
    public void shutdown() {
        setNamespace(null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Map getConfig() {
        return config;
    }

    public void setConfig(Map config) {
        this.config = config;
    }    
    
    // From name space
    public void write(DeviceTag tag, DataValue value){
    }
    
    // From namespace
    public DataValue read(DeviceTag tag){
        return null;
    }

    public IridiusNamespace getNamespace() {
        return namespace;
    }

    public void setNamespace(IridiusNamespace namespace) {
        this.namespace = namespace;
    }

    public UaFolderNode getNode() {
        return node;
    }

    public void setNode(UaFolderNode node) {
        this.node = node;
    }
    
    public void addTag(DeviceTag tag) {
        tag.setDevice(this);
        namespace.addTag(tag);
        tags.add(tag);
    }
    
    // ???
    public void setValue(DeviceTag tag, DataValue value) {
        namespace.setValue(tag, value);
    }
}
