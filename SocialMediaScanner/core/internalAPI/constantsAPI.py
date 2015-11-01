from django.http import HttpResponse
from core.services import *
from core.config.api_config import api_config

"""
This API is used to return constants
"""


def platforms(request):
    if request.method == 'GET':
        response_data = {
            'apis': api_config,
        }
        return HttpResponse(
            json.dumps(response_data),
            content_type="application/json"
        )
