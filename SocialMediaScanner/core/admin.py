from django.contrib import admin
from core.models import UserProfile, Company


class UserProfileAdmin(admin.ModelAdmin):
    list_display = ('user', 'my_company', 'area')

admin.site.register(UserProfile, UserProfileAdmin)
admin.site.register(Company)
