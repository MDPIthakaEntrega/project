package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import grabber.DataGrabberGeneric;
import grabber.GrabberCitygrid;
import grabber.GrabberImportMagicYelp;
import grabber.GrabberTwitter;
import service.CompanyStruct;
import service.ResponseStruct;

public class GrabberTest {

    private void testCore(CompanyStruct companyStruct, DataGrabberGeneric grabber) {
	List<ResponseStruct> listResponse = null;
	try{
	    listResponse = grabber.pullData(companyStruct, companyStruct.getLocation());
	} catch(Exception e) {
	    fail("Pulling data for " + companyStruct.getCompanyName() + " at " + companyStruct.getLocation() + " from " + grabber.toString() + " should not have throwed exceptions");
	}
	
	assertTrue("Returned response from " + grabber.toString() + " is empty!", listResponse.size() > 0);
    }
    
    @Test
    public void test() {
	GrabberCitygrid grabberCitygrid = new GrabberCitygrid();
	GrabberTwitter grabberTwitter = new GrabberTwitter();
	GrabberImportMagicYelp grabberImportMagicYelp = new GrabberImportMagicYelp();
	CompanyStruct companyStruct = new CompanyStruct("Zingermans", "Zingermans", "Zingermans", "Zingermans", "Ann Arbor, MI, 48105");
	testCore(companyStruct, grabberCitygrid);
	testCore(companyStruct, grabberTwitter);
	testCore(companyStruct, grabberImportMagicYelp);
    }

}
