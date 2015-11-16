from core.services import *
from django.http import Http404
from core.models import UserProfile
from core.config.api_config import api_config
from core.config.chart_config import chart_to_name


def settings_logic(request):
    if user_is_authenticated(request):
        if request.method == "POST":
            username = request.user.username
            user = User.objects.get(username=username)
            user_profile = UserProfile.objects.get(user=user)
            if request.POST['type'] == "account":
                update_reviews(user_profile, request.POST['configs'])
                user_profile.api_config = request.POST['configs']
                user_profile.save()
                return HttpResponse(
                    json.dumps({'status': 'success'}),
                    content_type="application/json"
                )
            elif request.POST['type'] == "chart":
                user_profile.chart_config = request.POST['configs']
                user_profile.save()
                return HttpResponse(
                    json.dumps({'status': 'success'}),
                    content_type="application/json"
                )
        elif request.method == "GET":
            part = request.GET.get('part', None)
            username = request.user.username
            user = User.objects.get(username=username)
            user_profile = UserProfile.objects.get(user=user)
            if part == 'account':
                response_data = {
                    'apis': api_config,
                    'configs': user_profile.api_config
                }
                return HttpResponse(
                    json.dumps(response_data),
                    content_type="application/json"
                )
            elif part == 'chart':
                response_data = {
                    'charts': chart_to_name,
                    'configs': user_profile.chart_config
                }
                return HttpResponse(
                    json.dumps(response_data),
                    content_type="application/json"
                )
    else:
        raise Http404