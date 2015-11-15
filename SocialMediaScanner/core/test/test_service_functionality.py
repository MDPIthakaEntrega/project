from django.test import TestCase, Client, RequestFactory
from core.business import *
from django.contrib.sessions.middleware import SessionMiddleware
from django.contrib.auth.models import User

client = Client()
test_username = "test_username"
test_email = "test_user@test.com"
test_pwd = "test123"
test_area = "ann arbor, mi"
test_company = "test_company"
test_api_config = "{}"


def add_session_to_request(request):
    """Annotate a request object with a session"""
    middleware = SessionMiddleware()
    middleware.process_request(request)
    request.session.save()


class ServiceFunctionalityTest(TestCase):
    def setUp(self):
        self.factory = RequestFactory()

    def test_signup_login_user(self):
        self.user = \
            setup_user_profile(test_username, test_email, test_pwd, test_area, test_company, json.dumps({}))
        request = self.factory.get('/signup/')
        request.user = self.user
        add_session_to_request(request)
        signup_login_user(request, test_username, test_pwd)
        self.assertEqual(user_is_authenticated(request), True)

    def test_create_sys_user(self):
        create_sys_user(test_username, test_email, test_pwd)
        user = User.objects.get(username=test_username)
        self.assertEqual(user.email, test_email)

    def test_log_out_request(self):
        # simulate a logged in user
        request = self.factory.get('/')
        self.user = \
            setup_user_profile(test_username, test_email, test_pwd, test_area, test_company, json.dumps({}))
        request.user = self.user
        add_session_to_request(request)
        self.assertEqual(user_is_authenticated(request), True)
        log_out_user(request)
        self.assertEqual(user_is_authenticated(request), False)

    def test_link_profile_to_sys_user(self):
        user = User(username=test_username, password=test_pwd, email=test_email)
        user.save()
        self.user = user
        link_profile_to_sys_user(test_username, test_area, test_company, test_api_config)
        user_profile = UserProfile.objects.get(user=user)
        # system user should be consistent with user profile
        self.assertEqual(user_profile.user.username, user.username)
        self.assertEqual(user_profile.user.email, user.email)
        # user profile should be set up
        self.assertEqual(user_profile.area, test_area)
        self.assertEqual(user_profile.my_company.company_name, test_company)