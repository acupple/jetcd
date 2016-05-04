# jetcd
simple etcd java client base okhttp

# usage
1. import dependency
```xml
<dependency>
    <groupId>org.mokey.acupple</groupId>
    <artifactId>jetcd</artifactId>
    <version>1.0.0.SNAPSHOT</version>
</dependency>
```
2. code
```java
Etcd client = new EtcdClient(new URI[]{URI.create("http://127.0.0.1:2379")});
// Create path
client.createDir("/root/abc");
// Set value
client.setIfNotExists("/root/abc", "this is a test message");
//register callback for change
client.waitRecursive(new Handler<EtcdResponse>(){
	@Override
		public void handle(EtcdResponse event) {
		//Your handler here
	}
}, "/root/abc");
```
