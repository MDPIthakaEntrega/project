from django.shortcuts import render_to_response, render
from django.http import *
from models import Comment

# Create your views here.
# my name is ...


def login(request):
    if request.method == 'POST':
        cname = request.POST['username']
        request.session['name'] = cname
        print cname
        return HttpResponseRedirect('/index/')
        print haha
    return render(request, 'login.html')


def index(request):
    name = request.session['name']
    print name
    comments = Comment.objects.filter(company__name=name)
    print comments
    return render_to_response('index.html', {'comments': comments})
