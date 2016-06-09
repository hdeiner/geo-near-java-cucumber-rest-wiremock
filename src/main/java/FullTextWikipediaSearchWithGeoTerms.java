package main.java;

import java.net.URI;
import java.net.URL;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.*;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.standalone.CommandLineOptions;
import com.github.tomakehurst.wiremock.standalone.JsonFileMappingsLoader;

import us.monoid.web.JSONResource;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.Resty;

public class FullTextWikipediaSearchWithGeoTerms {
	private URL url;
	private URI uri;
	private JSONResource response;
	private JSONArray responseArray;

	public FullTextWikipediaSearchWithGeoTerms(String queryTerm, String username, boolean useFakeServer) {
	
		Resty restCall = new Resty();

		WireMockServer wireMockServer = null;
		
		try {
			if (!useFakeServer) {
				url = new URL("http://api.geonames.org/wikipediaSearchJSON?q=" + queryTerm + "&maxRows=20&username=" + username);
			} else {				
				FileSource fileSource=new SingleRootFileSource("./wiremock");
				FileSource filesFileSource=fileSource.child("__files");
				FileSource mappingsFileSource=fileSource.child("mappings");
				
				CommandLineOptions options=new CommandLineOptions();

				wireMockServer=new WireMockServer(Options.DEFAULT_PORT, fileSource, options.browserProxyingEnabled());				
				wireMockServer.loadMappingsUsing(new JsonFileMappingsLoader(mappingsFileSource));
				
				wireMockServer.start();

				url = new URL("http://localhost:8080/wikipediaSearchJSON?q=" + queryTerm + "&maxRows=20&username=" + username);
			}

			uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());	
			response = restCall.json(uri);
			responseArray = new JSONArray(response.get("geonames").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (useFakeServer) {
			wireMockServer.stop();
		}
	}

    public int getArticleCount() throws Exception {
        return responseArray.length();
    }

    public int getResponseRowFor(String title)  {
        int responseRow = -1;
        for (int row=0; row<responseArray.length(); row++) {
            try {
                if (responseArray.getJSONObject(row).get("title").toString().equals(title)) {responseRow = row+1; }
            } catch (JSONException e) {
            }
        }
        return responseRow;
    }

    public String getTitleFor(int row)  {
        try {
            return responseArray.getJSONObject(row-1).get("title").toString();
        } catch (JSONException e) {
            return "";
        }
    }

    public String getFeatureFor(int row) {
        try {
            return responseArray.getJSONObject(row-1).get("feature").toString();
        } catch (JSONException e) {
            return "";
        }
    }

    public String getSummaryFor(int row) {
        try {
            return responseArray.getJSONObject(row-1).get("summary").toString();
        } catch (JSONException e) {
            return "";
        }
    }

    public String getRawResponseString(){
        return responseArray.toString();
    }

}