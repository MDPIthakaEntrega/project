import urllib
import urllib2

class CityGridReviews(object):

    def searchReviews(self,where,what,publishercode,rating="",days="",rpp="",format="json",sort=""):
        qStr = {'publisher':publishercode, 'where': where, 'what': what}
        url = "http://api.citygridmedia.com/content/reviews/v2/search/where?"

        if len(rating) > 0:
            qStr['rating'] = rating
        if len(days) > 0:
            qStr['days'] = days
        if len(rpp) > 0:
            qStr['rpp'] = rpp
        if len(format) > 0:
            qStr['format'] = format
        if len(sort) > 0:
            qStr['sort'] = sort

        url += urllib.urlencode(qStr)
        #print url
        response = urllib2.urlopen( url ).read()
        return response