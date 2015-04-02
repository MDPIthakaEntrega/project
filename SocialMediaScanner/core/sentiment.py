#from SocialMediaScanner.settings import BASE_DIR
from alchemyapi import AlchemyAPI
import json
import os
BASE_DIR = os.path.dirname(os.path.dirname(__file__))

alchemyapi = AlchemyAPI()



with open(BASE_DIR + 'static/data.json', 'r') as file:
    reviews = json.loads(file.read())
    reviews = reviews["reviews"]

analyzed_data = []
for r in reviews:
    content = r["content"]
    response = alchemyapi.sentiment('text', content)
    print response
    if response['status'] == 'OK':
        if 'score' in response['docSentiment']:
            r['sentiment_score'] = response['docSentiment']['score']
        else:
            r['sentiment_score'] = 0
        r['sentiment_type'] = response['docSentiment']['type']
    analyzed_data.append(r)

final = {u'new_reviews': analyzed_data}
with open(BASE_DIR + 'static/data.json', 'w+') as file:
    file.write(json.dumps(final))
    file.close()
