mport cv2
import numpy as np
from pyzbar.pyzbar import decode



def getBarCodeFromImages(myData):
    image = cv2.imread(r'C:/Users/ahmad/Desktop/Auto pilot/barcode.jpg', 0)
    code = decode(image)
    if code == myData:
        return True
    return False
    
def matchBarCode():
    cap = cv2.VideoCapture(0)
    cap.set(3, 640)
    cap.set(4, 480)
    while True:
    
        success, image = cap.read()

        for barcode in decode(image):
            print(barcode.data)
            myData = barcode.data.decode('utf-8')
            print(myData)
            
            pts = np.array([barcode.polygon], np.int32)
            pts = pts.reshape((-1,1,2))
            cv2.polylines(image, [pts], True, (255,0,255), 5)
            pts2 = barcode.rect

            cv2.putText(image, myData, (pts2[0], pts2[1]), cv2.FONT_HERSHEY_SIMPLEX,
                    0.9,(255,0,255))

            getBarCodeFromImages(myData)
            
        cv2.imshow('Result', image)
        cv2.waitKey(1)


