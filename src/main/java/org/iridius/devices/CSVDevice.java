package org.iridius.devices;

import org.iridius.Device;
import org.iridius.DeviceTag;
import java.io.File;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

/**
 *
 * @author Stefano Lissa
 */
public class CSVDevice extends Device implements Runnable {

    List<String> fields;
    long interval = 5000;
    long lastFilePosition = 0;

    @Override
    public void startup() {
        
        fields = (List<String>) config.get("fields");
        try {
            interval = Long.parseLong((String)config.get("interval"));
        } catch (Exception e) {
            
        }
        
        for (String field : fields) {
            DeviceTag tag = new DeviceTag();
            tag.setName(field);
            addTag(tag);
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
                    setValue(tag, new DataValue(new Variant(values[valueIdx])));
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

}
