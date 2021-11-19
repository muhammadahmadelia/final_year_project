import ClientServerCommunication as serverCommunication
import FirebaseDataStoring as dataStoring
def controlling():
    server_ip = dataStoring.getIPAddressFromFirebase()
    print (server_ip)
    while True:
        serverCommunication.sendImages(server_ip)
            


controlling()
