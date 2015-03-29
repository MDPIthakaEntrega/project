from django.db import models
from django.contrib.auth.models import User
# Create your models here.

'''
class CustomUserManager(auth_models.BaseUserManager):
    def create_user(self, company_name, email, password):
        if not email:
            raise ValueError('User must have an email address')

        user = self.model(
            email=CustomUserManager.normalize_email(email),
            company_name=company_name
        )

        user.set_password(password)
        user.save()
        return user

    def create_superuser(self, company_name, email, password):
        user = self.model(
            email=CustomUserManager.normalize_email(email),
            company_name=company_name,
            password=password
        )

        user.is_admin = True
        # user.set_password(password)
        user.save(using=self._db)
        return user


class UserProfile(auth_models.AbstractBaseUser):
    email = models.EmailField(unique=True)
    company_name = models.CharField(max_length=100, unique=True)
    manager_name = models.CharField(max_length=100, blank=True)
    habitCode = models.IntegerField(default=0)
    is_admin = models.BooleanField(default=False)

    objects = CustomUserManager()

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = ['company_name', ]

    def get_full_name(self):
        return self.company_name

    def get_short_name(self):
        return self.company_name

    @property
    def is_staff(self):
        return self.is_admin

'''
class Company(models.Model):
    company_name = models.CharField(max_length=100, blank=False)

    def __str__(self):
        return self.company_name


class UserProfile(models.Model):
    user = models.OneToOneField(User)
    last_logged = models.DateTimeField()
    real_name = models.CharField(max_length=100, blank=True, null=True)
    area = models.CharField(max_length=100, blank=True)
    habit_code = models.IntegerField(default=0)
    competitor_company = models.TextField()
    my_company = models.ForeignKey(Company)


class Comment(models.Model):
    company = models.ForeignKey(UserProfile)
    content = models.TextField()

    def __str__(self):
        return self.content[0]