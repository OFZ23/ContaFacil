#!/bin/bash
cd /home/offz/AndroidStudioProjects/ContaFacil
./gradlew assembleDebug --stacktrace --info > compile_log.txt 2>&1
echo "Compilation finished. Check compile_log.txt for details."

