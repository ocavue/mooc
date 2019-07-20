set -ex
cd $(dirname $0)

javac-algs4 ./*.java
java-algs4 -ea Point
java-algs4 -ea BruteCollinearPoints input6.txt
# java-algs4 -ea BruteCollinearPoints input8.txt
# java-algs4 -ea FastCollinearPoints  input6.txt
# java-algs4 -ea FastCollinearPoints  input8.txt

mkdir -p ./output
time=$(date +%Y-%m-%d_%H-%M-%S)
zip output/${time}.result.zip BruteCollinearPoints.java FastCollinearPoints.java Point.java
git add --all
git commit --allow-empty -m ${time}
