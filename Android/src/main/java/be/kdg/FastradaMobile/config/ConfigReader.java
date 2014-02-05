package be.kdg.FastradaMobile.config;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * Created by philip on 5/02/14.
 */
public class ConfigReader {
    private String configPath = "res/xml/config.xml";
    private Document document;

    public ConfigReader(){
        FileInputStream configFileStream = null;

        try {
            configFileStream = new FileInputStream(configPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        try {
            document = builder.parse(configFileStream);
        }
        catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getConfigStringValue(String parameter, String parameterWaarde) {

        Element root = document.getDocumentElement();

        Node sensors = root.getElementsByTagName("sensors").item(0);

        NodeList sensorList = sensors.getChildNodes();

        for(int sensorIndex = 0; sensorIndex< sensorList.getLength();sensorIndex++){
            Node sensor = sensorList.item(sensorIndex);
            NodeList parameterList = sensor.getChildNodes();

            for(int parameterIndex = 0; parameterIndex< parameterList.getLength();parameterIndex++){

                if(parameterList.item(parameterIndex).getNodeType() == Node.ELEMENT_NODE ){ //controle of lijn een xml element is of whitespace

                    if(parameterList.item(parameterIndex).getAttributes().getNamedItem("name").getNodeValue().equals(parameter)){

                        NodeList parameterWaarden = parameterList.item(parameterIndex).getChildNodes();

                        for(int waardenIndex = 0; waardenIndex<parameterWaarden.getLength();waardenIndex++){

                            if(parameterWaarden.item(waardenIndex).getNodeType() == Node.ELEMENT_NODE && parameterWaarden.item(waardenIndex).getNodeName().equals(parameterWaarde)){

                                if(parameterWaarden.item(waardenIndex).hasChildNodes()){
                                    return parameterWaarden.item(waardenIndex).getChildNodes().item(0).getNodeValue();
                                }
                                else {
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

        return Integer.parseInt(getConfigStringValue(parameter, parameterWaarde));
    }

    public double getConfigDoubleValue(String parameter, String parameterWaarde) {
        return Double.parseDouble(getConfigStringValue(parameter, parameterWaarde));
    }

    public Parameter getParameterConfig(String parameterNaam) {
         int startBit = getConfigIntValue(parameterNaam,"startbit");
         int length = getConfigIntValue(parameterNaam,"length");
         String byteOrder = getConfigStringValue(parameterNaam,"byteorder");
         String valueType = getConfigStringValue(parameterNaam,"valuetype");
         double factor = getConfigDoubleValue(parameterNaam,"factor");
         int offset = getConfigIntValue(parameterNaam,"offset");
         double minimum = getConfigDoubleValue(parameterNaam,"minimum");
         double maximum = getConfigDoubleValue(parameterNaam,"maximum");
         String unit = getConfigStringValue(parameterNaam,"unit");

        return new Parameter(startBit,length,byteOrder,valueType,factor,offset,minimum,maximum,unit);
    }
}
