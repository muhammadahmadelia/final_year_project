import socket, os

client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(("192.168.43.71", 5005))
k = ''
size = 1024

while(1):
    print ("Do you want to transfer a \n1. Text File\n2.Image \n3.Video\n")
    l = raw_input()
    client_socket.send(k)
    k = int (k)
    if (k == 1):
        print ("Enter file name\n")
        strng = raw_input()
        size = client_socket.recv(1024)
        size = int(size)
        print ("The file size is - ", size, " bytes")
        size = size*2
        strng("\nThe contents of the file - ")
        print (strng)

    if (k==2 or k==3):
        print ("Enter filename of the image with extension ")
        fname = raw_input()
        client_socket.send(fname)
        fname = 'images/'+fname
        fp = open(fname, 'w')
        while True:
            strng = slient_socket.recv(512)
            if not strng:
                break
            fp.write(strng)
        fp.close()
        print ("Data recieved successfully")
        exit()
