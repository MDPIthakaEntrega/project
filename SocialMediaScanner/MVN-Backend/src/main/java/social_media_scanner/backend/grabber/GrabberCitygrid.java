package social_media_scanner.backend.grabber;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import social_media_scanner.backend.service.CompanyStruct;
import social_media_scanner.backend.service.ResponseStruct;

/**
 * CityGrid data source grabber, which must extends DataGrabberGeneric and
 * implement pullData method.
 * 
 * @author yuke
 * 
 */
public class GrabberCitygrid extends DataGrabberGeneric {

	private final int MAX_RRP = 50;

	private String generateURL(String companyName, String location, int pageNum, int rpp)
			throws UnsupportedEncodingException {

		String publisherCode = "10000008938";
		String urlBase = "http://api.citygridmedia.com/content/reviews/v2/search/where?";
		String whereParam = "where=" + URLEncoder.encode(location, "UTF-8");
		String whatParam = "what=" + URLEncoder.encode(companyName, "UTF-8");
		String pubParam = "publisher=" + URLEncoder.encode(publisherCode, "UTF-8");
		String formatParam = "format=" + URLEncoder.encode("json", "UTF-8");
		String rppParam = "rpp=" + URLEncoder.encode(String.valueOf(rpp), "UTF-8");
		String pageParam = "page=" + URLEncoder.encode(String.valueOf(pageNum), "UTF-8");

		String url = urlBase + whereParam + "&" + whatParam + "&" + pubParam + "&" + formatParam + "&" + rppParam + "&"
			+ pageParam;

		return url;
    }

    private int getMaxPageNum(String response) throws JSONException {

		JSONObject jsonObj = new JSONObject(response);
		JSONObject rstObj = jsonObj.getJSONObject("results");
		int totalHits = rstObj.getInt("total_hits");

		return (totalHits - 1) / MAX_RRP + 1;
    }

    private String processResponse(String response) {

		String threeDots = "" + (char) 37413;
		String newQuestionMark = "" + (char) 65533;
		response = response.replaceAll(threeDots, "...");
		response = response.replaceAll(newQuestionMark, "\"");

		return response;
    }

    @Override
    public List<ResponseStruct> pullData(CompanyStruct company) {
		// TODO Auto-generated method stub

		String urlForPageNum;
		int maxPageNum = 0;
		String response = null;

        try {
            urlForPageNum = generateURL(company.getCompanyName(), company.getLocation(), 1, 1);
            response = sendGet(urlForPageNum);
            response = processResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            maxPageNum = getMaxPageNum(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<ResponseStruct> responseStructList = new LinkedList<ResponseStruct>();
        for (int i = 1; i <= maxPageNum; i++) {

            try {
                String url = generateURL(company.getCompanyName(), company.getLocation(), i, MAX_RRP);
                System.out.println("pulling Data citygrid with url: ");
                System.out.println(url);
                response = sendGet(url);
                System.out.println("pulled data from citygrid");
                response = processResponse(response);
                responseStructList.add(new ResponseStruct(response, company.getCompanyName(), toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return responseStructList;

    }

    @Override
    public String toString() {

		return "Citygrid";
    }
}