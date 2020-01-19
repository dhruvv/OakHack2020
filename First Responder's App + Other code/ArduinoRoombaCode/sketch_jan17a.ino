#include <SoftwareSerial.h>
#define rxPin 0
#define txPin 11
// set up a new software serial port:
SoftwareSerial softSerial = SoftwareSerial(rxPin, txPin);
void setup() {
  //delay(2000);
  pinMode(rxPin, INPUT);
  pinMode(txPin, OUTPUT);
  softSerial.begin(19200);
  softSerial.write(128);
  //softSerial.write((byte)00000000);
  delay(200);
  softSerial.write(131);
  //delay(10000)
    

}
void loop() {
  //goForward();


}
void goForward() {
  softSerial.write(137); // Opcode number for DRIVE
  // Velocity (-500 – 500 mm/s)
  softSerial.write((byte)0x00); // 0x00c8 == 200
  softSerial.write((byte)0xc8);
  // Radius (-2000 – 2000 mm)
  // Special case: straight = 32768 or 32767 = hex 8000 or 7FFF
  softSerial.write((byte)128); // hex 80
  softSerial.write((byte)0); // hex 0
}
