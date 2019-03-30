# Robot 2019 Arduino
## Arduino-RoboRIO Communication Codes (sent via I2C as `byte` arrays)
### RoboRIO (`writeData`) to Arduino (`readData`)
* `0`: LED strip alliance color
    * `0`: red
    * `1`: blue
    * `2`: green (invalid)
* `1`: LED strip main pattern
    * `0`: off
    * `1`: robot ready
    * `2`: chasing up
    * `3`: green flowing up and down
	* `4` to `24`: elevator height
* `2`: LED strip diagnostic color
    * `0`: red
    * `1`: orange
    * `2`: yellow
    * `3`: green
    * `4`: blue
    * `5`: purple
* `3`: LED strip diagnostic pattern
    * `0`: same as main
    * `1`: solid on
    * `2`: blinking
* `4`: Pixy2 camera
    * `0`: disabled
    * `1`: color mode
### Arduino (`writeData`) to RoboRIO (`readData`)
* `0`: Does Pixy2 see an object?
    * `0`: no
    * `1`: yes
* `1`: X-value of coordinates of object to turn to
    * integer value from `0` to `315` in color mode and `0` to `78` in line mode
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