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
        response = api_caller.searchReviews(where,user_business_name,publishercode)
        list_review_records = parser.parseReview(response,user_business_name)
        inserter = cassandrainsertreviews()

        ##for testing
        cluster = Cluster(
            contact_points=['127.0.0.1'],
        )

        test_session = cluster.connect()
        test_session.execute("""USE review_keyspace; """)
        #insert into database
        inserter.insert_data(list_review_records, test_session)


