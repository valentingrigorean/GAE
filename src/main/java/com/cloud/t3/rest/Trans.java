package com.cloud.t3.rest;

import static com.cloud.t3.rest.App.CREDENTIAL_DEFAULT;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.Translate.Languages;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author Valentin
 */
@Path("translate")
public class Trans extends Resource {
    
    @GET
    @Produces("application/json")
    public Response getAll() throws IOException {
        Translate translate = new Translate.Builder(new NetHttpTransport(), new JacksonFactory(), CREDENTIAL_DEFAULT).setApplicationName(App.APPLICATION_NAME).build();
        Languages.List l = translate.languages().list().setKey(App.API_KEY_DEFAULT);
        return OK(l.execute().getLanguages().toArray());
    }

    @GET
    @Produces("application/json")
    @Path("{from}/{to}/{text}")
    public Response translate(@PathParam("from") String from, @PathParam("to") String to, @PathParam("text") String text) throws IOException {
        if (text.isEmpty()) {
            return BAD_REQUEST;
        }
        Translate translate = new Translate.Builder(new NetHttpTransport(), new JacksonFactory(), CREDENTIAL_DEFAULT).setApplicationName(App.APPLICATION_NAME).build();

        List<String> listText = new ArrayList<>();
        listText.add(text);

        Translate.Translations.List tList = translate.translations().list(listText, to).setKey(App.API_KEY_DEFAULT);
        if (!"detect".equals(from.toLowerCase())) {
            tList = tList.setSource(from);
        }
        tList = tList.setTarget(to);
        tList = tList.setQ(listText);
        tList = tList.setKey(App.API_KEY_DEFAULT);

        return OK(tList.execute().getTranslations().toArray());
    }
}
