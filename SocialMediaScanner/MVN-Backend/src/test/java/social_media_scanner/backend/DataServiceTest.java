package social_media_scanner.backend;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import social_media_scanner.backend.database.AccessData;
import social_media_scanner.backend.database.Citygrid;
import social_media_scanner.backend.database.CreateReviewKeyspace;
import social_media_scanner.backend.database.ImportMagicYelp;
import social_media_scanner.backend.database.Twitter;
import social_media_scanner.backend.grabber.DataGrabberGeneric;
import social_media_scanner.backend.grabber.GrabberCitygrid;
import social_media_scanner.backend.grabber.GrabberImportMagicYelp;
import social_media_scanner.backend.grabber.GrabberTwitter;
import social_media_scanner.backend.service.CompanyStruct;
import social_media_scanner.backend.service.ResponseStruct;

import com.datastax.driver.core.Session;
import social_media_scanner.backend.service.SearchStruct;

public class DataServiceTest {

	private static CreateReviewKeyspace testCreate = CreateReviewKeyspace.getDefaultInstance();
	
	@BeforeClass
	public static void createKeyspace() {
		testCreate.init();
		AccessData.initializeDatabase("127.0.0.1", "review_keyspace_dummy", "review_table", "inverted_table");

	}
	
	@AfterClass
	public static void deleteKeyspace() {

		AccessData.truncateTables();
		testCreate.close();
	}
	
	@Test
	public void dataTwitter() throws InstantiationException, IllegalAccessException, JSONException, 
		ClassNotFoundException, UnsupportedEncodingException {
		
		AccessData testTwitter = new Twitter();
		
		String response_string = "{\"statuses\":[{\"date\":\"Fri Nov 06 15:02:33 EST 2015\",\"id_str\":662721754758930432"
				+ ",\"rating\":0,\"text\":\"RT @ldwortheey: Dinner with Cait at Zingerman's Roadhouse #parentsweekend "
				+ "#uofm\u2026 https://t.co/r4aMw0LUe4\"},{\"date\":\"Fri Nov 06 14:59:09 EST 2015\",\"id_str\":662720899271909377"
				+ ",\"rating\":0,\"text\":\"#parentsweekend (@ Zingerman's Delicatessen) on #Yelp https://t.co/O1JFfU2fdu\"},"
				+ "{\"date\":\"Fri Nov 06 14:37:11 EST 2015\",\"id_str\":662715371292528640,\"rating\":0,\"text\":\"RT "
				+ "@A2BreakingNews: Heritage Turkeys at Zingerman\u2019s Mail Order: The Thanksgiving turkey most of "
				+ "us are used to is very different than\u2026 http\u2026\"},{\"date\":\"Fri Nov 06 14:29:40 EST "
				+ "2015\",\"id_str\":662713479099363328,\"rating\":1,\"text\":\"Heritage Turkeys at Zingerman\u2019s "
				+ "Mail Order: The Thanksgiving turkey most of us are used to is very different than\u2026 "
				+ "https://t.co/82X07f4SPF\"},{\"date\":\"Fri Nov 06 14:08:18 EST 2015\",\"id_str\":662708101875941376"
				+ ",\"rating\":0,\"text\":\"My coworker the truth! She saw me eating a poor ass muffin and told me about "
				+ "the free Zingerman's upstairs\"},{\"date\":\"Fri Nov 06 12:03:22 EST 2015\",\"id_str\":662676659204673538,\"rating\":2"
				+ ",\"text\":\"Training catered by Zingerman's 👌🏼#LawLibftw\"},{\"date\":\"Mon Nov 02 13:40:50 EST 2015\",\"id_str\":66125163697"
				+ "1438080,\"rating\":0,\"text\":\"it taste good Plum Market/Zingerman's opens at Detroit Metro Airp"
				+ "ort: The new airport sto... https://t.co/JvDOUZOV5N #organic #food #news\"},{\"date\":\"Mon Nov 02 12:12:26"
				+ " EST 2015\",\"id_str\":661229391645642752,\"rating\":0,\"text\":\"Zingerman's Food Tours https://t.co/ZYO0M"
				+ "gFlZr\"},{\"date\":\"Mon Nov 02 11:56:34 EST 2015\",\"id_str\":661225396860379136,\"rating\":0,\"text\":\"No"
				+ "vember/December 2015 Newsletter PDF:   Zingerman\u2019s Newsletter November/December 2015 The post November/D"
				+ "ecember\u2026 https://t.co/h3TrkopWlX\"},{\"date\":\"Mon Nov 02 11:42:10 EST 2015\",\"id_str\":6612217751124"
				+ "66432,\"rating\":1,\"text\":\"Reasons to love November in Ann Arbor: Cranberry Pecan Bread & Lemon Poppyseed "
				+ "Coffeecake at Zingerman's Bakehouse! https://t.co/hl0L5QffpD\"},{\"date\":\"Mon Nov 02 09:38:36 EST 2015\",\"i"
				+ "d_str\":661190677775179776,\"rating\":1,\"text\":\"Folks traveling through DIA will get a taste of #VisitAnn"
				+ "Arbor! Congrats @Zingermans! https://t.co/hCOFCaxXfb\"}]}";
		ResponseStruct response = new ResponseStruct(response_string, "zingerman's", "Twitter");
		

		List<ResponseStruct> responses = new LinkedList<ResponseStruct>();
		responses.add(response);
		testTwitter.insertData(responses);
		
		List<String> APIs = new LinkedList<String>();
		APIs.add("twitter");
		SearchStruct search = new SearchStruct("zingerman's", "", APIs);
		String selection = testTwitter.select(search, new LinkedList<String>());
		JSONObject reviews = new JSONObject(selection);
		assertTrue(reviews.getJSONArray("reviews").length() + "reviews selected, expected 11", 
				reviews.getJSONArray("reviews").length() == 11);	
	}
	
