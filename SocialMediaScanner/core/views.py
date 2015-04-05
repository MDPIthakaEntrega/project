from django.core.exceptions import ObjectDoesNotExist
from django.shortcuts import render
from django.http import *
from django.contrib import auth
import json
import operator
from core.models import UserProfile, Company
from core.error import ErrorObject
from FormTemplate import SignupForm
import pytz
from SocialMediaScanner.settings import BASE_DIR
from django.contrib.auth.models import User
from django.utils.timezone import now
# Create your views here.
# my name is ...
from dbHandler.cassandra_pull_reviews import cassandrareviewspuller
from dbHandler.cassandra_select_reviews import CityGridReviewSelector


def index(request):
    request.session.set_test_cookie()
    return render(request, 'index.html')


def logout(request):
    auth.logout(request)
    return HttpResponseRedirect('/')


def login(request):
    if request.user.is_authenticated():
         return HttpResponseRedirect('/dashboard/')
    return render(request, 'login.html')


def signup(request):
    if request.user.is_authenticated():
         return HttpResponseRedirect('/dashboard/')
    form_errors = {'username': '', 'email': '', 'password2': ''}
    if request.method == 'POST':
        form = SignupForm(request.POST)
        form_errors = form.errors
        print "suc catch the post"
        if form.is_valid():
            print "is validate"
            username = form.cleaned_data['username']
            email = form.cleaned_data['email']
            password = form.cleaned_data['password2']
            company_name = form.cleaned_data['cname']
            area = form.cleaned_data['area']
            confirm = User.objects.create_user(
                username=username,
                email=email,
                password=password)
            confirm.save()
            u = User.objects.get(username=username)
            try:
                Company.objects.get(company_name=company_name)
            except ObjectDoesNotExist:
                c = Company(company_name=company_name)
                c.save()
            c = Company.objects.get(company_name=company_name)
            full_user \
                = UserProfile(user=u,  habit_code=0, area=area, my_company=c, last_logged=now())
            full_user.save()
            user = auth.authenticate(username=username, password=password)
            pullAndInitializeNewReviews(company_name, username)
            auth.login(request, user)
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
            print user.username
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


def profile(request):
    if request.user.is_authenticated():
        return render(request, "profile.html", {'error': error_type})
    else:
        return render(request, "index.html")


def dash(request):
    if request.user.is_authenticated():
        company_name = request.user.userprofile.my_company.company_name
        username = request.user.username
        if request.method == "POST":
            stringify_new_reviews = request.POST["new_reviews"]
            list_object = json.loads(stringify_new_reviews)
            store_data = {u'new_reviews': list_object}
            with open(BASE_DIR + '/core/static/new_review_cookie/' + username + '_cookies.json', 'w') as file:
                file.write(json.dumps(store_data))
        #pullAndInitializeNewReviews(company_name, username)
        #pullAndInitializeNewReviews(company_name, username)

        with open(BASE_DIR + '/core/static/new_review_cookie/' + username + '_cookies.json', 'r') as file:
            newReviews = json.loads(file.read())
            newReviews = newReviews["new_reviews"]
        return render(request, 'dashboard.html', {'new_reviews': newReviews, 'reviews_num': len(newReviews)})
    else:
        error = ErrorObject("Oops!", "You have no permission for this page!")
        return render(request, "error.html", {'error': error})


def result(request):
    return render(request, 'result.html')


def date_handler(obj):
    return obj.isoformat() if hasattr(obj, 'isoformat') else obj


def newfeature(request):
    return render(request, 'index_new.html')


def pullAndInitializeNewReviews(company_name, username):
    cassandrareviewspuller.pullReviews(company_name)
    res = CityGridReviewSelector.selectReview(company_name)
    res.sort(key=lambda x: x.review_date, reverse=True)
    temp = res[0: 5]
    print temp
    new_reviews = []
    for i in range(len(temp)):
        dic = {}
        dic['review_id'] = temp[i].review_id
        dic['title'] = temp[i].review_title
        dic['content'] = temp[i].review_text
        dic['time'] = temp[i].review_date
        dic['author'] = temp[i].review_author
        new_reviews.append(dic)
        finalizedData = {u'new_reviews': new_reviews}

    with open(BASE_DIR + '/core/static/new_review_cookie/' + username + '_cookies.json', 'w') as file:
        file.write(json.dumps(finalizedData, default=date_handler))
        file.close()

