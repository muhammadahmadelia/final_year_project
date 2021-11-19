import cv2
import time

def imageCapture(counter):
    cam = cv2.VideoCapture(0)

    #cv2.namedWindow("Test")

    #img_counter = 0

    #while counter!=0:
    

        #cv2.imshow("Test", frame)

    #time.sleep(5)

    #if not ret:
        #break
    #k = cv2.waitKey(1)
    ret, image = cam.read()
    if ret:
        cv2.waitKey(0)
        img_name = "images/snapshots_{}".format(counter)
        cv2.imwrite(img_name+".jpg", image)
        print ("{} written!".format(img_name))
    #img_counter += 1
    #counter -= 1
    cam.release()
    cv2.destroyAllWindows()

counter = 0
while counter!=5:
    imageCapture(counter)
    counter += 1