	@Test
	public void dataCitygrid() throws UnsupportedEncodingException, InstantiationException, IllegalAccessException, 
		JSONException, ClassNotFoundException {
	
		AccessData testCitygrid = new Citygrid();
		
		String response_string = "{\"results\":{\"query_id\":null,\"uri\":\"http://api.citygridmedia.com/revie"
				+ "ws/reviews/v2/search/where?format=json&page=1&rpp=50&what=zingerman%27s&histograms=false&where="
				+ "ann+arbor+mi&publisher=10000008938&region_type=circle\",\"first_hit\":1,\"last_hit\":50,\"total_"
				+ "hits\":66,\"page\":1,\"rpp\":50,\"did_you_mean\":null,\"regions\":[{\"type\":\"city\",\"name\":\"An"
				+ "n Arbor, MI\",\"latitude\":42.275434999999995,\"longitude\":-83.731138999999999,\"default_radiu"
				+ "s\":5.3200}],\"histograms\":[],\"reviews\":[{\"review_id\":\"ip_10302154082\",\"review_title\":\"Z"
				+ "ingerman's Delicatessen is great!\",\"review_text\":\"Zingerman's is notorious for its sandwiches "
				+ "and it lives up to the hype.  They variety and quality of the sandwiches is great.  There's also "
				+ "plenty of seating.\",\"pros\":null,\"cons\":null,\"review_rating\":10,\"review_date\":\"2009-07-29"
				+ "T03:46:00Z\",\"review_author\":\"Sally L\",\"helpful_count\":null,\"unhelpful_count\":null,\"typ"
				+ "e\":\"user_review\",\"source\":\"INSIDERPAGES\",\"reference_id\":null,\"source_id\":\"17\",\"attr"
				+ "ibution_logo\":\"http://www.insiderpages.com/images/ip_logo_88x33.jpg\",\"attribution_text\":\"In"
				+ "sider Pages\",\"attribution_url\":\"http://www.insiderpages.com/\",\"listing_id\":5168887,\"busi"
				+ "ness_name\":\"Zingerman's Delicatessen\",\"impression_id\":\"000b000003e7c56cef622d46c29dc2648564"
				+ "72fdbc\",\"review_author_url\":\"http://my.citysearch.com/members/public/profile/Sally+L?i=000b00"
				+ "0003e7c56cef622d46c29dc264856472fdbc\",\"review_url\":\"http://www.insiderpages.com/b/133135850"
				+ "94/zingermans-delicatessen-ann-arbor\",\"public_id\":\"zingermans-delicatessen-ann-arbor-2\"},{\"r"
				+ "eview_id\":\"ip_10218180545\",\"review_title\":\"Zingerman's\",\"review_text\":\"This is a famous"
				+ " deli in Ann Arbor, that is known for its fresh bread, and wonderful, large sandwiches.  Not only"
				+ " do they have sandwiches, but the deli has expanded to lots of items that you can purchase.  Don't"
				+ " get me wrong, the…\",\"pros\":null,\"cons\":null,\"review_rating\":6,\"review_date\":\"2005-09"
				+ "-29T13:53:00Z\",\"review_author\":\"Teresa F\",\"helpful_count\":null,\"unhelpful_count\":null,\"t"
				+ "ype\":\"user_review\",\"source\":\"INSIDERPAGES\",\"reference_id\":null,\"source_id\":\"17\",\"att"
				+ "ribution_logo\":\"http://www.insiderpages.com/images/ip_logo_88x33.jpg\",\"attribution_text\":\"In"
				+ "sider Pages\",\"attribution_url\":\"http://www.insiderpages.com/\",\"listing_id\":5168887,\"busin"
				+ "ess_name\":\"Zingerman's Delicatessen\",\"impression_id\":\"000b0000039441bc4bd0024b8ba8dbcc68418"
				+ "564a5\",\"review_author_url\":\"http://my.citysearch.com/members/public/profile/Teresa+F?i=000b00"
				+ "00039441bc4bd0024b8ba8dbcc68418564a5\",\"review_url\":\"http://www.insiderpages.com/b/133135850"
				+ "94/zingermans-delicatessen-ann-arbor\",\"public_id\":\"zingermans-delicatessen-ann-arbor-2\"}]}}";

		ResponseStruct response = new ResponseStruct(response_string, "zingerman's", "Citygrid");
		
		List<ResponseStruct> responses = new LinkedList<ResponseStruct>();
		responses.add(response);
		testCitygrid.insertData(responses);
		
		List<String> APIs = new LinkedList<String>();
		APIs.add("citygrid");
        SearchStruct search = new SearchStruct("zingerman's", "", APIs);
		String selection = testCitygrid.select(search, new LinkedList<String>());
		JSONObject reviews = new JSONObject(selection);
		assertTrue(reviews.getJSONArray("reviews").length() + "reviews selected, expected 2", 
				reviews.getJSONArray("reviews").length() == 2);

	}
	
	
	@Test
	public void testYelp() throws UnsupportedEncodingException, InstantiationException, IllegalAccessException, 
		JSONException, ClassNotFoundException {
		
		AccessData testYelp = new ImportMagicYelp();
		
		String response_string = "{\"tables\":[{\"guid\":\"a7465cec-9863-4681-9a06-b19cd289c0ce\",\"queryGuid\":\"e8"
				+ "cfcf4a-2743-4ebf-84f7-2c1e4ac83b97\",\"results\":[{\"photobox_image/_source\":\"//s3-media3.fl.yelpcd"
				+ "n.com/photo/wYflXUf8nU2xnOpJtzlNew/60s.jpg\",\"rating_value\":\"10/29/2015\",\"sendmessage_labe"
				+ "l\":\"Send message\",\"image/_alt\":\"5.0 star rating\",\"arrange_value\":\"Stop following Nick H.\",\"p"
				+ "hotobox_link/_source\":\"/user_details?userid=B0lEYDxO-bV2q707JFrjhQ\",\"userlocation_value\":\"Washing"
				+ "ton, DC\",\"photobox_image/_alt\":\"Nick H.\",\"elite15_link/_text\":\"Elite ’15\",\"wrapigcommon_numbe"
				+ "r_2\":80.0,\"image\":\"http://s3-media4.fl.yelpcdn.com/assets/srv0/yelp_styleguide/c2252a4cd43e/assets/i"
				+ "mg/stars/stars_map.png\",\"wrapigcommon_number_2/_source\":\"80\",\"userdisplay_link/_text\":\"Nick H"
				+ ".\",\"image/_source\":\"//s3-media4.fl.yelpcdn.com/assets/srv0/yelp_styleguide/c2252a4cd43e/assets/img/s"
				+ "tars/stars_map.png\",\"review_descriptions\":[\"I don't have much to add that others haven't already m"
				+ "entioned. Suffice it to say that this is one of my favorite places to eat in the whole world. Not an o"
				+ "verstatement.\",\"Recommend stopping here if you are visiting Ann Arbor. It's a truly unique deli. The "
				+ "newly renovated space is wonderful. The staff is incredibly kind and attentive. They have everything a "
				+ "foodie dreams of - sandwhiches, sides, dessert, gelato, coffee, drinks, fancy pantsy olive oils and oth"
				+ "er cooking ingrediants, cheese are, fresh baked bread. The list goes on.\",\"You will leave here full a"
				+ "nd happy.\"],\"smalluseful_value\":\"Useful\",\"photobox_image\":\"http://s3-media3.fl.yelpcdn.com/phot"
				+ "o/wYflXUf8nU2xnOpJtzlNew/60s.jpg\",\"wrapigcommon_number_1\":154.0,\"sharereview_label\":\"Share revi"
				+ "ew\",\"wasthis_label\":\"Was this review …?\",\"elite15_link/_source\":\"/elite\",\"smallcool_value\":\"C"
				+ "ool\",\"photobox_link\":\"http://render.import.io/user_details?userid=B0lEYDxO-bV2q707JFrjhQ\",\"wrapig"
				+ "common_value_1\":\"friends\",\"sendmessage_value\":\"Follow Nick H.\",\"userdisplay_link\":\"http://rend"
				+ "er.import.io/user_details?userid=B0lEYDxO-bV2q707JFrjhQ\",\"wrapigcommon_value_2\":\"reviews\",\"elite1"
				+ "5_link\":\"http://render.import.io/elite\",\"userdisplay_link/_source\":\"/user_details?userid=B0lEYDxO"
				+ "-bV2q707JFrjhQ\",\"smallfunny_value\":\"Funny\",\"wrapigcommon_number_1/_source\":\"154\",\"compliment_l"
				+ "abel\":\"Compliment\"}]}]}";
		
		ResponseStruct response = new ResponseStruct(response_string, "zingerman's", "ImportMagicYelp");
		
		List<ResponseStruct> responses = new LinkedList<ResponseStruct>();
		responses.add(response);
		testYelp.insertData(responses);
		
		List<String> APIs = new LinkedList<String>();
		APIs.add("importmagicyelp");
        SearchStruct search = new SearchStruct("zingerman's", "", APIs);
		String selection = testYelp.select(search, new LinkedList<String>());
		JSONObject reviews = new JSONObject(selection);
		assertTrue(reviews.getJSONArray("reviews").length() + "reviews selected, expected 1", 
				reviews.getJSONArray("reviews").length() == 1);
		
	}
	
//	DataGrabberGeneric temp = new GrabberImportMagicYelp();
//	CompanyStruct zingermans = new CompanyStruct("zingerman's", "zingerman's",
//			"zingerman's", "zingermans-delicatessen-ann-arbor-2", "ann arbor, mi");
//	
//	
//	System.out.println("befor pull yelp");
//	List<ResponseStruct> temp1 = temp.pullData(zingermans, "ann arbor, mi");
//	System.out.println("after pull yelp");
//	int i = 0;
//	for(ResponseStruct resp: temp1) {
//		
//		System.out.println(resp.getAPIName());
//		System.out.println(resp.getCompanyName());
//		System.out.println("Response" + resp.getResponse());
//		if(i == 3) {
//			break;
//		}
//		else {
//			i++;
//		}
//	}
		
}
