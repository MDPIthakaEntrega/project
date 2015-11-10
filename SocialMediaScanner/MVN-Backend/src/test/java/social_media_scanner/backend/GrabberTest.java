package social_media_scanner.backend;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import social_media_scanner.backend.grabber.DataGrabberGeneric;
import social_media_scanner.backend.grabber.GrabberCitygrid;
import social_media_scanner.backend.grabber.GrabberImportMagicYelp;
import social_media_scanner.backend.grabber.GrabberTwitter;
import social_media_scanner.backend.service.CompanyStruct;
import social_media_scanner.backend.service.ResponseStruct;

/*
 * Unit tests for API data grabbers.
 */
public class GrabberTest extends TestCase {

	public GrabberTest(String testName) {
		super(testName);
	}

	private void testCore(CompanyStruct companyStruct, DataGrabberGeneric grabber) {
		List<ResponseStruct> listResponse = null;
		try {
			listResponse = grabber.pullData(companyStruct, companyStruct.getLocation());
		} catch (Exception e) {
			fail("Pulling data for " + companyStruct.getCompanyName() + " at " + companyStruct.getLocation() + " from "
					+ grabber.toString() + " should not have throwed exceptions");
		}

		assertTrue("Returned response from " + grabber.toString() + " is empty!", listResponse.size() > 0);
	}
	
    public static Test suite()
    {
        return new TestSuite( GrabberTest.class );
    }

	public void testGrabber() {
		GrabberCitygrid grabberCitygrid = new GrabberCitygrid();
		GrabberTwitter grabberTwitter = new GrabberTwitter();
		GrabberImportMagicYelp grabberImportMagicYelp = new GrabberImportMagicYelp();
		CompanyStruct companyStruct = new CompanyStruct("Zingermans", "Zingermans", "Zingermans", "Zingermans",
				"Ann Arbor, MI, 48105");
		testCore(companyStruct, grabberCitygrid);
		testCore(companyStruct, grabberTwitter);
		testCore(companyStruct, grabberImportMagicYelp);
	}
}
