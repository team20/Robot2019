# Robot2019
Team 20's 2019 Official Robot Code. 
## Arduino-RoboRIO Communication Codes (sent via I2C as `byte` arrays)
### RoboRIO (`writeData`) to Arduino (`readData`)
* `0`: LED strip pattern (still needs to be modified for 2019)
    * `0`: off
    * `1`: robot ready
    * `2`: red alliance, low gear
	* `3`: red alliance, high gear
	* `4`: blue alliance, low gear
	* `5`: blue alliance, high gear
* `1`: Pixy2 camera
    * `0`: disabled
    * `1`: color mode
    * `2`: line mode
* `2`: Ultrasonic distance sensor
    * `0`: disabled
    * `1`: enabled
### Arduino (`writeData`) to RoboRIO (`readData`)
* `0`: Does Pixy2 see an object?
    * `0`: no
    * `1`: yes
* `1`: X-value of coordinates of object to turn to
    * integer value from `0` to `315` in color mode and `0` to `78` in line mode
* `2`: Distance sensor value
    * integer value (inches)
## `if` Loops
### What is an `if` loop?
An `if` loop yields essentially the same result as a `for` loop but instead of automatically running every iteration consecutively, only one iteration is run every time it is called. This allows for multiple things that require `for` loops to run at the same time (not really, just alternating really fast) on a single thread (because the Arduino only has one). It is used in our Arduino code so that one Arduino can handle the LEDs and the Pixy2 camera at the same time. 
### `for` Loop
```c++
int max = 10;
int delayPeriod = 500;
for (int counter = 0; counter < max; counter ++) {
    //code goes here
    delay(delayPeriod);
}
```
### `if` Loop
```c++
unsigned long timeStamp = 0;
int counter = 0;
int max = 10;
int delayPeriod = 500;
if (millis() - timeStamp >= delayPeriod) {
    timeStamp = millis();
    if (!(counter < max))
        counter = 0;
    //code goes here
    counter ++;
}
```
