from django.http import HttpResponse
from django.contrib import auth
from django.contrib.auth.models import User
from core.models import UserProfile, Company
from django.core.exceptions import ObjectDoesNotExist
from django.db import transaction
from core.config.api_config import api_config
from core.config.chart_config import chart_to_name
import requests
import json


def search_relevant_reviews(user_profile):
    api_config_json = json.loads(user_profile.api_config)
    api_filter = ''
    for api in api_config:
        api_filter = \
            api_filter+'&'+api+'='+('no' if (api not in api_config_json or api_config_json[api] == '') else 'yes')
    url = 'http://localhost:3456/search?company='+\
          user_profile.my_company.company_name+api_filter
    try:
        resp = requests.get(url=url)
        temp = json.loads(resp.text)
        reviews = temp['reviews']
        if len(reviews):
            response_data = {
                'apis': api_config,
                'charts': chart_to_name,
                'api_config': user_profile.api_config,
                'chart_config': user_profile.chart_config,
                'reviews': resp.text,
                'username': user_profile.user.username,
                'company': user_profile.my_company.company_name
            }
            return HttpResponse(
                json.dumps(response_data),
                content_type="application/json"
            )
        else:
            return HttpResponse(
                'No review found',
                status=400
            )
    except:
        return HttpResponse(
            'Account not initialized or review server is down,' +
            ' please go to the setting section and try to pull data again',
            status=400
        )


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


def init_company_reviews(api_config, company_name, area):
    payload = {
        'citygridName': api_config.get('CityGrid', ''),
        'twitterName': api_config.get('Twitter', ''),
        'company': company_name,
        'yelpName': api_config.get('Yelp', ''),
        'location': area
    }
    return requests.post("http://localhost:3456/init", data=payload)


def update_reviews(user_profile, new_api_config):
    old_api_config = json.loads(user_profile.api_config)
    new_api_config = json.loads(new_api_config)
    changed_apis = ''
    payload = {
        'location': user_profile.area,
        'company': user_profile.my_company.company_name
    }
    for platform in new_api_config:
        if old_api_config.get(platform, '') == '' and old_api_config.get(platform, '') != new_api_config.get(platform, ''):
            if changed_apis != '':
                changed_apis = changed_apis + ', ' + platform.lower()
            else:
                changed_apis = platform.lower()
    payload['apis'] = changed_apis
    for platform in new_api_config:
        key = platform.lower() + 'Name'
        payload[key] = new_api_config[platform]
    return requests.post("http://localhost:3456/updateAPI", data=payload)






