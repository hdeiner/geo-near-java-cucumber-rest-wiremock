package main.java;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.assertEquals;

public class StepDefs {
	private NearbyPostalCodes nearbyPostalCodes;
	private String accountName;
	private boolean useFakeServer;
	
	@Given("^I have an account at http://api\\.geonames\\.org of \"([^\"]*)\"$")
	public void i_have_an_account_at_http_api_geonames_org_of(String accountName) throws Throwable {
	    this.accountName = accountName;
	    nearbyPostalCodes = null;
	    useFakeServer = false;
	}
	
	@Given("^I have an account at http://api\\.geonames\\.org of \"([^\"]*)\", but I am testing on a fake$")
	public void i_have_an_account_at_http_api_geonames_org_of_but_I_am_testing_on_a_fake(String accountName) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    this.accountName = accountName;
	    nearbyPostalCodes = null;
	    useFakeServer = true;
	}
	
	@When("^I find nearby postal codes with postalCode \"([^\"]*)\", country \"([^\"]*)\", and radius \"([^\"]*)\"$")
	public void i_find_nearby_postal_codes_with_postalCode_country_and_radius(String postalCode, String country, String radius) throws Throwable {
		nearbyPostalCodes = new NearbyPostalCodes(accountName, postalCode, country, new Double(radius), useFakeServer);
	}
	
	@Then("^row = \"([^\"]*)\", placeName = \"([^\"]*)\", postalCode = \"([^\"]*)\", latitude = \"([^\"]*)\", longitude = \"([^\"]*)\", and distance = \"([^\"]*)\"$")
	public void row_placeName_postalCode_latitude_longitude_and_distance(String index, String placeName, String postalCode, String latitude, String longitude, String distance) throws Throwable {
	    int rowNumber = new Integer(index);
		assertEquals(placeName, nearbyPostalCodes.getPlaceName(rowNumber));
	    assertEquals(postalCode, nearbyPostalCodes.getPostalCode(rowNumber));
	    assertEquals(new Double(latitude), nearbyPostalCodes.getLatitude(rowNumber), 0.05);
	    assertEquals(new Double(longitude), nearbyPostalCodes.getLongitude(rowNumber), 0.05);
	    assertEquals(new Double(distance), nearbyPostalCodes.getDistance(rowNumber), 0.05);
	}

}
