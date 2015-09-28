from django.conf.urls import patterns, include, url
from django.contrib import admin
from core.views import *
from core.internalAPI.settingsAPI import *
from core.internalAPI.userInfoAPI import *

urlpatterns = patterns('',
    url(r'^admin/', include(admin.site.urls)),
    url(r'^$', index),
    url(r'^login_auth/$', login_auth_view),
    url(r'^signup_auth/$', signup_view),
    url(r'^login/', login_view),
    url(r'^signup/', signup_view),
    url(r'^logout/', logout),
    url(r'^dashboard/$', dashboard_view),
    url(r'^api/settings/$', settings_logic),
    url(r'^api/user/$', userinfo)
)
