#!/usr/bin/env bash
CURRENT_FOLDER="$PWD"
CURRENT_HOME="$HOME"

if [[ $1 = "teacher" ]]; then
    bash $CURRENT_FOLDER/uninstall.sh teacher
	echo Installing LabSpy for teachers.

	# Installing in the labspy dir.
	mkdir ~/.labspy
	mkdir ~/.labspy/icons
	mkdir ~/.labspy/bin
	# mkdir -p ~/.labspy/out/artifacts/Student # Don't change, it will broke LabSpy for remote installation if you don't use default installation (for developers it's very bad)
	# mkdir ~/.labspy/assets  # Don't change, it will broke LabSpy for remote installation if you don't use default installation (for developers it's very bad)
	cp $CURRENT_FOLDER/assets/*.png ~/.labspy/icons/
	cp $CURRENT_FOLDER/README.md ~/.labspy/
	cp $CURRENT_FOLDER/assets/labspy.sh ~/.labspy/bin/ # Don't change, it will broke LabSpy for remote installation if you don't use default installation (for developers it's very bad)
	cp $CURRENT_FOLDER/out/artifacts/Teacher/Teacher.jar ~/.labspy/
	cp $CURRENT_FOLDER/out/artifacts/Student/Student.jar ~/.labspy/bin/ # Don't change, it will broke LabSpy for remote installation if you don't use default installation (for developers it's very bad)
	mv ~/.labspy/Teacher.jar ~/.labspy/labspy_teacher.jar

	# Creating SH file
	cp $CURRENT_FOLDER/assets/start_labspy.sh ~/.labspy/start_labspy.sh
	sudo ln -s ~/.labspy/start_labspy.sh /usr/local/bin/labspy
	sudo chmod +x /usr/local/bin/labspy

	# Creating desktop entry
	rm -f $CURRENT_HOME/.local/share/applications/labspy.desktop
	cat $CURRENT_FOLDER/assets/labspy.desktop | sed 's/HOME/'$(echo "$CURRENT_HOME" | sed 's/\//\\\//g')'/g' > $CURRENT_HOME/.local/share/applications/labspy.desktop

	echo
	echo
	echo "LabSpy for teachers was sucessfully installed!"
	echo "To execute LabSpy, just: "
	echo
	echo "    - Run 'labspy' in your terminal or"
	echo "    - Find 'LabSpy Teacher Mode' in your GNOME / Unity Dashboard."
	echo " "
elif [[ $1 = "student" ]]; then
    bash $CURRENT_FOLDER/uninstall.sh  student
	sudo mkdir /var/lib/LabSpy/
	sudo cp $CURRENT_FOLDER/out/artifacts/Student/Student.jar /var/lib/LabSpy/
	sudo cp $CURRENT_FOLDER/assets/labspy.sh /var/lib/LabSpy/
	sudo echo ""
	sudo ln -s /var/lib/LabSpy/labspy.sh /etc/init.d/labspy_client
	sudo chmod +x /etc/init.d/labspy_client
	sudo update-rc.d labspy_client defaults 99 01
	sudo /etc/init.d/labspy_client start
	sudo bash -c "echo '# Trusted IP List for teacher connections' > /var/lib/LabSpy/addressList"
	./configure.sh add_teacher_ip
else
    echo "Unrecognized command: $1"
    echo "This program has just two commands:"
    echo "./install.sh student # Installs LabSpy Student version"
    echo "./install.sh teacher # Installs LabSpy Teacher version"
fi

