package social_media_scanner.backend.service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by charlie on 11/13/15.
 */
public class SearchStruct {


    String companyName;
    String keyword;
    List<String> APIs = new LinkedList<>();

    public SearchStruct(String companyName_in, String keyword_in, List<String> APIs_in) {
        this.companyName = companyName_in;
        this.keyword = keyword_in;
        if(APIs_in.isEmpty()) {
            this.APIs.add("Twitter");
            this.APIs.add("ImportMagicYelp");
            this.APIs.add("Citygrid");
        }
        else {
            APIs.addAll(APIs_in);
        }
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<String> getAPIs() {
        return APIs;
    }

    public void setAPIs(List<String> APIs) {
        this.APIs = APIs;
    }
}
