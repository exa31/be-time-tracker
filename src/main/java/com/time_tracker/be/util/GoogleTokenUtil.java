package com.time_tracker.be.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.util.Map;

public class GoogleTokenUtil {
    public static Map<String, Object> verifyGoogleToken(String idToken) throws Exception {
        URL url = new URL("https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken);
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(url, Map.class);
    }
}
