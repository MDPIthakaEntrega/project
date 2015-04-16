# Baobab Social Media Scanner
This is an open source solution to combine different information from social media websites of variety. It is an analytics and management tool to help local business owner or individules to manage their reputations better.

# Configurations
* Presentation Layer: JQuery(not consistent version now), Bootstrap 3
* Business Layer: Django 1.7
* Service Layer: Alchemy API, City Grid API
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
Data Layer currently stores reviews about a company or product, which it gets from API's (Currently CityGrid).
Important files (SocialMediaScanner/BackEnd/resources/):
---1). business.txt
------*business.txt specifies the attributes that are desired for each review (for analysis)
------*_Source_ and _Sentiment_ will be determined automatically
------*Any other attributes in this list are potentially provided by the reviews
---------*Example: content is often 'review_text'
------*If the API response (JSON format) contains these attributes 
---2). APICitygrid.txt
------*Each line specifies an attribute in business.txt, and the path to that attribute in the response (JSON) from Citygrid
To add a new API source (e.g. to get reviews from Yelp), you must create a file named _API<APINAME>.txt_ (e.g. APICitygrid.txt for Citygrid).
and place in the **SocialMediaScanner/BackEnd/resources/** folder. 
Format for file:
--<attribute> 				<path-to-attribute>

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

NoSQL Database - Cassandra = {
	
 	'reviews': {
 		'COMPANYNAME' : 'YOURCOMPNAY', e.g. Chipotle
 		'REVIEWID' : 'APINAME_REVIEWID', e.g. Citygrid_12345 
		'JSONFORMATTEDREVIEW' : 'REVIEW', e.g. {"review_id":"ip_10218180545","review_title":"Zingerman's",
												"review_text":"This is a famous deli in Ann Arbor, ...", ...}
	
	}

}


# Internationalization
# https://docs.djangoproject.com/en/1.7/topics/i18n/

LANGUAGE_CODE = 'en-us'
...

```
