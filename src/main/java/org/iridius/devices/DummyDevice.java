package org.iridius.devices;

import org.iridius.Device;
import org.iridius.DeviceTag;
import java.util.Collections;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

/**
 *
 * @author Stefano Lissa
 */
public class DummyDevice extends Device {

    @Override
    public void startup() {
        super.startup();
        DeviceTag tag = new DeviceTag();
        tag.setDataType(Identifiers.String);
        tag.setName("dummy1");
        tag.setDisplayName("Dummy 1");
        addTag(tag);
        
        DataValue dv = new DataValue(new Variant(config.get("dummy1").toString()));
        
        setValue(tag, dv);
    }

    


    
}
