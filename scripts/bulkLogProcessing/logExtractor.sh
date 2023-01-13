#!/bin/bash

classFolder=$1
output=$2

OIFS=$IFS
IFS=$'\n'


for semester in $(ls $classFolder); do
    semesterFolder="$output/$semester"
    mkdir "$semesterFolder"

    #echo "loop 1"

    for assignment in $(ls -d "$classFolder/$semester/"*/); do

        assignmentNumber=$(ls "$assignment" | sed -r 's|[^0-9]*([0-9\-]+)[^0-9]*|\1|')
        #echo "loop 2"
        #echo "Assignment number: $assignmentNumber"
        #echo "Assignment Folder: $assignment"

        for student in $(ls -d "${assignment}Assignment $assignmentNumber/"*/); do
            #echo "loop 3"
            studentID=$(basename $student)
            studentWork="${student}Submission attachment(s)"
            studentFeedBack="${student}Feedback Attachment(s)"

            [ ! -d "$studentWork" ] && continue

            localChecksFileNumber=$(echo "$assignmentNumber" | tr '-' '_')
            dataFile1=$(find $studentWork/ -name *"assignment$localChecksFileNumber"*"csv"*) #| grep -m1 "" )
            
            if [ ! -z "$dataFile1" ]; then
            #    echo "all logs: $dataFile1"
                echo "$dataFile1" | while read -d $'\n' logStyle; do
            #        echo "individual log: $logStyle"
            #        exit
                    [ ! -d "$semesterFolder/$studentID" ] && mkdir "$semesterFolder/$studentID"
                    cp "$logStyle" "$semesterFolder/$studentID"
                done
            fi

            [ ! -d "$studentFeedBack" ] && continue

            dataFile2=$(find $studentFeedBack/ -name *"feedback"*"txt"* | grep -m1 "" )
            dataFile3=$(find $studentFeedBack/ -name *"sources"*"txt"* | grep -m1 "" )
            dataFile4=$(find $studentFeedBack/ -name *"results"*"json"* | grep -m1 "" )

            if [ ! -z "$dataFile2" ]; then
                [ ! -d "$semesterFolder/$studentID" ] && mkdir "$semesterFolder/$studentID"
                cp "$dataFile2" "$semesterFolder/$studentID/feedback$assignmentNumber.txt"
            fi
            if [ ! -z "$dataFile3" ]; then
                [ ! -d "$semesterFolder/$studentID" ] && mkdir "$semesterFolder/$studentID"
                cp "$dataFile3" "$semesterFolder/$studentID/sources$assignmentNumber.txt"
            fi
            if [ ! -z "$dataFile4" ]; then
                [ ! -d "$semesterFolder/$studentID" ] && mkdir "$semesterFolder/$studentID"
                cp "$dataFile4" "$semesterFolder/$studentID/results$assignmentNumber.json"
            fi
        done
    done
done

IFS=$OIFS
echo "done"

