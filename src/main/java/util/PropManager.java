package util;

import de.dhbwka.swe.utils.util.PropertyManager;

import java.util.HashMap;

public class PropManager extends PropertyManager {

    public PropManager(String fsPropertiesFileName, Class<?> resourceClass, String resourcePropertiesFileName) throws Exception {
        super(fsPropertiesFileName, resourceClass, resourcePropertiesFileName);
    }

    public PropManager(HashMap<String, String> properties) {
        super(properties);
    }


}
