import socket
from socket import *
import ImageUpload as imageUpload
import time
import re

def sendImages(server_ip):
    time.sleep(10)
    SERVER_IP = '192.168.43.95'
    COMMON_PORT = 5005
    SERVER = (SERVER_IP, COMMON_PORT)
    client = socket(AF_INET, SOCK_STREAM)

    try:
        client.connect((SERVER_IP, COMMON_PORT))
        from_server = client.recv(4096)
        from_server = from_server.decode()

        from_server_array = re.split('[-:]', from_server)
        imagesNumber = int(from_server_array[0])
        priority = int(from_server_array[1])
        print ("Prioirty : {}".format(priority))
        print ("ImageNumber : {}".format(imagesNumber))

        counter = 0
        public_url = [imagesNumber]
        
        while counter != imagesNumber:
            # first take images

            # upload on firebase storage
            uploadingImageName = 'new_cool_image_{}.jpg'.format(counter)
            public_url[counter] = imageUpload.upload('images/image0.jpg', uploadingImageName, priority)
            print(uploadingImageName+" uploaded to firebase storage")
            counter += 1

        #to_server = "Images Uploaded"
        client.send(public_url.encode())
        client.close()
    except:
        print("Exception occured")
