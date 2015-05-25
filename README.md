# LabSpy
A application to monitor what students do you your laboratories, and help they when needed.

# How to compile / Run

1. Download the latest JDK and put it inside your **/opt/java** folder (note that your Java binary should be in /opt/java/bin/java).
2. Compile the project and generate .jar files going to the project root and typing `ant` in the Terminal.
3. Run `export PATH=/opt/java/bin:$PATH` in your Terminal.
4. Run the desired JAR file with `java -jar out/artifacts/<Teacher|Student>/<Teacher|Student>.jar`.
5. 

# How to test in a single computer

You'll need a bunch of RAM and, of course, a towel.

1. [Download and install Xvfb](http://packages.ubuntu.com/lucid/xvfb).
2. Start a Terminal session and run `Xvfb :100`
3. Start a second Terminal session and run `DISPLAY=:100 sublime_text` (you can change the last param to `firefox` too)
4. Start a third Terminal session and run `DISPLAY=:100 java -jar Student.jar`.
5. Start a fourth Terminal session and run `java -jar Teacher.jar`.
