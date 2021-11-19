from firebase import firebase
from datetime import date
import json
import socket

firebase = firebase.FirebaseApplication('https://robotic-swarm.firebaseio.com/', None)

def getValuesFromFirebase():
    result = firebase.get('/values', None)
    print(result)

def postValuesToFirebase(temperatureValue, humidityValue):
    today_date = date.today()
    dateString = "{}-{}-{}".format(today_date.day, today_date.month, today_date.year)
    data = {
            'Temperature' : temperatureValue,
            'Humidity' : humidityValue,
            'Date' : dateString
        }
    path = '/values/'+dateString
    result = firebase.post(path, dateString, data)
    #path = '/values/'+dateString+'/'+result['name']
    #counter = 1
    #for x in urlList:
        #result2 = firebase.post(path, {'imageURL' : x})
        #counter += 1

def deleteValuesFromFirebase(value):
    firebase.delete('/value', value)

def putValuesToFirebase(temperatureValue, humidityValue, priority):
    today_date = date.today()
    dateString = "{}-{}-{}".format(today_date.day, today_date.month, today_date.year)
    data = {
            'Temperature' : temperatureValue,
            'Humidity' : humidityValue,
            'Date' : dateString,
            'Priority' : priority
        }
    path = '/values/'
    result = firebase.put(path, dateString, data)
    print (result)

def putServerIPtoFirebase(ip):
    path = '/raspberryPiServer/'
    result = firebase.put(path, 'ip_address', ip)
    print(result)

def getIPAddress():
    ss = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    ss.connect(('8.8.8.8', 1))
    ip = ss.getsockname()[0]
    return ip

def getIPAddressFromFirebase():
    server_ip_address = firebase.get('/raspberryPiServer/ip_address', None)
    return server_ip_address

#putValuesToFirebase('25', '35', 1)
#print(getIPAddress())
#putServerIPtoFirebase(getIPAddress())
#ip = getIPAddressFromFirebase()
#print (ip)
