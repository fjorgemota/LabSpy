# LabSpy
An application to monitor what students do in laboratories and help them when needed.

# How to install (end-users)

1. Download the latest Oracle JRE and install it in your OS.
2. Download the latest [LabSpy version](https://github.com/fjorgemota/LabSpy/archive/master.zip).
2. Run `./install.sh teacher` or `./install.sh student` from a Terminal window. If you don't have Ant installed or just want to skip compilation, add the flag ` -nc` to the end of command. 
3. Have fun ;)

# How to install (manually)
Manual way to do the things - mainly for developers.

1. Download the latest Oracle JRE and put it inside your **/opt/java** folder (note that your Java binary should be in /opt/java/bin/java).
2. Compile the project and generate .jar files going to the project root and typing `ant` in the Terminal.
3. Run `export PATH=/opt/java/bin:$PATH` in your Terminal.
4. Run the desired JAR file with `java -jar out/artifacts/<Teacher|Student>/<Teacher|Student>.jar`.

