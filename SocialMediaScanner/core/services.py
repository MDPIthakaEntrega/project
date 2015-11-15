__author__ = 'renl'
from django.contrib import auth
from django.contrib.auth.models import User
from core.models import UserProfile, Company
from django.core.exceptions import ObjectDoesNotExist
from django.db import transaction
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
    return confirm


def log_out_user(request):
    auth.logout(request)


def user_is_authenticated(request):
    return request.user.is_authenticated()


def link_profile_to_sys_user(username, area, company_name, api_config):
    u = User.objects.get(username=username)
    try:
        Company.objects.get(company_name=company_name)
    except ObjectDoesNotExist:
        c = Company(company_name=company_name)
        c.save()
    c = Company.objects.get(company_name=company_name)
    full_user \
        = UserProfile(user=u, area=area, my_company=c, api_config=api_config)
    full_user.save()
    return u

@transaction.atomic
def setup_user_profile(username, email, password, area, company_name, api_config):
    create_sys_user(username, email, password)
    return link_profile_to_sys_user(username, area, company_name, api_config)


def authen_user(username, password):
    return auth.authenticate(username=username, password=password)


def pure_login(request, user):
    auth.login(request, user)


def is_active(user):
    return user.is_active


def get_username_from_session(request):
    return request.user.username


def has_same_password(request, raw_origin_password):
    return request.user.check_password(raw_origin_password)


def set_new_password(request, new_password):
    request.user.set_password(new_password)
    request.user.save()





