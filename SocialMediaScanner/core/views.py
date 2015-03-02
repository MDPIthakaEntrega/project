from django.shortcuts import render_to_response, render
from django.http import *
from django.contrib import auth
from core.models import Comment
from core.error import ErrorObject
from FormTemplate import SignupForm
import requests
from django.contrib.auth.models import User
import json
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
    if request == 'POST':
        form = SignupForm(request.POST)
        form_errors = form.errors
        if form.is_valid():
            username = form.cleaned_data['username']
            email = form.cleaned_data['email']
            password = form.cleaned_data['password2']
            confirm = User.objects.create_user(
                username = username,
                email = email,
                password = password)
            confirm.save()
            user = auth.authenticate(username=username, password=password)
            auth.login(request,user)
            return HttpResponseRedirect('/dashboard/')
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
            key = request.POST['keyword']

            checkFb = ('checkFb') in request.POST
            checkTw = ('checkTw') in request.POST
            checkCg = ('checkCg') in request.POST

            print key
            print checkFb
            print checkTw
            print checkCg

            #result = getlist(key, checkFb, checkTw, checkCg)
            result = Comment.objects.all()

            sentiment = []

            for r in result:
                dest =  'https://api.sentigem.com/external/get-sentiment?api-key=' + api_key + '&text=' + r.content
                print dest
                pack = requests.get(dest)
                print pack.json()
            return render(request, 'result.html', {'result' : result})
        else:
            return render(request, 'dashboard.html')
    else:
        error = ErrorObject("Oops!", "You have no permission for this page!")
        return render(request, "error.html", {'error': error})

def result(request):
    return render(request, 'result.html')



