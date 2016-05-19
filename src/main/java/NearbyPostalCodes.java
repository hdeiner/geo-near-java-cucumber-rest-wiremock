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

public class NearbyPostalCodes {
	private URL url;
	private URI uri;
	private JSONResource response;
	private JSONArray responseArray;
	private int responseRowCount;
	
	public NearbyPostalCodes(String username, String postalCode, String countryAbbreviation, double radius, boolean useFakeServer) {
	
		Resty restCall = new Resty();

		WireMockServer wireMockServer = null;
		
		try {
			if (!useFakeServer) {
				url = new URL("http://api.geonames.org/findNearbyPostalCodesJSON?postalcode=" + postalCode + "&country=" + countryAbbreviation + "&radius=" + new Double(radius).toString() + "10&username=" + username);
			} else {				
				FileSource fileSource=new SingleRootFileSource("./wiremock");
				FileSource filesFileSource=fileSource.child("__files");
				FileSource mappingsFileSource=fileSource.child("mappings");
				
				CommandLineOptions options=new CommandLineOptions();

				wireMockServer=new WireMockServer(Options.DEFAULT_PORT, fileSource, options.browserProxyingEnabled());				
				wireMockServer.loadMappingsUsing(new JsonFileMappingsLoader(mappingsFileSource));
				
				wireMockServer.start();

				url = new URL("http://localhost:8080/findNearbyPostalCodesJSON?postalcode=" + postalCode + "&country=" + countryAbbreviation + "&radius=" + new Double(radius).toString() + "10&username=" + username);				
			}

			uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());	
			response = restCall.json(uri); 
			responseArray = new JSONArray(response.get("postalCodes").toString());
			responseRowCount = responseArray.length();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (useFakeServer) {
			wireMockServer.stop();
		}
	}
	
	public int getResponseRowCount() {
		return responseRowCount;		
	}
	
	public String getPlaceName(int index) throws JSONException {
		return new JSONObject(responseArray.get(index).toString()).getString("placeName");
	}
	
	public String getPostalCode(int index) throws JSONException {
		return new JSONObject(responseArray.get(index).toString()).getString("postalCode");
	}
	
	public double getLatitude(int index) throws JSONException {
		return new Double(new JSONObject(responseArray.get(index).toString()).getString("lat"));
	}
	
	public double getLongitude(int index) throws JSONException {
		return new Double(new JSONObject(responseArray.get(index).toString()).getString("lng"));
	}
	
	public double getDistance(int index) throws JSONException {
		return new Double(new JSONObject(responseArray.get(index).toString()).getString("distance"));
	}
}