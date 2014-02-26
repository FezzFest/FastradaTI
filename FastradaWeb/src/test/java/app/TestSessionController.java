package app;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Jonathan on 26/02/14.
 */
public class TestSessionController {
    private SessionController sessioncontroller;

    @Before
    public void before() {
        sessioncontroller = new SessionController();
    }

    @Test
    public void testNewSessionPost() {
        SessionData run1 = new SessionData("Run3", new Date(System.currentTimeMillis()), "Zalig ritje met mooi weer", "Spa Francorchamps");
        SessionData run2 = new SessionData("Run4", new Date(System.currentTimeMillis()), "Zeer veel commentaar is hier geschreven", "Spa Francorchamps");
        Gson gson = new Gson();

        String json1 = gson.toJson(run1);
        String json2 = gson.toJson(run2);

        //TODO make localhost postrequest
        int sessionId1 = sessioncontroller.getNewSessionId(json1).getSessionId();
        int sessionId2 = sessioncontroller.getNewSessionId(json2).getSessionId();

        Assert.assertEquals("SessionIds must be consecutive", sessionId1 + 1, sessionId2);
    }
}
