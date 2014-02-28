package dataInterpreter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlo on 28/02/14.
 */
public class Sensor {
    private List<Parameter> parameters;

    public Sensor() {
        parameters = new ArrayList<Parameter>();
    }

    public void addParamater(Parameter parameter) {
        if(parameter.getName().isEmpty()) {
            return;
        }

        boolean exists = false;
        for(Parameter checkParam : parameters) {
            if(checkParam.equals(parameter)) {
               exists = true;
            }
        }

        if(!exists) {
            parameters.add(parameter);
        }

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
