from cassandra.cluster import Cluster

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

session.execute(""" DROP TABLE IF EXISTS test_insert; """)

session.execute("""
   CREATE TABLE IF NOT EXISTS test_insert (
   review_site_name varchar,
   review_id varchar,
   review_rating int,
   PRIMARY KEY(review_site_name, review_id));
   """)

session.execute(""" INSERT INTO test_insert(review_site_name, review_id, review_rating)
    VALUES('citygrid', 'first_review', '1'); """)

#session.execute(""" INSERT INTO test_insert(review_site_name, review_id, review_text)
#    VALUES('citygrid', 'first_review', 'evergreen sucks!!') IF NOT EXISTS; """)

result = session.execute("select * from test_insert;")
for x in result:
    print x.review_site_name
    print x.review_id
    print x.review_rating