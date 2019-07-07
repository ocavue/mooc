time=$(date +%Y-%m-%d_%H-%M-%S)
zip output/${time}.result.zip ./Percolation.java ./PercolationStats.java
git add --all
git commit -m ${time}
