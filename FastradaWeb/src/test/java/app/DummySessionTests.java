package app;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DummySessionTests {

    private HashMap<Integer, Integer> SessionTemperatures;
    private SessionController sessioncontroller;

    @Before
    public void before() {
        sessioncontroller = new SessionController();
    }

    //
//  @Test
//  public void saveAndGet() {
//    Book book = new Book();
//    book.setAuthor(this.testAuthor);
//    book.setTitle("Pro Spring");
//
//    book = this.repository.save(book);
//    assertNotNull(book.getId());
//
//    Book book2 = this.repository.findById(book.getId());
//    assertEquals(book, book2);
//  }
    @Test
    public void getSessionSpeed() {
        SessionTemperatures = sessioncontroller.getParameterBySessionId(1, "temperature");

        HashMap<Integer, Integer> controlMap = new HashMap<Integer, Integer>();
        controlMap = new HashMap<Integer, Integer>();
        for (int teller = 0; teller < 30; teller++) {
            controlMap.put(teller + 200, teller * 4);
        }

        Assert.assertEquals(controlMap, SessionTemperatures);
    }
//
//    @Test
//    public void getSessionList() {
//        List<Integer> sessionList =  sessioncontroller.list();
//
//        List<Integer> controlList = new ArrayList<Integer>();
//        for (int teller = 0; teller < 10; teller++) {
//            controlList.add(teller);
//        }
//
//        assertEquals(sessionList, controlList);
//
//    }
}
