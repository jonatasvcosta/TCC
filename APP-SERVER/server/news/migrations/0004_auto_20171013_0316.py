# -*- coding: utf-8 -*-
# Generated by Django 1.11.2 on 2017-10-13 03:16
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('news', '0003_merge_20171013_0316'),
    ]

    operations = [
        migrations.AlterField(
            model_name='article',
            name='url',
            field=models.URLField(max_length=500, unique=True),
        ),
    ]