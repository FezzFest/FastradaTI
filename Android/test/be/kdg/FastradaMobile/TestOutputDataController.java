package be.kdg.FastradaMobile;

import be.kdg.FastradaMobile.controllers.OutputDataController;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by FezzFest on 21/02/14.
 */
public class TestOutputDataController {
    @Test
    public void testGetRequest() {
        OutputDataController outputDataController = new OutputDataController();
        String result = outputDataController.doGet("https://graph.facebook.com/peter.vanakelyen.1");

        assertTrue(result.contains("Peter Van Akelyen"));
    }
}
