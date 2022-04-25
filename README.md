# Load-Balancer
Load Balancing is a key concept to system design. One of the popular ways to balance load in a system is to use the concept of consistent hashing. Consistent Hashing allows requests to be mapped into hash buckets while allowing the system to add and remove nodes flexibly so as to maintain a good load factor on each machine.  
# Naive mod-n Hashing
The principle of mod-n hashing is the following. Each key is hashed using a hashing function to transform an input into an integer. Then, we perform a modulo based on the number of nodes. 
# Scalability problems with basic approach
What if we implement a datastore using sharding and based on the mod-n strategy to distribute data? If we scale the number of nodes, we need to perform a rebalancing operation.  
This sounds like a really heavy operation  
This is exactly the purpose of consistent hashing algorithms.  
# ring consistent hash  
Consistent Hashing maps servers to the key space and assigns requests(mapped to relevant buckets, called load) to the next clockwise server. Servers can then store relevant request data in them while allowing the system flexibility and scalability.   
So basically this is a hashing but instead of loads getting hashed to servers, We have hashed Servers to the assumed ring of numbered loads  
Now if a new server is added , we just hash it and add it to the ring 
# handling uneven distribution  
a situatuion like this can happen  
![1](https://imgur.com/zcccAz9.png)
# how to handle this ?  
we use the concept of VIRTUAL SERVERS which are basically the same servers being hashed to multiple positions on the load ring  
  
The situation with virtual servers looks like this  
![2](https://imgur.com/RMdlDkK.png)  
# want to read more ? link goes as :  
https://itnext.io/introducing-consistent-hashing-9a289769052e
