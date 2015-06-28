#!/usr/bin/env bash

CURRENT_HOME="$HOME"
if [[ $1 = "student" ]]; then
    if [ -x /etc/init.d/labspy_client ]; then
        sudo /etc/init.d/labspy_client stop
    fi;
	sudo update-rc.d -f labspy_client remove
	sudo rm -f /etc/init.d/labspy_client
	sudo rm -rf /var/lib/LabSpy
	echo "Student successfully uninstalled"
	echo "We will miss you :("
elif [[ $1 = "teacher" ]]; then
	sudo rm -rf ~/.labspy/
	sudo rm -f /usr/local/bin/labspy
	sudo rm -f $CURRENT_HOME/.local/share/applications/labspy.desktop
	echo "Teacher successfully uninstalled"
	echo "We will miss you :("
else
    echo "Unrecognized command: $1"
    echo "This program has just two commands:"
    echo "./uninstall.sh student # Uninstalls LabSpy Student version"
    echo "./uninstall.sh teacher # Uninstalls LabSpy Teacher version"
fi