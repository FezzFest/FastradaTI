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
    FastradaDAO fastradaDAO = new FastradaDAO();

    //TODO concurrent request not allowed
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @ResponseBody
    public SessionId getNewSessionId(@RequestBody String sessionDataString) {
        Gson gson = new Gson();
        SessionData sessionData = gson.fromJson(sessionDataString, SessionData.class);
        int sessionId = fastradaDAO.createNextSession(sessionData);
        return new SessionId(sessionId);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<SessionData> getAllSessionData() {
        return fastradaDAO.getAllSessionsData();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getParametersBySessionId(@PathVariable("id") Integer id) {
        return fastradaDAO.getParametersBySessionId(id);
    }

    @RequestMapping(value = "/{id}/{parameter}", method = RequestMethod.GET)
    public List<Parameter> getParameterValuesBySessionId(@PathVariable("id") Integer sessionId, @PathVariable("parameter") String parameter) {
        return fastradaDAO.getParameterValuesBySessionId(sessionId, parameter);
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