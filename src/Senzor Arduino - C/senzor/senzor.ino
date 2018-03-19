#include <Servo.h> 

#define TRIG 6
#define ECHO 5
#define GND 4
#define UCC 7
#define SERVOPIN 9

Servo myservo;
int vzdialenost;
int recv = 0, pos = 0;

void setup(){
  Serial.begin(9600);
  
  pinMode(TRIG, OUTPUT);
  pinMode(ECHO, INPUT);
  pinMode(GND, OUTPUT);
  pinMode(UCC, OUTPUT);

  digitalWrite(UCC, HIGH);
  digitalWrite(GND, LOW);
  
  myservo.attach(SERVOPIN);
}

void loop(){
  int duration, distance;
  vzdialenost = 0;   
  
  myservo.write(pos);
  
  if (Serial.available() > 0) {
    recv = Serial.read();
    if (recv == '0' && pos <= 180){
        pos++;
        delay(30);
        for(int i = 0; i < 10; i++){
          digitalWrite(TRIG, HIGH);
          delayMicroseconds(10);
          digitalWrite(TRIG, LOW);
          duration = pulseIn(ECHO, HIGH);
          distance = (duration / 2) / 29; // The speed of sound is 340 m/s or 29 microseconds per centimeter.
          vzdialenost += distance;
          delay(10);
        }
      Serial.print(vzdialenost / 10);
    }
  }
}

