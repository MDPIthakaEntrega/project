from cassandra.cluster import Cluster
cluster = Cluster(
    contact_points=['127.0.0.1'],
)

session = cluster.connect()
session.execute("""
   CREATE KEYSPACE IF NOT EXISTS review_keyspace WITH replication
       = {'class':'SimpleStrategy', 'replication_factor':1};
""")

session.execute("""USE review_keyspace; """)


session.execute("""
   CREATE TABLE IF NOT EXISTS review_table (
   review_site_name varchar,
   review_id varchar,
   review_business_name varchar,
   review_url varchar,
   review_title varchar,
   review_author varchar,
   review_author_url varchar,
   review_date timestamp,
   review_text varchar,
   review_rating int,
   review_pros varchar,
   review_cons varchar,
   review_helpful_count int,
   review_unhelpful_count int,
   review_type varchar,
   PRIMARY KEY(review_site_name, review_id));
   """)

session.execute("""
CREATE INDEX IF NOT EXISTS user_business_review_index
ON review_keyspace.review_table (review_business_name);
""")


