from django.shortcuts import render_to_response, render
from django.http import *
from django.contrib import auth
import operator
from core.models import Comment, UserProfile
from core.error import ErrorObject
from FormTemplate import SignupForm
#import requests
from django.contrib.auth.models import User
import json

from dbHandler.cassandra_select_reviews import CityGridReviewSelector
from dbHandler.cassandra_pull_reviews import cassandrareviewspuller
# Create your views here.
# my name is ...



def index(request):
    return render(request, 'index.html')

def logout(request):
    auth.logout(request)
    return HttpResponseRedirect('/')

def login(request):
    return render(request, 'login.html')

def signup(request):
    form_errors = {'username': '', 'email': '', 'password2': ''}
    if request.method == 'POST':
        manager_name = request.POST['mname']
        habit_code = int(request.POST['habit'])
        print manager_name, habit_code
        form = SignupForm(request.POST)
        form_errors = form.errors
        print "suc catch the post"
        if form.is_valid():
            print "is validate"
            username = form.cleaned_data['username']
            email = form.cleaned_data['email']
            password = form.cleaned_data['password2']
            manager_name = form.cleaned_data['mname']
            habit_code = int(form.cleaned_data['habit'])
            confirm = User.objects.create_user(
                username=username,
                email=email,
                password=password)
            confirm.save()
            u = User.objects.get(username=username)
            full_user = UserProfile(user=u, manager_name=manager_name, habit_code=habit_code)
            full_user.save()
            user = auth.authenticate(username=username, password=password)
            auth.login(request, user)
            company_name = request.user.username
            cassandrareviewspuller.pullReviews(company_name)
            return HttpResponseRedirect('/dashboard/')
    if form_errors.get('username') is None:
        form_errors['username'] = ''
    if form_errors.get('email') is None:
        form_errors['email'] = ''
    if form_errors.get('password2') is None:
        form_errors['password2'] = ''
    return render(request, 'register.html', {'error_user': form_errors.get('username'), 'error_email': form_errors.get('email'), 'error_password': form_errors.get('password2')})

def login_auth_view(request):
    username = request.POST.get('username', '')
    password = request.POST.get('password', '')

    user = auth.authenticate(username=username, password=password)

    if user is not None:
        if user.is_active:
            auth.login(request, user)

            return HttpResponseRedirect('/dashboard/')
        else:
            error_type = "Invalid Account"
            return render(request, "login.html", {'error': error_type})
    else:
        error_type = "Incorrect username or password!"
        return render(request, "login.html", {'error': error_type})

def register_auth_view(request):
    pass


def dash(request):
    if request.user.is_authenticated():
        api_key = '34a5b7f2ca689a980fe72844e6482e4eDKF94rOEsBcvIaWnl61PoL80uAYzk2Ri'
        if request.method == "POST":
            kw = request.POST['keyword']

            checkFb = ('checkFb') in request.POST
            checkTw = ('checkTw') in request.POST
            checkCg = ('checkCg') in request.POST

            print kw
            print checkFb
            print checkTw
            print checkCg

            #result = getlist(key, checkFb, checkTw, checkCg)
            #result = Comment.objects.all()
            company_name = request.user.username
            #company_name = "Zingerman's"
            #cassandrareviewspuller.pullReviews(company_name)
            "after insert"
            result = {}
            final = []
            res = CityGridReviewSelector.selectReview(company_name)
            word_list = kw.split()
            for r in res:
                count = 0
                for each in word_list:
                    if each in r.review_text:
                        count += 1
                if count != 0:
                    if count not in result:
                        result[count] = [r]
                    else:
                        result[count].append(r)
            sorted_result = sorted(result.items(),key=operator.itemgetter(0))
            sorted_result.reverse()
            print sorted_result
            for value in sorted_result:
                temp = value[1]
                final += temp

            print "after result", len(result)
            sentiment = []


            return render(request, 'result.html', {'result' : final})
        else:
            return render(request, 'dashboard.html')
    else:
        error = ErrorObject("Oops!", "You have no permission for this page!")
        return render(request, "error.html", {'error': error})

def result(request):
    return render(request, 'result.html')



