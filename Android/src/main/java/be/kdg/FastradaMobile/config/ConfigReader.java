package be.kdg.FastradaMobile.config;


import be.kdg.FastradaMobile.R;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by philip on 5/02/14.
 */
public class ConfigReader {
    private Document document;
    private InputStream configFileStream;

    public ConfigReader(Context context) {
        configFileStream = null;
        configFileStream = context.getResources().openRawResource(R.raw.config);
        readConfigInputStream(configFileStream);
    }

    public ConfigReader(String configPath){
        try {
            configFileStream = new FileInputStream(configPath);
            readConfigInputStream(configFileStream);
        } catch (FileNotFoundException e) {

            //e.printStackTrace();
        }
    }

    private void readConfigInputStream(InputStream configFileStream) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        try {
            document = builder.parse(configFileStream);
        } catch (SAXException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public String getConfigStringValue(String parameter, String parameterWaarde) {
        if (document == null) {
            //TODO raise error
            return "";
        }

        Element root = document.getDocumentElement();
        Node sensors = root.getElementsByTagName("sensors").item(0);
        NodeList sensorList = sensors.getChildNodes();

        for (int sensorIndex = 0; sensorIndex < sensorList.getLength(); sensorIndex++) {
            Node sensor = sensorList.item(sensorIndex);
            NodeList parameterList = sensor.getChildNodes();
            for (int parameterIndex = 0; parameterIndex < parameterList.getLength(); parameterIndex++) {
                //controle of lijn een xml element is of whitespace
                if (isElementNode(parameterList.item(parameterIndex))) {
                    if (parameterList.item(parameterIndex).getAttributes().getNamedItem("name").getNodeValue().equals(parameter)) {
                        NodeList parameterWaarden = parameterList.item(parameterIndex).getChildNodes();
                        for (int waardenIndex = 0; waardenIndex < parameterWaarden.getLength(); waardenIndex++) {
                            if (isElementNode(parameterWaarden.item(waardenIndex)) && parameterWaarden.item(waardenIndex).getNodeName().equals(parameterWaarde)) {
                                if (parameterWaarden.item(waardenIndex).hasChildNodes()) {
                                    return parameterWaarden.item(waardenIndex).getChildNodes().item(0).getNodeValue();
                                } else {
                                    return "";
                                }
                            }
                        }
                    }
                }
            }
        }
        return "";
    }

    public int getConfigIntValue(String parameter, String parameterWaarde) {
        String configStringValue = getConfigStringValue(parameter, parameterWaarde);
        if (configStringValue.equals(""))
            return 0;

        return Integer.parseInt(configStringValue);
    }

    public double getConfigDoubleValue(String parameter, String parameterWaarde) {
        String configStringValue = getConfigStringValue(parameter, parameterWaarde);
        if (configStringValue.equals(""))
            return 0;

        return Double.parseDouble(configStringValue);
    }

    public Parameter getParameterConfig(String parameterNaam) {
        int startBit = getConfigIntValue(parameterNaam, "startbit");
        int length = getConfigIntValue(parameterNaam, "length");
        String byteOrder = getConfigStringValue(parameterNaam, "byteorder");
        double factor = getConfigDoubleValue(parameterNaam, "factor");
        int offset = getConfigIntValue(parameterNaam, "offset");
        double minimum = getConfigDoubleValue(parameterNaam, "minimum");
        double maximum = getConfigDoubleValue(parameterNaam, "maximum");
        String unit = getConfigStringValue(parameterNaam, "unit");

        return new Parameter(parameterNaam, startBit, length, byteOrder, factor, offset, minimum, maximum, unit);
    }


    public List<String> getParameterNames(int sensorId) {
        List<String> parameterNames = new ArrayList<String>();

        Element root = document.getDocumentElement();

        Node sensors = root.getElementsByTagName("sensors").item(0);

        NodeList sensorList = sensors.getChildNodes();

        for (int i = 0; i < sensorList.getLength(); i++) {
            if (isElementNode(sensorList.item(i)) && sensorList.item(i).getAttributes().getNamedItem("id").getNodeValue().equals(sensorId + "")) {
                NodeList parameters = sensorList.item(i).getChildNodes();
                for (int j = 0; j < parameters.getLength(); j++) {
                    if (isElementNode(parameters.item(j))) {
                        parameterNames.add(parameters.item(j).getAttributes().getNamedItem("name").getNodeValue());
                    }
                }
            }
        }
        return parameterNames;
    }

    private boolean isElementNode(Node node) {
        return node.getNodeType() == Node.ELEMENT_NODE;
    }

    public int[] getSensorIds() {
        Element root = document.getDocumentElement();
        Node sensors = root.getElementsByTagName("sensors").item(0);
        NodeList sensorList = sensors.getChildNodes();
        int[] ints = new int[sensorList.getLength()];

        for (int i = 0; i < sensorList.getLength(); i++) {
            if (isElementNode(sensorList.item(i))) {
                ints[i] = Integer.parseInt(sensorList.item(i).getAttributes().getNamedItem("id").getNodeValue());
            }
        }
        return ints;
    }
}
