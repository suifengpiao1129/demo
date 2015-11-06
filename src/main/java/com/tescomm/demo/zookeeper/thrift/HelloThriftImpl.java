package com.tescomm.demo.zookeeper.thrift;

import org.apache.thrift.TException;

public class HelloThriftImpl implements HelloThrift.Iface{

	@Override
	public String Hello2(String para) throws TException {
		System.out.println(para);
		return para;
	}

}
