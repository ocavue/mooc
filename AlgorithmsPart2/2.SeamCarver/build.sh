#!/bin/bash

set -ex
cd $(dirname $0)
clear

javac-algs4 -nowarn ./*.java

java-algs4 -ea SeamCarver
open ./output.png

mkdir -p ./output
time=$(date +%Y-%m-%d_%H-%M-%S)
zip output/${time}.result.zip ./*.java
git add --all
git commit --allow-empty -m ${time}
