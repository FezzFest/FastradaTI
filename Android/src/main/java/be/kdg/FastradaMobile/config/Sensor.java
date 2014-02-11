package be.kdg.FastradaMobile.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by philip on 6/02/14.
 */
public class Sensor {
    private List<Parameter> parameters;

    public Sensor() {
        parameters = new ArrayList<Parameter>();
    }

    public void addParamater(Parameter parameter) {
        parameters.add(parameter);
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object o) {
        Sensor sensor2 = (Sensor) o;
        List<Parameter> senser2Parameters = sensor2.getParameters();
        if (parameters.size() != senser2Parameters.size())
            return false;
        for (int i = 0; i < parameters.size(); i++) {
            if (!parameters.contains(senser2Parameters.get(i))) {
                return false;
            }
            if (!senser2Parameters.contains(parameters.get(i))) {
                return false;
            }
        }

        return true;
    }
}
