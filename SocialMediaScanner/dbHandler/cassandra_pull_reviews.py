import json
from dbHandler.citygrid_api_reviews import CityGridReviews
from dbHandler.citygrid_parse_response import CityGridParser
from dbHandler.cassandra_insert_reviews import cassandrainsertreviews
from cassandra.cluster import Cluster

class cassandrareviewspuller(object):

    @staticmethod
    def pullReviews(user_business_name, where = "Ann Arbor"):
        publishercode = "10000008938"
        api_caller = CityGridReviews()
        parser = CityGridParser()
        response = api_caller.searchReviews(where,user_business_name,publishercode, rpp = "1")
        presponse = json.loads(response)
        data = dict(json.loads(json.dumps(presponse)))
        results = dict(json.loads(json.dumps(data[u'results'])))
        total = results[u'total_hits']
        print "total type", type(total)
        pages = total/50
        list_review_records = []
        for p in range(pages+1):
            rep = api_caller.searchReviews(where,user_business_name,publishercode,rpp="50",page=str(p+1))
            list = parser.parseReview(rep,user_business_name)
            list_review_records += list
        print "length of list", len(list_review_records)
        print list_review_records
#       list_review_records = parser.parseReview(response,user_business_name)
        inserter = cassandrainsertreviews()
        ##for testing
        cluster = Cluster(
            contact_points=['127.0.0.1'],
        )

        test_session = cluster.connect()
        test_session.execute("""USE review_keyspace; """)
        #insert into database
        inserter.insert_data(list_review_records, test_session)

