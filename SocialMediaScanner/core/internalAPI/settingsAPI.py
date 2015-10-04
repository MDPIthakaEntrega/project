__author__ = 'renl'
from django.http import HttpResponse
from core.services import *
from django.http import Http404
from core.models import UserProfile


def settings_logic(request):
    if user_is_authenticated(request):
        if request.method == "POST":
            if user_is_authenticated(request):
                username = request.user.username
                user = User.objects.get(username=username)
                userprofile = UserProfile.objects.get(user=user)
                if request.POST['type'] == "account":
                    print request.POST['configs']
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
            else:
                pass
        elif request.method == "GET":
            if user_is_authenticated(request):
                print "here"
                part = request.GET.get('part', None)
                print part
                username = request.user.username
                user = User.objects.get(username=username)
                userprofile = UserProfile.objects.get(user=user)
                if part == 'account':
                    mock_apis = ["Twitter", "Yelp", "CityGrid"]
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