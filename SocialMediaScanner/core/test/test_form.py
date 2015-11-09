from django.test import TestCase
from core.forms.FormTemplate import SignupForm
# Create your tests here.


class SignupTest(TestCase):
    def test_forms(self):
        email_error_form = SignupForm({
            'username': 'user1',
            'email': 'test_email',
            'password1': 'password1',
            'password2': 'password1',
            'cname': 'company1',
            'area': 'ann arbor'
        })
        password_error_form = SignupForm({
            'username': 'user1',
            'email': 'test_email@umich.edu',
            'password1': 'password1',
            'password2': 'password2',
            'cname': 'company1',
            'area': 'ann arbor'
        })
        self.assertEqual(email_error_form.is_valid(), False)
        self.assertEqual(password_error_form.is_valid(), False)
