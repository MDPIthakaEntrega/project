from django.http import HttpResponseRedirect
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
        print "suc catch the post"
        if form.is_valid():
            username, email, password, company_name, area = get_form_data(form)
            setup_user_profile(username, email, password, area, company_name)
            signup_login_user(request, username, password)
            #pullAndInitializeNewReviews(company_name, username)
            #if busi_init_new_reviews == -1
            #print error
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


def settings_page_logic(request):
    if user_is_authenticated(request):
        if request.method == "POST":
            raw_origin_password = request.POST.get('orig_password')
            if not has_same_password(request, raw_origin_password):
                error_info = "The original password is not correct"
                return render(request, "profile.html", {'error': error_info})
            else:
                new_password = request.POST.get('new_pass2')
                set_new_password(request, new_password)
                success_info = "Password changed successfully"
                return render(request, "profile.html", {'success': success_info})
        return render(request, "profile.html")
    else:
        return HttpResponseRedirect('/')


def new_reviews_logic(request):
    if user_is_authenticated(request):
        username = get_username_from_session(request)
        path = BASE_DIR + '/core/static/new_review_cookie/' + username + '_cookies.json'
        if request.method == "POST":
            stringify_new_reviews = request.POST["new_reviews"]
            print stringify_new_reviews
            list_object = json.loads(stringify_new_reviews)
            store_data = {u'new_reviews': list_object}
            print store_data
            write_new_reviews_to(path, store_data)
        newReviews = read_new_reivews(path)
        return render(request, 'new_reviews.html', {'new_reviews': newReviews, 'reviews_num': len(newReviews)})
    else:
        return HttpResponseRedirect('/login/')

def charts_logic(request):
    if user_is_authenticated(request):
        return render(request, 'charts.html')
    else:
        return HttpResponseRedirect('/')

