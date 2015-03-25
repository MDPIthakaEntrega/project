__author__ = 'renl'
from django import forms
from django.core.exceptions import ObjectDoesNotExist
import re
from django.contrib.auth.models import User


class SignupForm(forms.Form):
    username = forms.CharField(max_length = 16)
    email = forms.EmailField()
    password1 = forms.CharField(widget = forms.PasswordInput)
    password2 = forms.CharField(widget = forms.PasswordInput)
    mname = forms.CharField(max_length=100)
    habit = forms.CharField(max_length=10)

    def clean_username(self):
        username = self.cleaned_data['username']
        if not re.search(r'^[\w\']+$', username):
            raise forms.ValidationError('Only letters, numbers and underline can be included!')
        try:
            User.objects.get(username = username)
        except ObjectDoesNotExist:
            return username
        raise forms.ValidationError('Username already exist!')

    def clean_email(self):
        email = self.cleaned_data['email']
        try:
            User.objects.get(email = email)
        except ObjectDoesNotExist:
            return email
        raise forms.ValidationError('Email registered.')

    def clean_password2(self):
        if 'password1' in self.cleaned_data:
            password1 = self.cleaned_data['password1']
            password2 = self.cleaned_data['password2']
            if password1 == password2:
                return password2
            else:
                raise forms.ValidationError('Passwords are not the same.')

    def clean_mname(self):
        print 1
        manager = self.cleaned_data['mname']
        print manager
        return manager

    def clean_habit(self):
        print 2
        habit = self.cleaned_data['habit']
        print habit
        return habit