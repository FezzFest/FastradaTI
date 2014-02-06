package be.kdg.FastradaMobile.config;

import java.util.HashMap;

/**
 * Created by philip on 6/02/14.
 */
public class Sensor {
    private HashMap<String,Parameter> parameters;

    public Sensor() {
        parameters = new HashMap<String, Parameter>();
    }

    public void addParamater(String parameterName, Parameter parameter) {
        if(!parameterName.equals("")){
            parameters.put(parameterName,parameter);
        }
    }

    public HashMap<String, Parameter> getParameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object o) {
        Sensor sensor2 = (Sensor) o;
        HashMap<String,Parameter> senser2Parameters = sensor2.getParameters();
        if(parameters.size()!=senser2Parameters.size())
            return false;

        for(String key:parameters.keySet()){
            if(!senser2Parameters.containsKey(key)){
                return false;
            }
            if(!parameters.get(key).equals(senser2Parameters.get(key))){
                return false;
            }
        }
        return true;
    }
}
