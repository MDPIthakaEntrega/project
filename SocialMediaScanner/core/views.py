from django.shortcuts import render_to_response, render
from django.http import *
from core.models import Comment
# Create your views here.
# my name is ...


def login(request):
    if request.method == "POST":
        cname = request.POST['username']
        request.session['name'] = cname
        print(cname)
        return HttpResponseRedirect('/index/')
        print(haha)
    return render(request, 'login.html')


def index(request):
    pass

def dash(request):
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
        return render(request, 'result.html', {'result' : result})
    else:
        return render(request, 'dashboard.html')

def result(request):
    return render(request, 'result.html')



