package be.kdg.FastradaMobile;

import be.kdg.FastradaMobile.controllers.OutputDataController;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by FezzFest on 21/02/14.
 */
public class TestOutputDataController {
    @Test
    public void testGetRequest() {
        OutputDataController outputDataController = new OutputDataController();
        String url = "http://vps42465.ovh.net/fastrada/get.php?text=Fastrada";
        String result = outputDataController.doGet(url);

        assertEquals("Server must return success", "Success", result);
    }

   /* @Test
    public void testPostRequest() {
        OutputDataController outputDataController = new OutputDataController();
        String url = "http://vps42465.ovh.net/fastrada/post.php";
        String parameters = "text=Fastrada";
        String result = outputDataController.doPost(url, parameters);

        assertEquals("Server must return success", "Success", result);
    }*/
}
