#!/bin/sh

virtualenv venv

# Enter venv
source venv/bin/activate
pip install -r requirements.txt
printf "\n\n\n=====Middle Layer Packages Successfully Installed====\n\n\n"

# Django database migrate
python manage.py makemigrations
python manage.py syncdb
printf "\n\n\n====User Information Database Successfully Setup====\n\n\n" 

npm install --prefix "core/static"
printf "\n\n\n=====React Packages Successfully Installed====\n\n\n"

npm run build --prefix "core/static"
npm run build-signup --prefix "core/static"

printf "\n\n\n=====React Components Successfully Generated====\n\n\n"
deactivate

