time=$(date +%Y-%m-%d_%H-%M-%S)
cd `dirname $0`
mkdir -p ./output
javac-algs4 Permutation.java
java-algs4 -ea Permutation 3 < distinct.txt
java-algs4 -ea Permutation 8 < distinct.txt
zip output/${time}.result.zip ./*.java
git add --all
git commit --allow-empty -m ${time}
