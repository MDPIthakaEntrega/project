from django.db import models
from django.contrib.auth.models import User
# Create your models here.

class Company(models.Model):
    company_name = models.CharField(max_length=100, blank=False, unique=True)

    def __str__(self):
        return self.company_name


class UserProfile(models.Model):
    user = models.OneToOneField(User)
    last_logged = models.DateTimeField()
    area = models.CharField(max_length=100, blank=True)
    habit_code = models.IntegerField(default=0)
    competitor_company = models.TextField()
    my_company = models.ForeignKey(Company)
