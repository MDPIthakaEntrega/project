from django.shortcuts import render

__author__ = 'renl'
from SocialMediaScanner.settings import BASE_DIR
import json


def date_handler(obj):
    return obj.isoformat() if hasattr(obj, 'isoformat') else obj


def get_form_data(form):
    username = form.cleaned_data['username']
    email = form.cleaned_data['email']
    password = form.cleaned_data['password2']
    company_name = form.cleaned_data['cname']
    area = form.cleaned_data['area']
    return username, email, password, company_name, area


def signup_get_helper(request, form_errors):
    if form_errors.get('username') is None:
        form_errors['username'] = ''
    if form_errors.get('email') is None:
        form_errors['email'] = ''
    if form_errors.get('password2') is None:
        form_errors['password2'] = ''
    return render(request, 'register.html',
                  {'error_user': form_errors.get('username'),
                   'error_email': form_errors.get('email'),
                   'error_password': form_errors.get('password2')})


def get_username_password_from(request):
    username = request.POST.get('username', '')
    password = request.POST.get('password', '')
    return username, password