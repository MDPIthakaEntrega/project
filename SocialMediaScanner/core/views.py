from business import *


def index(request):
    return render(request, 'index.html')


def logout(request):
    return log_out_user_logic(request)


def login_view(request):
    return check_status_redirect(request, 'login.html')


def signup_view(request):
    return signup_logic(request)


def login_auth_view(request):
    return login_auth_logic(request)


def settings_view(request):
    return settings_page_logic(request)


def dashboard_view(request):
    if user_is_authenticated(request):
        return render(request, 'new_dashboard.html')
    else:
        return HttpResponseRedirect('/login/')


def new_reviews_view(request):
    return new_reviews_logic(request)


def charts_view(request):
    return charts_logic(request)




