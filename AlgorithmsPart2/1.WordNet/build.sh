#!/bin/bash

set -ex
cd $(dirname $0)

javac-algs4 ./*.java

java-algs4 -ea WordNet
# java-algs4 SAP
# java-algs4 Outcast

# mkdir -p ./output
# time=$(date +%Y-%m-%d_%H-%M-%S)
# zip output/${time}.result.zip ./*.java
# git add --all
# git commit --allow-empty -m ${time}
