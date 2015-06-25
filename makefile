CURRENT_FOLDER=$(shell pwd)
CURRENT_HOME=$(shell echo $HOME)
APPEND_TO_DESKTOP_ENTRY=sudo tee --append /usr/share/applications/labspy.desktop > /dev/null

install_student: 

install_teacher: uninstall_teacher
	@echo Installing LabSpy for teachers.

	# Installing in the labspy dir.
	mkdir ~/.labspy
	mkdir ~/.labspy/icons
	cp $(CURRENT_FOLDER)/out/artifacts/Teacher/Teacher.jar ~/.labspy/
	cp $(CURRENT_FOLDER)/assets/*.png ~/.labspy/icons/
	cp $(CURRENT_FOLDER)/README.md ~/.labspy/
	mv ~/.labspy/Teacher.jar ~/.labspy/labspy_teacher.jar

	# Creating SH file
	echo "#!/bin/sh" > ~/.labspy/start_labspy.sh
	echo "java -jar ~/.labspy/labspy_teacher.jar" >> ~/.labspy/start_labspy.sh
	sudo ln -s ~/.labspy/start_labspy.sh /usr/local/bin/labspy
	sudo chmod +x /usr/local/bin/labspy

	# Creating desktop entry
	sudo rm -f /usr/share/applications/labspy.desktop
	sudo touch /usr/share/applications/labspy.desktop
	echo "[Desktop Entry]" | $(APPEND_TO_DESKTOP_ENTRY) 
	echo "Encoding=UTF-8" | $(APPEND_TO_DESKTOP_ENTRY) 
	echo "Name=LabSpy Teacher Mode" | $(APPEND_TO_DESKTOP_ENTRY) 
	echo "Comment=An application to monitor what students do in laboratories and help them when needed." | $(APPEND_TO_DESKTOP_ENTRY) 
	echo "Exec=labspy" | $(APPEND_TO_DESKTOP_ENTRY) 
	echo "Icon=$(HOME)/.labspy/icons/labspy_stealth.png" | $(APPEND_TO_DESKTOP_ENTRY) 
	echo "Type=Application" | $(APPEND_TO_DESKTOP_ENTRY) 
	echo "Terminal=false" | $(APPEND_TO_DESKTOP_ENTRY) 
	echo "Categories=Education;" | $(APPEND_TO_DESKTOP_ENTRY) 
	@echo
	@echo
	@echo "LabSpy for teachers was sucessfully installed!"
	@echo "To execute LabSpy, just: "
	@echo 
	@echo "    - Run 'labspy' in your terminal or"
	@echo "    - Find 'LabSpy Teacher Mode' in your GNOME / Unity Dashboard."
	@echo " "

compile:
	ant

uninstall_student:

uninstall_teacher:
	-sudo rm -rf ~/.labspy/
	-sudo rm -f /usr/local/bin/labspy
	-sudo rm -f /usr/share/applications/labspy.desktop

clean:
	rm -rf out/
