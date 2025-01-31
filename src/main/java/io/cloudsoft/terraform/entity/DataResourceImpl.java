package io.cloudsoft.terraform.entity;

import org.apache.brooklyn.core.entity.Attributes;
import org.apache.brooklyn.core.entity.lifecycle.Lifecycle;
import org.apache.brooklyn.core.sensor.Sensors;
import org.apache.brooklyn.entity.stock.BasicEntityImpl;

import java.util.Map;

public class DataResourceImpl extends BasicEntityImpl  implements DataResource {

    public DataResourceImpl() {
        sensors().set(Attributes.SERVICE_STATE_ACTUAL, Lifecycle.CREATED);
    }

    @Override
    public boolean refreshSensors(Map<String, Object> resource) {
        resource.forEach((k, v) -> sensors().set(Sensors.newStringSensor("tf." + k), v.toString()));
        return true;
    }
}
