--run with: "./cqlsh -f <path-to-tbl_create.cql> localhost"
DROP KEYSPACE IF EXISTS review_keyspace;

CREATE KEYSPACE IF NOT EXISTS review_keyspace WITH replication= {'class':'SimpleStrategy', 'replication_factor':1};

CREATE TABLE IF NOT EXISTS review_keyspace.review_table (review_id varchar, company_name varchar, json varchar, 
		PRIMARY KEY(review_id));
		
CREATE TABLE IF NOT EXISTS review_keyspace.inverted_table (review_word varchar, review_id_set set<varchar>, 
		PRIMARY KEY(review_word));
		
CREATE INDEX IF NOT EXISTS company ON review_keyspace.review_table (company_name);