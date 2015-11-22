#!/bin/sh

# Enter venv
source venv/bin/activate
pip install -r requirements.txt

# Django database migrate
echo ======== MIGRATE DATABASE BEGIN ============
python manage.py makemigration
python manage.py syncdb
echo ========== MIGRATE DATABASE END ============

npm install --prefix "core/static"
npm run build --prefix "core/static"
npm run build-signup --prefix "core/static"
deactivate
