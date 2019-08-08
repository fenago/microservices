Install kubectl binary with curl on Linux
Download the latest release with the command:
```
curl -LO https://storage.googleapis.com/kubernetes-release/release/`curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt`/bin/linux/amd64/kubectl
```
To download a specific version, replace the $(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt) portion of the command with the specific version.

For example, to download version v1.15.0 on Linux, type:
```
curl -LO https://storage.googleapis.com/kubernetes-release/release/v1.15.0/bin/linux/amd64/
```
Make the kubectl binary executable.
```
chmod +x ./kubectl
```
Move the binary in to your PATH.
```
sudo mv ./kubectl /usr/local/bin/kubectl
```
Test to ensure the version you installed is up-to-date:
```
kubectl version
```
or
```
cd ~/.kube && kubectl --kubeconfig="cibc13-kubeconfig.yaml" get nodes
```
