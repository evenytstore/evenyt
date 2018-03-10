import urllib.request
import json
endpoint='https://maps.googleapis.com/maps/api/distancematrix/json?'
api_key='AIzaSyBlFIdKc2KOWGwuBWKdQ8PuWVG-bJwDpmo'
origin= input('Where are you: ').replace(' ','+')
destination=input('Where do yo wantr to go: ').replace(' ','+')
nav_request='origin=()&destination=()&key=()'.format(origin,destination,api_key)
request=endpoint+nav_request
response =urllib.request.urlopen(request).read()
directions=json.loads(response)
print (directions)
