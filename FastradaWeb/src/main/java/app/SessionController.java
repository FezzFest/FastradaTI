package app;

import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/sessions")
public class SessionController {


    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody String list() {
        //TODO get from database

        return "{1,2,3,4,5,6,7,8,9}";
    }


    @RequestMapping(value = "/{id}/{parameter}", method = RequestMethod.GET)
    public HashMap<Integer, Integer> getParameterBySessionId(int sessionId, String parameter) {
        HashMap<Integer, Integer> sessionValues = new HashMap<Integer, Integer>();

        //TODO get values from database
        sessionValues = new HashMap<Integer, Integer>();
        for (int teller = 0; teller < 30; teller++) {
            sessionValues.put(teller + 200, teller * 4);
        }

        return sessionValues;
    }

//    @RequestMapping(value = "/{id}/{parameter}", method = RequestMethod.GET)
//    public @ResponseBody Book find(@PathVariable("id") Integer id) {
//        Book book = this.bookRepository.findById(id);
//        if (book == null) {
//            throw new BookNotFoundException(id);
//        }
//        return book;
//    }
}
