from datetime import datetime

from cassandra.cluster import Cluster

from citygrid_api_reviews import CityGridReviews
from citygrid_parse_response import CityGridParser


puller = CityGridReviews()
parser = CityGridParser()
response = puller.searchReviews("ann arbor,mi","zingerman","10000008938")
data = parser.parseReview(response, "zingerman")[2]
unicode_date = data["review_date"]
print unicode_date
unicode_date = datetime.strptime(unicode_date, '%Y-%m-%dT%H:%M:%fZ')
#this is just a test


cluster = Cluster(
    contact_points=['127.0.0.1'],
)
session = cluster.connect()

session.execute("""
   CREATE KEYSPACE IF NOT EXISTS test WITH replication
       = {'class':'SimpleStrategy', 'replication_factor':1};
""")

session.execute("""USE test; """)

session.execute(""" DROP TABLE IF EXISTS test_url; """)

session.execute("""
   CREATE TABLE IF NOT EXISTS test_url (
   review_site_name varchar,
   review_id varchar,
   review_date timestamp,
   PRIMARY KEY(review_site_name, review_id));
   """)

bound_statement = session.prepare("""
    INSERT INTO test_url
    (review_site_name,review_id,review_date)
    VALUES ('CityGrid', ?, ?);
""")

list_data = []
list_data.append("first review")
list_data.append(unicode_date)
session.execute(bound_statement.bind((
    list_data))
)

dict1 = {}
dict1["id"] = "121212"
dict1["date"] = "sdfsdfsdf"

s = ""
for x in dict1.keys():
    s = s + x + ", "
print s


#session.execute(""" INSERT INTO test_insert(review_site_name, review_id, review_text)
#    VALUES('citygrid', 'first_review', 'evergreen sucks!!') IF NOT EXISTS; """)

result = session.execute("select * from test_url;")
for x in result:
    print x.review_site_name
    print x.review_id
    print x.review_date