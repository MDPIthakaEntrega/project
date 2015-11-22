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
# test JavaScript
echo ========== TEST JavaScript BEGIN ===========
npm run test --prefix "core/static"
echo =========== TEST JavaScript END ============

# test Python
echo ============= TEST Django BEGIN ============
python manage.py test
echo ============== TEST Django END =============

deactivate
