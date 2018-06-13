# Distributed FTP Architecture
A Java based distributed client-server model that mimics the FTP protocol. Server may be a single server or a cluster of servers and files present on them can be downloaded by the client by just accessing the master server.

Using the application: 
--DOWNLOAD
 1. Run the server.class on a master node.
 2. Run the RMI-binded files on required slave nodes of the master server. Note that the IP address of master server has to be hard coded into slave nodes.
 3. Run the client.class on client node and you will recieve directory wise structured file details of all files present on slave node file systems.
 4. Enter name of file to be downloaded and the file will be downloaded in the root directory of the client.
 
 --UPLOAD
  1. Configure the cluster as above.
  2. Run client and set path to directory where file to be uploaded is present.
  3. Enter -push in command prompt and it will upload the file to node server where the size of file can be best fit.
  
  NOTE: Use 'backup' to test connection between client and master node. Use 'backup_2' to test connection between slave and master server nodes.
