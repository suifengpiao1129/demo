package com.tescomm.demo.zookeeper.thrift;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.tescomm.demo.zookeeper.watcher.WatchEvent;


/**
 * thrift客户端，先通过zookeeper的API获取节点的信息（ip地址）传递给客户端
 * @author ymz
 *
 */
public class ThriftClient {
	private static Logger logger = Logger.getLogger(ThriftClient.class);
	/**
	 * thrift客户端
	 * @param ip
	 * @param para 传输内容
	 */
	public void startClient(String ip,String para){
		TTransport transport;
		try {
		
			transport = new TSocket(ip, 55909);
            TProtocol protocol = new TBinaryProtocol(transport);
            HelloThrift.Client client = new HelloThrift.Client(protocol);
            transport.open();
            String result;
			result = client.Hello2(para);
			logger.info(result);
            transport.close();
		} catch (TTransportException e) {
			
			e.printStackTrace();
		}catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		ThriftClient tcl= new ThriftClient();
		int i=0;
		try {
			for(i=0;i<500;i++){
				WatchEvent we=new WatchEvent();
				String Ip=we.getDataIp();
				tcl.startClient(Ip, "hello"+i);
				
				Thread.sleep(5000);
			}
		} catch (Exception e) {
			i--;
		}

	}
}
