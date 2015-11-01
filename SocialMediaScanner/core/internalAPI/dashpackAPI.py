from django.http import HttpResponse
from core.services import *
from django.http import Http404
from core.config.api_config import api_config
import requests

"""
This API is used to return all the information that a dashboard needs
"""


def data_pack(request):
    if user_is_authenticated(request) and request.method == 'GET':
        username = request.user.username
        user = User.objects.get(username=username)
        user_profile = UserProfile.objects.get(user=user)
        mock_apis = api_config
        chart_to_name = {
            "sentiment_pie_chart": "Sentiment Pie Chart",
            "bar_chart_ratings": "Bar Chart Ratings",
            "num_reviews_by_month": "Number of Reviews by Month"
        }
        url = 'http://localhost:3456/search?company%20name=random%27s&keyword='
        resp = requests.get(url=url)
        temp = json.loads(resp.text)
        reviews = temp['reviews']
        if len(reviews):
            response_data = {
                'apis': mock_apis,
                'charts': chart_to_name,
                'api_config': user_profile.api_config,
                'chart_config': user_profile.chart_config,
                'reviews': resp.text,
                'username': username,
                'company': user_profile.my_company.company_name
            }
            return HttpResponse(
                json.dumps(response_data),
                content_type="application/json"
            )
        else:
            return HttpResponse(
                'not initialized',
                status=400
            )