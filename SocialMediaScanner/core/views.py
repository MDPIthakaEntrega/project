from django.core.exceptions import ObjectDoesNotExist
from django.shortcuts import render
from django.http import *
from django.contrib import auth
from django.contrib.auth.models import User
from django.utils.timezone import now
from core.models import UserProfile, Company
from core.forms.FormTemplate import SignupForm
from SocialMediaScanner.settings import BASE_DIR
from dbHandler.cassandra_pull_reviews import cassandrareviewspuller
from dbHandler.cassandra_select_reviews import CityGridReviewSelector
from business import *

import json


def index(request):
    return render(request, 'index.html')


def logout(request):
    return log_out_user(request)


def login_view(request):
    return check_status_redirect(request, 'login.html')


def signup_view(request):
    return signup_logic(request)


def login_auth_view(request):
    return login_auth_logic(request)


def settings_view(request):
    if user_is_authenticated(request):
        if request.method == "POST":
            raw_origin_password = request.POST.get('orig_password')
            if not request.user.check_password(raw_origin_password):
                error_info = "The original password is not correct"
                return render(request, "profile.html", {'error': error_info})
            else:
                new_password = request.POST.get('new_pass2')
                print new_password
                request.user.set_password(new_password)
                request.user.save()
                success_info = "Password changed successfully"
                return render(request, "profile.html", {'success': success_info})
        return render(request, "profile.html")
    else:
        return HttpResponseRedirect('/')



def dashboard_view(request):
    if user_is_authenticated(request):
        return render(request, 'index_new.html')
    else:
        return HttpResponseRedirect('/login/')

def new_reviews_view(request):
    return new_reviews_logic(request)




"""
def dash(request):
    #abandon
    if request.user.is_authenticated():
        company_name = request.user.userprofile.my_company.company_name
        username = request.user.username
        if request.method == "POST":
            stringify_new_reviews = request.POST["new_reviews"]
            list_object = json.loads(stringify_new_reviews)
            store_data = {u'new_reviews': list_object}
            with open(BASE_DIR + '/core/static/new_review_cookie/' + username + '_cookies.json', 'w+') as file:
                file.write(json.dumps(store_data))
        pullAndInitializeNewReviews(company_name, username)
        #pullAndInitializeNewReviews(company_name, username)

        with open(BASE_DIR + '/core/static/new_review_cookie/' + username + '_cookies.json', 'r') as file:
            newReviews = json.loads(file.read())
            newReviews = newReviews["new_reviews"]
        return render(request, 'dashboard.html', {'new_reviews': newReviews, 'reviews_num': len(newReviews)})
    else:
        return HttpResponseRedirect('/login/')
"""



