#!/bin/bash

if [[ $1 = "teacher" ]]; then
    make install_teacher
elif [[ $1 = "student" ]]; then
    make install_student
else
    echo "Something went wrong, this command exists?"
fi

