package com.account.sdk;

import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.util.print.LogFactory;

public class SdkManager
{
    private static String CLIENT_ID = "";
    private static HttpTransport transport = new NetHttpTransport();
    private static JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

    public static class GooglePlayerInfo
    {
        public String userID;
        public String name;
        public String picture;
        public String locale;
        public String family_name;
        public String given_name;
        public String email;
    }

    /**
     * 验证登录token是否合法
     * 
     * API:https://developers.google.com/identity/sign-in/android/start-integrating
     * 
     * @param idTokenString
     * @return
     */
    public static GooglePlayerInfo checkGooglePlay(String idTokenString)
    {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory).setAudience(Collections.singletonList(CLIENT_ID)).build();

        try
        {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null)
            {
                Payload payload = idToken.getPayload();

                GooglePlayerInfo info = new GooglePlayerInfo();

                String userId = payload.getSubject();
                info.userID = userId;
                info.email = payload.getEmail();
                info.name = (String) payload.get("name");
                info.picture = (String) payload.get("picture");
                info.locale = (String) payload.get("locale");
                info.family_name = (String) payload.get("family_name");
                info.given_name = (String) payload.get("given_name");

                return info;
            }
            else
            {
                LogFactory.error("无效的IDToken:{}", idTokenString);
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }

        return null;
    }
}
