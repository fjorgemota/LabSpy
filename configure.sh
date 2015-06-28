#!/usr/bin/env bash

if [[ $1 = "add_teacher_ip" ]]; then
    if [ -z "$2" ]; then
        read -p "Enter the IP of the Teacher computer to be added: " -e TEACHER_IP
    else
        TEACHER_IP="$2"
    fi
    sudo bash -c "echo '$TEACHER_IP' >> /var/lib/LabSpy/addressList"
	echo $TEACHER_IP added successfully to the trusted IP list!

elif [[ $1 = "remove_teacher_ip" ]]; then
    if [ -z "$2" ]; then
        read -p "Enter the IP of the Teacher computer to be removed: " -e TEACHER_IP
    else
        TEACHER_IP="$2"
    fi
    sudo bash -c "grep -v '$TEACHER_IP' /var/lib/LabSpy/addressList > /var/lib/LabSpy/addressList.tmp"
    sudo bash -c "cat /var/lib/LabSpy/addressList.tmp > /var/lib/LabSpy/addressList"
    sudo bash -c "rm /var/lib/LabSpy/addressList.tmp"
	echo $TEACHER_IP removed successfully to the trusted IP list!
else
    echo "Unrecognized command: $1"
    echo "This program has just three commands:"
    echo "./configure.sh add_teacher_ip <ip> # Adds IP to the trusted list of the LabSpy Student"
    echo "./configure.sh remove_teacher_ip <ip> # Removes IP from the trusted list of the LabSpy Student"
fi