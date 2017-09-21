from django.db import models


class Website(models.Model):
    name = models.CharField(max_length=255)
    language = models.CharField(max_length=15, default="")
    url = models.CharField(max_length=255)
