Changes
API Specifications:

* All Parameters are always required
* There are 5 general parameters for pulling data:
    1) 'company'
    2) 'location'
    3) 'yelpName'
    4) 'twitterName'
    5) 'citygridName'

* When specifying API's to pull from, use lowercase, i.e. ('yelp', 'citygrid', or 'twitter')

Even though all API parameters must be passed:
* Pulling from Twitter uses the 'company' paramater
* Pulling from Citygrid uses 'company' and 'location' parameters
* Pulling from Yelp uses 'yelpName'

APIs:

Format:
Name
Description
Type
Parameters
Example

/pull
Pulls data for a specific company from all API's (deprecated)
GET
company, location, yelpName, twitterName, citygridName
http://localhost:3456/pull?company=zingerman%27s&location=ann%20arbor&yelpName=zingermans-delicatessen-ann-arbor-2

/init
Initializes data for a company by pulling from all API's (for signup)
POST
{"company": <company_name>, "twitterName":<twitter_name>, "yelpName":<yelp_name>, "citygridName":<citygrid_name>,
 "location": <location>}
http://localhost:3456/init
{   "company": "zingerman's",
    "citygridName": "zingermans"
    "twitterName": "zingermans-twitter",
    "yelpName":"zingermans-ann-arbor",
    "location":"ann arbor, mi"
}


/updateAPI
Pulls data from specific APIs to update data for a company (same as Init, but can specify API's as a list)
POST
{"company": <company_name>, "twitterName":<twitter_name>, "yelpName":<yelp_name>, "citygridName":<citygrid_name>,
    "location": <location>, "apis": <comma-separated-list-of-apis>}
http://localhost:3456/updateAPI
{   "company": "zingerman's",
    "citygridName": "zingermans"
    "twitterName": "zingermans-twitter",
    "yelpName":"zingermans-ann-arbor",
    "location":"ann arbor, mi"
    "apis" : "twitter"   or   "apis" : "twitter,yelp"  or "apis" : "twitter, yelp" etc...
}

/search
Grabs reviews for a specific company from database, specifying which API's to pull from
    * not specifying any apis will grab reviews from all the API's
    * not specifying a query will return all reviews
GET
company, query(optional), twitter (optional), yelp (optional), citygrid (optional)
http://localhost:3456/search?company=zingerman's
or
http://localhost:3456/search?company=zingerman's&query=tasty
or
http://localhost:3456/search?company=zingerman's&query=tasty&twitter=yes&citygrid=yes



TODO:

Send proper JSON resonses (Spring??)
Grab all reviews from Yelp (not just first 20)