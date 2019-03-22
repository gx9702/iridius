package org.iridius.devices;

import org.iridius.Device;
import org.iridius.DeviceTag;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Stefano Lissa
 */
public class DummyDevice extends Device {

    @Override
    public List<DeviceTag> getTags() {
        return Collections.emptyList();
    }


    
}
