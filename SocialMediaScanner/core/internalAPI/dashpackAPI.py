from core.services import *

"""
This API is used to return all the information that a dashboard needs
"""


def data_pack(request):
    if user_is_authenticated(request) and request.method == 'GET':
        username = request.user.username
        user = User.objects.get(username=username)
        user_profile = UserProfile.objects.get(user=user)
        return search_relevant_reviews(user_profile)
    else:
        return HttpResponse('not authorized', status=401)