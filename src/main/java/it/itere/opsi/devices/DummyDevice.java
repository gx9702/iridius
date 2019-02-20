package it.itere.opsi.devices;

import it.itere.opsi.OpsiDevice;
import it.itere.opsi.OpsiDeviceTag;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Stefano Lissa
 */
public class DummyDevice extends OpsiDevice {

    @Override
    public List<OpsiDeviceTag> getTags() {
        return Collections.emptyList();
    }


    
}
