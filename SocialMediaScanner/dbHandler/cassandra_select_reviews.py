from cassandra.cluster import Cluster

class CityGridReviewSelector(object):

    @staticmethod
    def selectReview(user_business_name):
        cluster = Cluster(
            contact_points=['127.0.0.1']
        )
        session = cluster.connect()
        session.execute("USE review_keyspace")
        result = session.execute("""
            SELECT * FROM review_table WHERE review_business_name = user_business_name;
        """)

        return result