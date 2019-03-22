/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iridius;

import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

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
    protected List<DeviceTag> tags;
    protected boolean initialized = false;
    
    /**
     * Called on initialization and called again on reinitialization.
     */
    public void startup() {
        if (initialized) {
            shutdown();
        }
        
    }
    
    
    public void shutdown() {
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
    
    public abstract List<DeviceTag> getTags();
    
    public void write(DeviceTag tag, Variant value){
    }
    
    public Object read(DeviceTag tag){
        return "";
    }
}
