__author__ = 'renl'
from django.contrib import auth
from django.contrib.auth.models import User
from core.models import UserProfile, Company
from django.core.exceptions import ObjectDoesNotExist
from django.utils.timezone import now
from SocialMediaScanner.settings import CASSANDRA_URL
from django.db import transaction
import httplib
import json


def signup_login_user(request, username, password):
    user = auth.authenticate(username=username, password=password)
    auth.login(request, user)


def create_sys_user(username, email, password):
    confirm = User.objects.create_user(
        username=username,
        email=email,
        password=password)
    confirm.save()


def log_out_user(request):
    auth.logout(request)


def user_is_authenticated(request):
    return request.user.is_authenticated()


def link_profile_to_sys_user(username, area, company_name):
    u = User.objects.get(username=username)
    try:
        Company.objects.get(company_name=company_name)
    except ObjectDoesNotExist:
        c = Company(company_name=company_name)
        c.save()
    c = Company.objects.get(company_name=company_name)
    full_user \
        = UserProfile(user=u,  habit_code=0, area=area, my_company=c, last_logged=now())
    full_user.save()

@transaction.atomic
def setup_user_profile(username, email, password, area, company_name):
    create_sys_user(username, email, password)
    link_profile_to_sys_user(username, area, company_name)


def authen_user(username, password):
    return auth.authenticate(username=username, password=password)


def pure_login(request, user):
    auth.login(request, user)


def is_active(user):
    return user.is_active


def get_username_from_session(requset):
    return requset.user.username


def write_new_reviews_to(path, data):
    with open(path, 'w+') as file:
        file.write(json.dumps(data))


def read_new_reivews(path):
    with open(path, 'r') as file:
        new_reviews = json.loads(file.read())
        return new_reviews["new_reviews"]


def has_same_password(request, raw_origin_password):
    return request.user.check_password(raw_origin_password)


def set_new_password(request, new_password):
    request.user.set_password(new_password)
    request.user.save()


"""
the following functions are prepared for the connection @Apr 8th
"""


def pull_new_reviews(username, company):
    conn = httplib.HTTPConnection(CASSANDRA_URL)
    conn.request("GET", "/pull?username=" + username + "&company=" + company)
    response = conn.getresponse()
    #maybe use deep copy
    conn.close()
    return response


def init_new_reviews(username, company):
    conn = httplib.HTTPConnection(CASSANDRA_URL)
    conn.request("GET",
                 "/initnewreviews?username=" + username + "&company=" + company)
    response = conn.getresponse()
    conn.close()
    return response


