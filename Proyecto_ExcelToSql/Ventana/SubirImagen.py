import boto3
from botocore.client import Config

ACCESS_KEY_ID = 'AKIAIRNMEOSSXOCFHKZA'
ACCESS_SECRET_KEY = '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'
BUCKET_NAME = 'evenytstore'
FILE_NAME = 'evenyt1.jpg'

data = open('.\Imagenes\\'+FILE_NAME,'rb')

s3 = boto3.resource(
    's3',
    aws_access_key_id = ACCESS_KEY_ID,
    aws_secret_access_key = ACCESS_SECRET_KEY,
    config = Config(signature_version = 's3v4')
)

s3.Bucket(BUCKET_NAME).put_object(Key=FILE_NAME,Body=data, ContentType='image/jpeg')##inserta la imagen
s3.ObjectAcl(BUCKET_NAME,FILE_NAME).put(ACL='public-read') #Lo hace público



print("Done")