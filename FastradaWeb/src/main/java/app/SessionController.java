package app;

import com.google.gson.Gson;
import dataInterpreter.ByteCalculateController;
import dataInterpreter.CompressorController;
import dataInterpreter.ConfigController;
import json.SessionData;
import json.SessionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import persistence.FastradaDAO;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @ResponseBody
    public List<Parameter> getParameterValuesBySessionId(@PathVariable("id") Integer sessionId, @PathVariable("parameter") String parameter) {
        return fastradaDAO.getParameterValuesBySessionId(sessionId, parameter);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteSession(@PathVariable("id") Integer sessionId) {
        fastradaDAO.deleteSession(sessionId);
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    @ResponseBody
    public String savePacket(@RequestBody byte[] compressedDataBytes) {
        // Create new controllers
        InputStream configFile = getClass().getClassLoader().getResourceAsStream("config.xml");
        ConfigController config = new ConfigController(configFile);
        ByteCalculateController byteCalculator = new ByteCalculateController(config);

        // Decompress data
        byte[] decompressed = CompressorController.decompress(compressedDataBytes);
        int numberOfPackets = decompressed.length / 18;
        for (int i = 0; i < numberOfPackets; i++) {
            // Get session ID
            int sessionId = 0;

            // Split array
            byte[] bTimestamp = new byte[8];
            byte[] bPacket = new byte[10];
            System.arraycopy(decompressed, (18*i), bTimestamp, 0, 8);
            System.arraycopy(decompressed, (18*i)+8, bPacket, 0, 10);

            // Get timestamp
            ByteBuffer timestampBuffer = ByteBuffer.wrap(bTimestamp);
            long timestamp = timestampBuffer.getLong();

            // Parse packet data
            Map<String, Double> map = byteCalculator.calculatePacket(bPacket);
            HashMap hashMap = new HashMap(map);

            // Insert into database
            fastradaDAO.insertPacketValues(sessionId, timestamp, hashMap);
        }

        return "Processed " + numberOfPackets + " packets.";
    }

    @RequestMapping(value = "/gps/{id}", method = RequestMethod.GET)
      @ResponseBody
      public List<GpsValue> getGpsFromSession(@PathVariable("id") Integer sessionId) {
         return fastradaDAO.getGpsById(sessionId);
      }

/*    @RequestMapping(method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public HttpEntity<?> create(@RequestBody Book book, @Value("#{request.requestURL}") StringBuffer parentUri) {
        book = this.bookRepository.save(book);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(childLocation(parentUri, book.getId()));
        return new HttpEntity<Object>(headers);
    }*/


/*    @RequestMapping(value = "/{id}/{parameter}", method = RequestMethod.GET)
    public @ResponseBody Book find(@PathVariable("id") Integer id) {
        Book book = this.bookRepository.findById(id);
        if (book == null) {
            throw new BookNotFoundException(id);
        }
        return book;
    }*/
}