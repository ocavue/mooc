#!/bin/bash

set -ex
cd $(dirname $0)
clear

javac-algs4 -nowarn ./*.java
clear

java-algs4 -ea WordNet
java-algs4 -ea SAP digraph1.txt
java-algs4 -ea SAP digraph2.txt
java-algs4 -ea Outcast synsets.txt hypernyms.txt outcast5.txt outcast8.txt outcast11.txt

mkdir -p ./output
time=$(date +%Y-%m-%d_%H-%M-%S)
zip output/${time}.result.zip ./*.java
git add --all
git commit --allow-empty -m ${time}
