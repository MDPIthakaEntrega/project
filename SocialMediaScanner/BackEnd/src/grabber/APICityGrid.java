package grabber;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * CityGrid data source grabber, which must extends DataGrabberGeneric and implement pullData method.
 * @author yuke
 *
 */
public class APICityGrid extends DataGrabberGeneric {
	
	public static void main(String[] args) throws IOException {
		
		DataGrabberGeneric d = new APICityGrid();
		//System.out.println(d.pullData("Zingerman's", "Ann Arbor,48105"));
		String str = d.pullData("Zingerman's", "Ann Arbor,48105").get(0);
		//System.out.println(URLDecoder.decode(str, "UTF-8"));
		System.out.println(str);
	}

	@Override
	public List<String> pullData(String companyName, String location) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String publisherCode = "10000008938";
		String urlBase = "http://api.citygridmedia.com/content/reviews/v2/search/where?";
		String whereParam = "where=" + URLEncoder.encode(location, "UTF-8");
		
		String whatParam = "what=" + URLEncoder.encode(companyName, "UTF-8");
		String pubParam = "publisher=" + URLEncoder.encode(publisherCode, "UTF-8");
		String formatParam = "format=" + URLEncoder.encode("json", "UTF-8");
		
		String url = urlBase + whereParam + "&" + whatParam + "&" + pubParam + "&" + formatParam;
		String response = null;
		try {
			response = sendGet(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String threeDots = "" + (char)37413;
		String newQuestionMark = "" + (char)65533;
		response = response.replaceAll(threeDots, "...");
		response = response.replaceAll(newQuestionMark, "\"");
		
		return Arrays.asList(response);
	}
}