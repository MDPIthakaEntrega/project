__author__ = 'renl'
from django.http import HttpResponse
from core.services import *
from django.http import Http404
from core.models import UserProfile
from core.config.api_config import api_config

def settings_logic(request):
    if user_is_authenticated(request):
        if request.method == "POST":
            username = request.user.username
            user = User.objects.get(username=username)
            userprofile = UserProfile.objects.get(user=user)
            if request.POST['type'] == "account":
                userprofile.api_config = request.POST['configs']
                userprofile.save()
                return HttpResponse(
                    json.dumps({'status': 'success'}),
                    content_type="application/json"
                )
            elif request.POST['type'] == "chart":
                userprofile.chart_config = request.POST['configs']
                userprofile.save()
                return HttpResponse(
                    json.dumps({'status': 'success'}),
                    content_type="application/json"
                )
        elif request.method == "GET":
            part = request.GET.get('part', None)
            username = request.user.username
            user = User.objects.get(username=username)
            userprofile = UserProfile.objects.get(user=user)
            if part == 'account':
                mock_apis = api_config
                response_data = {
                    'apis': mock_apis,
                    'configs': userprofile.api_config
                }
                return HttpResponse(
                    json.dumps(response_data),
                    content_type="application/json"
                )
            elif part == 'chart':
                chart_to_name = {
                    "sentiment_pie_chart": "Sentiment Pie Chart",
                    "bar_chart_ratings": "Bar Chart Ratings",
                    "num_reviews_by_month": "Number of Reviews by Month"
                }
                response_data = {
                    'charts': chart_to_name,
                    'configs': userprofile.chart_config
                }
                return HttpResponse(
                    json.dumps(response_data),
                    content_type="application/json"
                )
    else:
        raise Http404