set -ex
cd $(dirname $0)

javac-algs4 Permutation.java
java-algs4 -ea Permutation 3 < distinct.txt
java-algs4 -ea Permutation 8 < duplicates.txt

mkdir -p ./output
time=$(date +%Y-%m-%d_%H-%M-%S)
zip output/${time}.result.zip ./*.java
git add --all
git commit --allow-empty -m ${time}
