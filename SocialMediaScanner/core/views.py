from django.shortcuts import render

# Create your views here.
# my name is ...
def login(request):
    return render(request, 'login.html')
