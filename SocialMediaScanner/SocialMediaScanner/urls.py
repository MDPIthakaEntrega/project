from django.conf.urls import patterns, include, url
from django.contrib import admin
from core.views import *


urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'SocialMediaScanner.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),
    url(r'^$', login),
    url(r'^login/', login),
    url(r'^index/', index),
    url(r'^dashboard/', dash),
)
