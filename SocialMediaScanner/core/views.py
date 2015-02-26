from django.shortcuts import render_to_response, render
from django.http import *
from core.models import Comment

# Create your views here.
# my name is ...


def login(request):
    if request.method == 'POST':
        cname = request.POST['username']
        request.session['name'] = cname
        print(cname)
        return HttpResponseRedirect('/index/')
        print(haha)
    return render(request, 'login.html')


def index(request):
    pass

def dash(request):
    if request.method == 'POST':
        print
    return render(request, 'dashboard.html')
