package be.kdg.FastradaMobile;

import android.content.Intent;
import android.test.ServiceTestCase;
import be.kdg.FastradaMobile.services.CommunicationService;

/**
 * Created by FezzFest on 19/02/14.
 */
public class TestCommunicationService extends ServiceTestCase<CommunicationService> {

    public TestCommunicationService() {
        super(CommunicationService.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void startCommunicationService() throws InterruptedException {
        // Start service
        Intent intent = new Intent(getSystemContext(), CommunicationService.class);
        startService(intent);
        Thread.sleep(5000);
    }
}
