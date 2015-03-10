import json
from datetime import datetime
#input: response returned by the citygrid review API (in json format)
#output: a list of records containing "important" information

class CityGridParser(object):

    def parseReview(self,response,user_business_name):
        presponse = json.loads(response)
        data = dict(json.loads(json.dumps(presponse)))
        results = dict(json.loads(json.dumps(data[u'results'])))
        list_review_records = []
        for reviews in results[u'reviews']:
            review = dict(json.loads(json.dumps(reviews)))
            site_name = "CityGrid"
            review_id = review[u'review_id']
            business_name = review[u'business_name']
            review_url = review[u'review_url']
            review_title = review[u'review_title']
            review_author = review[u'review_author']
            review_author_url = review[u'review_author_url']
            review_date = review[u'review_date']
            review_text = review[u'review_text']
            review_rating = review[u'review_rating']
            #pros
            #cons
            helpful_count = review[u'helpful_count']
            unhelpful_count = review[u'unhelpful_count']
            review_type = review[u'type']
            review_records = {}

            #convert review_date to date_time from unicode
            print "review_date ", review_date, " type ", type(review_date)
            date_posted = review_date
            date_posted = datetime.strptime(date_posted, '%Y-%m-%dT%H:%M:%SZ')

            #review_records["site_name"] = site_name
            review_records["review_site_name"] = site_name
            review_records["review_id"] = review_id
            #reviewRecord["business_name"] = business_name /// we don't use this business_name because even for the same business, name varies on citygrid.
            review_records["review_business_name"] = user_business_name
            review_records["review_url"] = review_url
            review_records["review_title"] = review_title
            review_records["review_author"] = review_author
            review_records["review_author_url"] = review_author_url
            review_records["review_date"] = date_posted
            review_records["review_text"] = review_text
            review_records["review_rating"] = review_rating
            review_records["review_helpful_count"] = helpful_count
            review_records["review_unhelpful_count"] = unhelpful_count
            review_records["review_type"] = review_type
            #after constructing the review record, just append it to the answer list
            list_review_records.append(review_records)

        return list_review_records