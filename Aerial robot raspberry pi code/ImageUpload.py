import sys
import requests
import firebase_admin
from firebase_admin import credentials
from firebase_admin import storage
from datetime import date

cred = credentials.Certificate('robotic-swarm-firebase-adminsdk-z1ko7-d7c8e3e7d2.json')
firebase_admin.initialize_app(cred, {
        'storageBucket' : 'robotic-swarm.appspot.com'
        })
    
bucket = storage.bucket()

def upload(image, imageName, priority):
    today = date.today()
    dateString = "{}-{}-{}/{}".format(today.day, today.month, today.year, priority)
    blob = bucket.blob(dateString+'/'+imageName)
    blob.upload_from_filename(image)
    print(blob.public_url)

    
return blob.public_url
