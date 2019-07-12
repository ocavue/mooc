time=$(date +%Y-%m-%d_%H-%M-%S)
mkdir -p ./output
zip output/${time}.result.zip ./*.java
git add --all
git commit --allow-empty -m ${time}
