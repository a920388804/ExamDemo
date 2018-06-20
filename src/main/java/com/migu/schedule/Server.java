package com.migu.schedule;

import java.util.ArrayList;
import java.util.List;

public class Server {
	public int nodeId;
	
	public List<Task> tasks = new ArrayList<Task>();

	public Server(int nodeId) {
		super();
		this.nodeId = nodeId;
	} 

}
