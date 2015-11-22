import json
from django.contrib.auth.models import User

from django.test import TestCase, Client
import httpretty

from core.config.api_config import api_config
from core.models import UserProfile
from core.services import setup_user_profile
from core.test.test_client import TestClient


client = Client()
test_username = "test_username"
test_email = "test_user@test.com"
test_pwd = "test123"
test_area = "ann arbor, mi"
test_company = "test_company"


class InternalAPITest(TestCase):
    def test_constants_platform_api(self):
        response = client.get('/api/constants/platforms')
        self.assertEqual(response.status_code, 200)
        response_json = json.loads(response.content)
        self.assertEqual(response_json['apis'], api_config)

    def test_data_pack_api(self):
        mock_response = {"reviews": [{
                                     "content": "Ann Arbor tradition. If you are a bread snob, or have a weakness of aged, imported, slightly-smelly but oh...",
                                     "title": "Ann Arbor tradition.  If you are a bread snob, or have a ...",
                                     "source": "Citygrid", "sentiment_feeling": "negative",
                                     "sentiment_score": -0.516538, "rating": 0.8, "date": "2007-08-05T21:26:45Z"}]}
        httpretty.register_uri(httpretty.GET, "'http://localhost:3456/search",
                               body=json.dumps(mock_response),
                               content_type="text/plain")
        response = client.get('/api/data/pack')
        self.assertEqual(response.status_code, 401)
        auth_client = TestClient(test_username, test_pwd)
        user = setup_user_profile(test_username, test_email, test_pwd, test_area, test_company, json.dumps({}))
        auth_client.login_user(user)
        response2 = auth_client.get('/api/data/pack')
        self.assertEqual(response2.status_code, 400)

    def test_user_info_api(self):
        auth_client = TestClient(test_username, test_pwd)
        user = setup_user_profile(test_username, test_email, test_pwd, test_area, test_company, json.dumps({}))
        auth_client.login_user(user)
        response = auth_client.get('/api/user/')
        self.assertEqual(response.status_code, 200)
        response_content = json.loads(response.content)
        self.assertEqual(response_content["username"], test_username)
        self.assertEqual(response_content["company"], test_company)


