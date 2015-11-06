package com.tescomm.demo.zookeeper.watcher;

import java.net.InetAddress;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
/**
 * zookeeper的监听节点方法及获取节点信息的方法
 * @author ymz
 *
 */
public class WatchEvent {
	private static Logger logger = Logger.getLogger(WatchEvent.class);
	static String path="/ymz";
	static CuratorFramework client= CuratorFrameworkFactory.builder()
			.connectString("10.95.3.136:2181,10.95.3.139:2181")
			.retryPolicy(new ExponentialBackoffRetry(1000, 3))
			.sessionTimeoutMs(5000)
			.build();
	static {
		client.start();
	}
	/**
	 * 监听节点是否移除，移除则注册，内容为本服务器ip
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public void watcher()throws Exception{
//		client.start();
		PathChildrenCache cache=new PathChildrenCache(client, path, true);
		cache.start(StartMode.POST_INITIALIZED_EVENT);
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			
//			@Override
			public void childEvent(CuratorFramework client,
					PathChildrenCacheEvent event) throws Exception {
				switch(event.getType()){
					case CHILD_REMOVED:
						logger.info("CHILD_REMOVED:"+event.getData());
						client.create().withMode(CreateMode.EPHEMERAL).forPath(path+"/server",InetAddress.getLocalHost().getHostAddress().getBytes());
						break;
					default:
						break;
				}
				//判断节点是否存在，不存在就注册，节点信息为本服务器的ip
			if(client.checkExists().forPath(path+"/server")==null){
				logger.info("/ymz/server节点不存在");
				client.create().withMode(CreateMode.EPHEMERAL).forPath(path+"/server",InetAddress.getLocalHost().getHostAddress().getBytes());
			}	
			}
		});
//		Thread.sleep(Integer.MAX_VALUE);
	}
	/**
	 * 获取zookeeper节点信息的方法
	 * @return ip
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public String getDataIp() throws Exception{
		//判断是否连上zookeeper服务端
		if(!client.isStarted()){
			client.start();
		}		
		String Ip=null;	
		//判断节点是否存在，存在获取节点信息
		if(client.checkExists().forPath(path+"/server")!=null){
			Ip=new String(client.getData().storingStatIn(new Stat()).forPath(path+"/server"));
		}
		
//		client.close();
		logger.info("服务端的IP："+Ip);
		return Ip;
	}
	public static void main(String[] args) throws Exception {
		WatchEvent we=new WatchEvent();
		we.getDataIp();
		
	}
}
