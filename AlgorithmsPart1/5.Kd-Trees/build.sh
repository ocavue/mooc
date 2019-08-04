set -ex
cd $(dirname $0)

javac-algs4 ./*.java

java-algs4 KdTreeVisualizer
java-algs4 NearestNeighborVisualizer ./input1K.txt
java-algs4 RangeSearchVisualizer ./input1K.txt

mkdir -p ./output
time=$(date +%Y-%m-%d_%H-%M-%S)
zip output/${time}.result.zip ./PointSET.java ./KdTree.java
git add --all
git commit --allow-empty -m ${time}
