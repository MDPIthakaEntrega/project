from django.shortcuts import render

__author__ = 'renl'
from SocialMediaScanner.settings import BASE_DIR
from dbHandler.cassandra_pull_reviews import cassandrareviewspuller
from dbHandler.cassandra_select_reviews import CityGridReviewSelector
from core.services import init_new_reviews
import json


def date_handler(obj):
    return obj.isoformat() if hasattr(obj, 'isoformat') else obj


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

    with open(BASE_DIR + '/core/static/new_review_cookie/' + username + '_cookies.json', 'w+') as file:
        file.write(json.dumps(finalizedData, default=date_handler))
        file.close()


def busi_init_new_reviews(username, company_name):
    response = init_new_reviews(username=username, company=company_name)
    if response.status == 200:
        return 0
    else:
        return -1



def get_form_data(form):
    username = form.cleaned_data['username']
    email = form.cleaned_data['email']
    password = form.cleaned_data['password2']
    company_name = form.cleaned_data['cname']
    area = form.cleaned_data['area']
    return username, email, password, company_name, area


def signup_get_helper(request, form_errors):
    if form_errors.get('username') is None:
        form_errors['username'] = ''
    if form_errors.get('email') is None:
        form_errors['email'] = ''
    if form_errors.get('password2') is None:
        form_errors['password2'] = ''
    return render(request, 'register.html',
                  {'error_user': form_errors.get('username'),
                   'error_email': form_errors.get('email'),
                   'error_password': form_errors.get('password2')})

def get_username_password_from(request):
    username = request.POST.get('username', '')
    password = request.POST.get('password', '')
    return username, password