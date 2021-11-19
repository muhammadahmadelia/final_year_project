import barcode
import cv2
from pyzbar.pyzbar import decode
from pyzbar.pyzbar import Decoded

hr = barcode.get_barcode_class('ean13')     #getting the class of the barcode

Hr = hr('1234567891012')                 #information to store in the barcode
 
qr = Hr.save('now created')
