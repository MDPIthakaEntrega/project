from django.db import models
from django.contrib.auth import models as auth_models
# Create your models here.

class UserProfile(auth_models.AbstractBaseUser):
    name = models.CharField(max_length=100, unique=True)
    email = models.EmailField(unique=True)
    managerName = models.CharField(max_length=100, blank=True)
    habitCode = models.IntegerField()


class Comment(models.Model):
    company = models.ForeignKey(Company)
    content = models.TextField()

    def __str__(self):
        return self.content[0]