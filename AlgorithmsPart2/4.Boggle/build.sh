#!/bin/bash

set -ex
cd $(dirname $0)
clear

javac-algs4 -nowarn ./*.java

java-algs4 -ea BoggleSolver dictionary-algs4.txt board4x4.txt
java-algs4 -ea BoggleSolver dictionary-algs4.txt board-q.txt
time java-algs4 -ea BoggleSolver dictionary-yawl.txt board-points1000.txt

mkdir -p ./output
time=$(date +%Y-%m-%d_%H-%M-%S)
zip output/${time}.result.zip ./*.java
git add --all
git commit --allow-empty -m ${time}
