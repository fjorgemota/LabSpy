#!/bin/sh

# Only compile if flag "-nc" (-not-compile) isn't set. 
if [[ $2 != "-nc" ]]; then
    make compile
fi

if [[ $1 = "teacher" ]]; then
    make install_teacher
elif [[ $1 = "student" ]]; then
    make install_student
else
    echo "Something went wrong, this command exists?"
fi

