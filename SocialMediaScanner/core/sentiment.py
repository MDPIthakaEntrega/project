#from SocialMediaScanner.settings import BASE_DIR
from alchemyapi import AlchemyAPI
import json
import os
BASE_DIR = os.path.dirname(os.path.dirname(__file__))

alchemyapi = AlchemyAPI()



with open(BASE_DIR + 'static/data.json', 'r') as file:
    reviews = json.loads(file.read())
    reviews = reviews["reviews"]

print len(reviews)

analyzed_data = []
for r in reviews:
    content = r["review_text"]
    response = alchemyapi.sentiment('text', content)
    print response
    if response['status'] == 'OK':
        if 'score' in response['docSentiment']:
            r['sentiment_score'] = response['docSentiment']['score']
        else:
            r['sentiment_score'] = 0
        r['sentiment_type'] = response['docSentiment']['type']
    analyzed_data.append(r)
print len(analyzed_data)
final = {u'reviews': analyzed_data}
with open(BASE_DIR + 'static/data.json', 'w+') as file:
    file.write(json.dumps(final))
    file.close()
