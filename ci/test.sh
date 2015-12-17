#!/bin/bash -ex

app_name=$(cat source/manifest.yml | grep "name:" | awk '{print $NF}')

curl -f "$app_name.$CF_APPS_DOMAIN"
exit $?
