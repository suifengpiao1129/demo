package com.tescomm.demo.zookeeper.thrift;

import org.apache.log4j.Logger;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

import com.tescomm.demo.zookeeper.thrift.HelloThrift.Processor;
import com.tescomm.demo.zookeeper.watcher.WatchEvent;

/**
 * thrift服务端，启动前调用zookeeper监听，当监听到zookeeper节点移除时，注册节点，内容为本地ip
 * @author ymz
 *
 */
public class ThriftServer {
	private static Logger logger = Logger.getLogger(ThriftServer.class);
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void startServer(){
		try {
			WatchEvent we=new WatchEvent();
			we.watcher();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try{
			TServerSocket serverTransport = new TServerSocket(55909);
			HelloThrift.Processor process=new Processor(new HelloThriftImpl());
			Factory portFactory= new TBinaryProtocol.Factory(true, true);
			 Args args = new Args(serverTransport);
	         args.processor(process);
	         args.protocolFactory(portFactory);
	         TServer server = new TThreadPoolServer(args);
	         logger.info("port 55909 start!!!!");
	         server.serve();
		}catch(TTransportException e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		ThriftServer tfs=new ThriftServer();
		tfs.startServer();
	}
}
