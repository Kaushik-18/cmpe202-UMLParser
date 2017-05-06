#!/bin/bash

if [ "$#" = 2 ]; then
java -jar umlparser.jar "$1" "$2"
elif [ "$#" = 3 ]; then
echo "Generating Sequence Diagram "
`rm -rf temp`
`mkdir -p temp`
cp SequenceAspect.aj temp
cp "$2/"*".java" temp
ajc -1.8 temp/*.java temp/*.aj
java -cp ".:./aspectjrt.jar:temp" Main
java -jar umlparser.jar "$1" "$2" "$3"
`rm -rf temp`
`rm -f sequence.txt`
else
echo "Invalid number of arguments !"
fi