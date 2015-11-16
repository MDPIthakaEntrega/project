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
    all_errors = []
    if form_errors.get('username') is not None:
        all_errors.append(form_errors.get('username'))
    if form_errors.get('email') is not None:
        all_errors.append(form_errors.get('email'))
    if form_errors.get('password2') is not None:
        all_errors.append(form_errors.get('password2'))
    return render(request, 'register.html', {'errors': all_errors})


def get_username_password_from(request):
    username = request.POST.get('username', '')
    password = request.POST.get('password', '')
    return username, password