# Robot 2019 Arduino
## Arduino-RoboRIO Communication Codes (sent via I2C as `byte` arrays)
### RoboRIO (`writeData`) to Arduino (`readData`)
* `0`: LED strip first 15
    * `0`: off
    * `1`: robot ready
    * `2`: red chasing up
	* `3`: blue chasing up
    * `4`: green flowing up and down
	* `5` to `25`: red elevator height
	* `26` to `46`: blue elevator height
* `1`: LED strip last 5
    * `0`: same as first 15
    * `1`: red
    * `2`: orange
    * `3`: yellow
    * `4`: green
    * `5`: blue
    * `6`: purple
* `2`: Pixy2 camera
    * `0`: disabled
    * `1`: color mode
    * `2`: line mode
* `3`: Ultrasonic distance sensor
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