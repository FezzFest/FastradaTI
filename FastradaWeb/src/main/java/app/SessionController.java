package app;


import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import persistence.FastradaDAO;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/sessions")
public class SessionController {
    //TODO autowiring in orde maken
    //@Autowired
    FastradaDAO fastradaDAO  = new FastradaDAO();

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Parameter> list() {
        //TODO get from database

        List<Parameter> parameters = new ArrayList<Parameter>();
        for (int teller = 0; teller < 10; teller++) {
            Parameter x = new Parameter();
            x.value = teller * 25;
            parameters.add(x);
        }
        return parameters;
    }

    @RequestMapping(value = "/{id}/{parameter}", method = RequestMethod.GET)
    public List<Parameter> getParameterBySessionId(int sessionId, String parameter) {
        List<Parameter> parameters = new ArrayList<Parameter>();
        for (int teller = 0; teller < 10; teller++) {
            Parameter x = new Parameter();
            x.value = teller * 25;
            parameters.add(x);
        }
        return parameters;
    }

    @RequestMapping(value = "new", method = RequestMethod.POST)
    @ResponseBody
    public SessionId getNewSessionId(@RequestBody String sessionDataString) {
        Gson gson = new Gson();
        SessionData sessionData = gson.fromJson(sessionDataString, SessionData.class);
        System.out.println(sessionData.getName());
        int sessionId = fastradaDAO.createNextSession(sessionData);
        return new SessionId(sessionId);
    }


/*    @RequestMapping(method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public HttpEntity<?> create(@RequestBody Book book, @Value("#{request.requestURL}") StringBuffer parentUri) {
        book = this.bookRepository.save(book);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(childLocation(parentUri, book.getId()));
        return new HttpEntity<Object>(headers);
    }*/


//    @RequestMapping(value = "/{id}/{parameter}", method = RequestMethod.GET)
//    public @ResponseBody Book find(@PathVariable("id") Integer id) {
//        Book book = this.bookRepository.findById(id);
//        if (book == null) {
//            throw new BookNotFoundException(id);
//        }
//        return book;
//    }
}