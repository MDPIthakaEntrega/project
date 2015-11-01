__author__ = 'renl'
from django.http import HttpResponse
from core.services import *
from django.http import Http404
from core.models import UserProfile

def userinfo(request):
    if request.method == "GET":
        if user_is_authenticated(request):
            username = request.user.username
            user = User.objects.get(username=username)
            userprofile = UserProfile.objects.get(user=user)
            company = userprofile.my_company.company_name
            response_data = {
                'username': username,
                'company': company
            }
            return HttpResponse(
                json.dumps(response_data),
                content_type="application/json"
            )
    else:
        raise Http404