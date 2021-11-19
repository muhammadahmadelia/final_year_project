import qrcode

img = qrcode.make('111111')

print(type(img))
print(img.size)
# <class 'qrcode.image.pil.PilImage'>
# (290, 290)

img.save('qrcode_test.png')
