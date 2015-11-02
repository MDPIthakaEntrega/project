from django.test import TestCase
from core.config.api_config import api_config


class ConfigTest(TestCase):
    def test_api_config_type(self):
        self.assertEqual(isinstance(api_config, list), True)