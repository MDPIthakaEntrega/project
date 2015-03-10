
class cassandrainsertreviews(object):

    def insert_data(self, list_of_reviews, cass_session):
        for review in list_of_reviews:
            list_parameters = []
            firstcommand = "INSERT INTO review_table ("
            secondcommand = "VALUES ("
            for x in review.keys():
                if review[x] != "":
                    list_parameters.append(review[x])
                    firstcommand = firstcommand + x + ","
                    secondcommand = secondcommand + "?,"
            firstcommand = firstcommand[:-1]
            secondcommand = secondcommand[:-1]
            firstcommand = firstcommand + ")"
            secondcommand = secondcommand + ");"
            command = firstcommand + secondcommand
            bound_statement = cass_session.prepare(command)
            cass_session.execute(bound_statement.bind((list_parameters)))
            #result = cass_session.execute("select * from review_table")
            #for y in result:
            #    print y



