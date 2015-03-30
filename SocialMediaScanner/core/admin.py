from django.contrib import admin
from core.models import UserProfile, Comment, Company


admin.site.register(UserProfile)
admin.site.register(Company)
admin.site.register(Comment)


