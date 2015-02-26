from cassandra.cluster import Cluster

#this is just a test

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