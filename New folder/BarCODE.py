import cv2
import numpy as np
from pyzbar.pyzbar import decode
import barcode
from pyzbar.pyzbar import Decoded
from barcode.writer import ImageWriter

def barcodeDetectionInImage():
    image = cv2.imread(r'E:\Final Year Project\100% Presentation\Code\New folder\barcode_test-1.svg', 0)
    code = decode(image)
    print(code)

def barcodeDetectionInVideo():
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

        cv2.imshow('Result', image)
        cv2.waitKey(1)


def barCodeGenerator():
    #hr = barcode.get_barcode_class('ean13')     #getting the class of the barcode

    #Hr = hr('1234567891012')                 #information to store in the barcode
 
    #qr = Hr.save('now created')
    EAN = barcode.get_barcode_class('ean13')
    ean = EAN(u'123456789011', writer=ImageWriter())
    fullname = ean.save('my_ean13_barcode')

barCodeGenerator()
#barcodeDetectionInImage()
