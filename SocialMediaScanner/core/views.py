from django.shortcuts import render
from models import Comment

# Create your views here.
# my name is ...
def login(request):
    if request.method == 'POST':
        cname = request.POST['username']
        print cname
    return render(request, 'login.html')

def index(request):
    return render(request, 'login.html')
