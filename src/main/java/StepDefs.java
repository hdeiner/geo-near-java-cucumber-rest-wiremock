package main.java;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class StepDefs {
	private boolean useFakeServer;
	private static String USERNAME = "howarddeiner";
	private static FullTextWikipediaSearchWithGeoTerms fullTextWikipediaSearchWithGeoTerms;
	private static boolean wasWebServiceInvoked = false;
	private int articleCount;

	@Given("^I am testing on the actual web service$")
	public void i_am_testing_on_the_actual_web_service() throws Throwable {
		useFakeServer = false;
	}

	@Given("^I am testing on the fake web service$")
	public void i_am_testing_on_the_fake_web_service() throws Throwable {
		useFakeServer = true;
	}

	@When("^I search for wikipedia articles mentioning \"([^\"]*)\"$")
	public void i_search_for_wikipedia_articles_mentioning(String searchTerms) throws Throwable {
		if (!wasWebServiceInvoked) {
			fullTextWikipediaSearchWithGeoTerms = new FullTextWikipediaSearchWithGeoTerms(searchTerms, USERNAME, useFakeServer);
			System.out.println("articleCount="+fullTextWikipediaSearchWithGeoTerms.getArticleCount());
			wasWebServiceInvoked = true;
		}
 	}

	@Then("^I should find \"([^\"]*)\" articles")
	public void i_should_find_articles(int articleCount1) throws Throwable {
		articleCount = fullTextWikipediaSearchWithGeoTerms.getArticleCount();
		assertThat("Wrong number of articles returned", articleCount, is(articleCount1));
	}

	@Then("^I should be able to verify title=\\\"([^\\\"]*)\\\", feature=\\\"([^\\\"]*)\\\", fact1=\\\"([^\\\"]*)\\\", fact2=\\\"([^\\\"]*)\\\", fact3=\\\"([^\\\"]*)\\\", fact4=\\\"([^\\\"]*)\\\"$")
	public void i_can_verify(String title, String feature, String fact_1, String fact_2, String fact_3, String fact_4) throws Throwable {
		int rowNumber = fullTextWikipediaSearchWithGeoTerms.getResponseRowFor(title);
		assertThat("Title is wrong",fullTextWikipediaSearchWithGeoTerms.getTitleFor(rowNumber),is(title));
		assertThat("Feature is wrong",fullTextWikipediaSearchWithGeoTerms.getFeatureFor(rowNumber),is(feature));
		assertThat("Fact1 is wrong",fullTextWikipediaSearchWithGeoTerms.getSummaryFor(rowNumber),containsString(fact_1));
		assertThat("Fact2 is wrong",fullTextWikipediaSearchWithGeoTerms.getSummaryFor(rowNumber),containsString(fact_2));
		assertThat("Fact3 is wrong",fullTextWikipediaSearchWithGeoTerms.getSummaryFor(rowNumber),containsString(fact_3));
		assertThat("Fact4 is wrong",fullTextWikipediaSearchWithGeoTerms.getSummaryFor(rowNumber),containsString(fact_4));
	}
}
