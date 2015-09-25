from django.http import HttpResponseRedirect, HttpResponse
from util.utilities import *
from forms.FormTemplate import *
from services import *
from django.contrib import auth
from django.shortcuts import render
__author__ = 'renl'
"""
This file is intended for all the business logic

"""


def log_out_user_logic(request):
    log_out_user(request)
    return HttpResponseRedirect('/')


def log_in_user(request):
    if user_is_authenticated(request):
        return HttpResponseRedirect('/dashboard/')
    return render(request, 'login.html')


def check_status_redirect(request, render_page, redirect_path='/dashboard'):
    if user_is_authenticated(request):
        return HttpResponseRedirect(redirect_path)
    else:
        return render(request, render_page)


def signup_logic(request):
    if user_is_authenticated(request):
        return HttpResponseRedirect('/dashboard/')
    form_errors = {'username': '', 'email': '', 'password2': ''}
    if request.method == 'POST':
        form = SignupForm(request.POST)
        form_errors = form.errors
        if form.is_valid():
            username, email, password, company_name, area = get_form_data(form)
            setup_user_profile(username, email, password, area, company_name)
            signup_login_user(request, username, password)
            return HttpResponseRedirect('/dashboard/')
    return signup_get_helper(request, form_errors)


def login_auth_logic(request):
    username, password = get_username_password_from(request)
    user = authen_user(username, password)
    if user is not None:
        if is_active(user):
            pure_login(request, user)
            return HttpResponseRedirect('/dashboard/')
        else:
            error_type = "Invalid Account"
            return render(request, "login.html", {'error': error_type})
    else:
        error_type = "Incorrect username or password!"
        return render(request, "login.html", {'error': error_type})


