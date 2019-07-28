set -ex
cd $(dirname $0)

javac-algs4 ./*.java
java-algs4 Board
java-algs4 Solver puzzle04.txt
java-algs4 Solver puzzle3x3-unsolvable.txt

mkdir -p ./output
time=$(date +%Y-%m-%d_%H-%M-%S)
zip output/${time}.result.zip ./*.java
git add --all
git commit --allow-empty -m ${time}
