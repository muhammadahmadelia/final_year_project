from socket import *
from time import ctime

HOST = ''
PORT = 21567
BUFSIZE = 1024
ADDR = (HOST, PORT)
ctrCMD = ['Up', 'Down', 'Left', 'Right', 'LED ON', 'LED OFF']

tcpSerSock = socket(AF_INET, SOCK_STREAM)
tcpSerSock.bind(ADDR)
tcpSerSock.listen(5)

while True:
    print ('Waiting for connection')
    tcpCliSock, addr = tcpSerSock.accept()
    #data, addr = sock.recvfrom(BUFSIZE)
    print ('....connected from {}'.format(addr))
    try:
        while True:
            data = ''
            data = tcpCliSock.recv(BUFSIZE)
            data = data.decode('UTF-8')
            #print (data)
            if not data:
                break
            
            if data == ctrCMD[0]:
                print ('Move forward')
                
            if data == ctrCMD[1]:
                print ('Move backward')

            if data == ctrCMD[2]:
                print ('Move left')

            if data == ctrCMD[3]:
                print ('Move right')

            if data == ctrCMD[4]:
                print ('Turn on LED')

            if data == ctrCMD[5]:
                print ('Turn off LED')
        print ("Inner loop")
                
    except KeyboradInterrupt:
        print ('Exception occur ')
                
