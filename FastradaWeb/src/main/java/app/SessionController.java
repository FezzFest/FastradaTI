package app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/sessions")
public class SessionController {


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Parameter> list() {
        //TODO get from database

        List<Parameter> parameters = new ArrayList<Parameter>();
        for (int teller = 0; teller < 10; teller++) {
            Parameter x = new Parameter();
            x.value = teller*25;
            parameters.add(x);
        }

        return parameters;
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