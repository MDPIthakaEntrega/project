__author__ = 'renl'
from django.http import HttpResponse
from core.services import *
from django.http import Http404
from core.models import UserProfile


def settings_logic(request):
    if user_is_authenticated(request):
        if request.method == "POST":
            request_data = request.POST.dict()
            if user_is_authenticated(request):
                username = request.user.username
                user = User.objects.get(username=username)
                userprofile = UserProfile.objects.get(user=user)
                userprofile.api_config = json.dumps(request_data)
                userprofile.save()
                return HttpResponse(
                    json.dumps({'status': 'success'}),
                    content_type="application/json"
                )
            else:
                pass
        elif request.method == "GET":
            if user_is_authenticated(request):
                mock_apis = ["Twitter", "Yelp", "CityGrid"]
                username = request.user.username
                user = User.objects.get(username=username)
                userprofile = UserProfile.objects.get(user=user)
                response_data = {
                    'apis': mock_apis,
                    'configs': userprofile.api_config
                }
                return HttpResponse(
                    json.dumps(response_data),
                    content_type="application/json"
                )
        else:
            pass
    else:
        raise Http404