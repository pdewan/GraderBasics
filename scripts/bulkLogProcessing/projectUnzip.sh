#!/bin/bash

classFolder=$1

OIFS=$IFS
IFS=$'\n'


for semester in $(ls "$classFolder"); do

    #echo "$semester"

    for assignment in $(ls -d "$classFolder/$semester/"*/); do

        #echo "$assignment"

        assignmentNumber=$(ls "$assignment" | sed -r 's|[^0-9]*([0-9\-]+)[^0-9]*|\1|')

        for student in $(ls -d "${assignment}Assignment $assignmentNumber/"*/); do
           studentWork="${student}Submission attachment(s)"
           

            zippedProjects="$(find $studentWork/ -maxdepth 3 -name \*".zip"*)"
            #echo "$zippedProjects"
            
            for zipped in $zippedProjects; do
                unzip -n "$zipped" -d "$(dirname $zipped)"
                echo "unzipped: $(dirname $zipped)"
            done
        done
    done
done

IFS=$OIFS
echo "done"

