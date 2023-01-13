#!/bin/bash

classFolder=$1
output=$2

OIFS=$IFS
IFS=$'\n'


for semester in $(ls $classFolder); do
    semesterFolder="$output/$semester"
    mkdir "$semesterFolder"

    for assignment in $(ls -d "$classFolder/$semester/"*/); do
        assignmentNumber=$(ls "$assignment" | sed -r 's|[^0-9]*([0-9\-]+)[^0-9]*|\1|')
        mkdir "$semesterFolder/A$assignmentNumber"
        for student in $(ls -d "${assignment}Assignment $assignmentNumber/"*/); do

            studentID=$(basename $student)
            studentWork="${student}Submission attachment(s)"

            [ ! -d "$studentWork" ] && continue
            localChecksFileNumber=$(echo "$assignmentNumber" | tr '-' '_')

            srcLoc="$(find $studentWork/ -name src -type d | grep -m1 "")"
            [ -z "$srcLoc" ] && continue
            cd "$srcLoc"
            javaFiles="$(find . -name "*.java")"
            
            for javaFile in $javaFiles; do
                dir=$(dirname $javaFile)
                [ ! -d "$semesterFolder/A$assignmentNumber/$studentID/$dir" ] && mkdir -p "$semesterFolder/A$assignmentNumber/$studentID/$dir"
                cp "$javaFile" "$semesterFolder/A$assignmentNumber/$studentID/$dir"
            done
            
        done
    done
done

IFS=$OIFS
echo "done"

