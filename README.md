# Baobab Social Media Scanner
This is an open source solution to combine different information from social media websites of variety. It is an analytics and management tool to help local business owner or individules to manage their reputations better.

# Configurations
* Presentation Layer: JQuery(not consistent version now), Bootstrap 3
* Business Layer: Django 1.7
* Service Layer: JRE 1.7 or later, Alchemy API, City Grid API
* Data Layer: NoSQL: Cassandra 2.0.14, SQL: any database connectable with Django

# Installation
Clone the repo to local.

# Setup
* Create a Django settings.py at SocialMediaScanner/SocialMediaScanner/
* Setup the structural database, take MySQL for example
```python
...
WSGI_APPLICATION = 'SocialMediaScanner.wsgi.application'


# Database
# https://docs.djangoproject.com/en/1.7/ref/settings/#databases

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.mysql',
        'NAME': 'socialmediascanner',
        'USER': 'USERNAME',
        'PASSWORD': 'YOURPASSWORD',
        'HOST': 'YOURHOST',   # Or an IP Address that your DB is hosted on
        'PORT': 'YOURPORT, 3306 FOR DEFAULT',
    }
}



# Internationalization
# https://docs.djangoproject.com/en/1.7/topics/i18n/

LANGUAGE_CODE = 'en-us'
...

```

*When adding new social media APIs, add one line for each API to the 'BackEnd/resources/ServiceInit.conf' with the following format: [API Name] + " NO", and then when the server starts, it will automatically grabber data from those APIs for all existing users.