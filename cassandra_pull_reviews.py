from citygrid_api_reviews import CityGridReviews
from citygrid_parse_response import CityGridParser

class cassandrareviewspuller(object):
    def pullReviews(self, user_business_name, where):
        publishercode = "10000008938"
        api_caller = CityGridReviews()
        parser = CityGridParser()
        response = api_caller.searchReviews(where,user_business_name,publishercode)
        list_review_records = parser.parseReview(response,user_business_name)

        #insert into database