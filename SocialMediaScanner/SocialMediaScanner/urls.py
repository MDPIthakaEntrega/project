from django.conf.urls import patterns, include, url
from django.contrib import admin
from core.views import *


urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'SocialMediaScanner.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),
    url(r'^$', index),
    url(r'^login_auth/$', login_auth_view),
    url(r'^signup_auth/$', signup),
    url(r'^login/', login),
    #url(r'^dashboard/', dash),
    url(r'^signup/', signup),
    url(r'^logout/', logout),
    url(r'^settings/', settings),
    url(r'^dashboard/$', dashboard),
    url(r'^dashboard/new_reviews$', newReviews),
)
