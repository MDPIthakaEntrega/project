from django.db import models
from django.contrib.auth.models import User
# Create your models here.


class Company(models.Model):
    company_name = models.CharField(max_length=100, blank=False, unique=True)

    def __str__(self):
        return self.company_name


class UserProfile(models.Model):
    user = models.OneToOneField(User)
    area = models.CharField(max_length=100, blank=True)
    api_config = models.TextField(default="{}")
    chart_config = models.TextField(default="{}")
    my_company = models.ForeignKey(Company)

