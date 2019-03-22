package org.iridius.devices;

import org.iridius.Device;
import org.iridius.DeviceTag;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

/**
 *
 * @author Stefano
 */
public class CSVDevice extends Device implements Runnable {

    List<UaNode> nodes;
    List<String> fields;
    long interval = 5000;
    long lastFilePosition = 0;

    @Override
    public void setConfig(Map config) {
        super.setConfig(config);
        fields = (List<String>) config.get("fields");
        try {
            interval = Long.parseLong((String)config.get("interval"));
        } catch (Exception e) {
            
        }
        
        // TODO: Better thread management?
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(this + " - Running");
            try {
                // TODO: Store the file size at reading moment and seek on next run
                // TODO: Check if the file has been changed meanwhile
                List<String> lines = FileUtils.readLines(new File((String)config.get("file")));
                String[] values = org.apache.commons.lang3.StringUtils.splitPreserveAllTokens(lines.get(lines.size()-1));
                
                int nodeIdx = 0;
                int valueIdx = -1;
                for(String field: fields) {
                    valueIdx++;
                    if (field.startsWith("-")) continue;
                    DeviceTag tag = tags.get(nodeIdx);
                    
                    // Get the associate node and update the value
                    UaVariableNode node = tag.getNode();
                    if (node != null) {
                        // TODO: Set the status and the device time
                        DataValue dv = new DataValue(new Variant(values[valueIdx]));
                        node.setValue(dv);
                    }
                    nodeIdx++;
                }
                Thread.sleep(interval);
            } catch (InterruptedException ex) {
                //Logger.getLogger(Machine.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<DeviceTag> getTags() {
        if (tags != null) return tags;
        
        tags = new ArrayList<>();
        for (String field : fields) {
            DeviceTag tag = new DeviceTag();
            tag.setName(field);
            tag.setDevice(this);
            tags.add(tag);
        }
        return tags;
    }

}
