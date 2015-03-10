__author__ = 'Charlie'

from cassandra.cluster import Cluster
from datetime import datetime
from cassandra_insert_reviews import cassandrainsertreviews

###TEST
test_list = []
reviewRecord = {}
reviewRecord["review_site_name"] = "Facebook"
reviewRecord["review_id"] = "101"
reviewRecord["review_business_name"] = "Zingermans"
reviewRecord["review_url"] = "Zingermans.com"
reviewRecord["review_title"] = ""
reviewRecord["review_author"] = "James Dean"
reviewRecord["review_author_url"] = ""
date_posted = '2014-01-15T01:35:30.314Z'
date_posted = datetime.strptime(date_posted, '%Y-%m-%dT%H:%M:%S.%fZ')
reviewRecord["review_date"] = date_posted
reviewRecord["review_text"] = "Food is Great"
reviewRecord["review_rating"] = 4
reviewRecord["review_helpful_count"] = 3
reviewRecord["review_unhelpful_count"] = ""
reviewRecord["review_type"] = "editorial"
test_list.append(reviewRecord)
###END Test

cluster = Cluster(
    contact_points=['127.0.0.1'],
)

session = cluster.connect()
session.execute("""USE review_keyspace; """)

test_insert_reviews = cassandrainsertreviews()
test_insert_reviews.insert_data(test_list, session)


#print "Success"
#print "Done"

#this is just a test
'''
cluster = Cluster(
    contact_points=['127.0.0.1'],
)
session = cluster.connect('myspace')
result = session.execute("""
select * from users
""")
for x in result:
    print x.lastname
    print x.age
'''