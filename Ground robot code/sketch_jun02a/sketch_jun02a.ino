#include <dht.h>
dht DHT;
#include<NewPing.h>
#define rightFront 2
#define rightBack 3
#define leftFront 4
#define leftBack 7
#define ENA 5
#define ENB 6
#define tp 8
#define ep    9
#define md 50000
#define DHT11_PIN 11
unsigned long previousMillis = 0;
const long interval = 1000;
NewPing sonar(tp, ep, md);
int ranturn = 0;
long r1 = 0;
long r2 = 500;
void setup() {
  pinMode( rightFront, OUTPUT);
  pinMode(rightBack, OUTPUT);
  pinMode(leftFront, OUTPUT);
  pinMode(leftBack, OUTPUT);
  pinMode(ENA, OUTPUT);
  pinMode(ENB, OUTPUT);
  Serial.begin(9600);
}
void loop() {
 if(Serial.available()){         
    r = r * (Serial.read() - '0'); 
    Serial.println(r);
    if(r==1){ //Manual movement of robot
    if(r==2){
      forword(100);
    }
    else if(r==8){
      backword(100);
    }
    else if(r==6){
      right(100);
    }
    else if(r==4){
      left(100);
    }
    else if(r==5){
      Stop()
    }
 }
 else if(r==0){ //auto movement of robot
  unsigned long randomDirection=millis();
  unsigned int distance = sonar.ping();       
if(randomDirection-r1 > r2&&ranturn==0){ 
  ranturn= ranturn+1;                    
  r1=randomDirection;
}
else if(randomDirection-r1 > r2&&ranturn==1){
  ranturn = ranturn-1;
  r1=randomDirection;
}
  if (distance  >= 3000) 
  {
    forword(50);
  }
  else if (distance < 3000&&ranturn==0) 
  {
    Stop();
    delay(500);
    backword(90);
    delay(1000);
    left(100);
    delay(500);
    unsigned int distance2 = sonar.ping();
    if (distance2 < 2000){
      left(100);
      delay(500);
      unsigned int distance2 = sonar.ping();
        if (distance2 < 2000){
          left(100);
          delay(500);
      }
    }
     
  }
  else if (distance < 3000&&ranturn==1){
   Stop();
   delay(500);
   backword(75);
   delay(500);
   right(100);
    delay(500);
    unsigned int distance2 = sonar.ping();
    if (distance2 < 2000){
      right(100);
      delay(500);
      unsigned int distance2 = sonar.ping();
        if (distance2 < 2000){
          right(100);
          delay(500);
      }
    }
  }
 }
 else if(r==3){ // get Temprature
  int chk = DHT.read11(DHT11_PIN);
  Serial.println(DHT.temperature);
 }
 else if(r==7){ // get Humidity
  int chk = DHT.read11(DHT11_PIN);
  Serial.println(DHT.humidity);
 }
 else if(r==3){ //get readings
  int chk = DHT.read11(DHT11_PIN);
  Serial.println(DHT.temperature+","+DHT.humidity);
 }
 }   

}

void moveRight(int mode, int percent) 
{
int duty = map(percent, 0, 100, 0, 255);

  switch(mode)
  {
    case 0: 
   digitalWrite(ENA, LOW);
    break;

    case 1: 
    digitalWrite(rightFront, LOW);
    digitalWrite(rightBack, HIGH);
    analogWrite(ENA, duty);
    break;

    case 2: 
    digitalWrite(rightFront, HIGH);
    digitalWrite(rightBack, LOW);
    analogWrite(ENA, duty);
    break;
    }

}


void moveLeft(int mode, int percent) 
{
  int duty = map(percent, 0, 100, 0, 255);
  
  switch(mode)
  {
    case 0: 
    digitalWrite(ENB, LOW);
    break;

    case 1: 
    digitalWrite(leftFront, HIGH);
    digitalWrite(leftBack, LOW);
    analogWrite(ENB, duty);
    break;

    case 2: 
    digitalWrite(leftFront, LOW);
    digitalWrite(leftBack, HIGH);
    analogWrite(ENB, duty);
    break;
  }
}

void forword (int duty)
{
  moveRight(1,duty);
  moveLeft(1,duty);
}

void backword (int duty)
{
moveRight(2,duty);
moveLeft(2,duty);
}

void left (int duty) 
{
  moveRight(1,duty);
  moveLeft(2,duty);
}
void right(int duty)
{
  moveRight(2,duty);
  moveLeft(1,duty);
}

void Stop ()
{
  moveRight(0,0);
  moveLeft(0,0);
}
