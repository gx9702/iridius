package it.itere.opsi.devices;

import it.itere.opsi.OpsiDevice;
import it.itere.opsi.OpsiDeviceTag;
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
public class CSVDevice extends OpsiDevice implements Runnable {

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
        
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(this + " - Running");
            try {
                List<String> lines = FileUtils.readLines(new File((String)config.get("file")));
                String[] values = org.apache.commons.lang3.StringUtils.splitPreserveAllTokens(lines.get(lines.size()-1));
                
                int nodeIdx = 0;
                int valueIdx = -1;
                for(String field: fields) {
                    valueIdx++;
                    if (field.startsWith("-")) continue;
                    OpsiDeviceTag tag = tags.get(nodeIdx);
                    
                    UaVariableNode node = tag.getNode();
                    if (node != null) {
                        node.setValue(new DataValue(new Variant(values[valueIdx])));
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
    public List<OpsiDeviceTag> getTags() {
        if (tags != null) return tags;
        
        tags = new ArrayList<>();
        for (String field : fields) {
            OpsiDeviceTag tag = new OpsiDeviceTag();
            tag.setName(field);
            tag.setDevice(this);
            tags.add(tag);
        }
        return tags;
    }

}
