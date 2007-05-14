/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.dbgp.internal.packets;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.dltk.dbgp.internal.DbgpRawPacket;
import org.eclipse.dltk.dbgp.internal.DbgpWorkingThread;
import org.eclipse.dltk.dbgp.internal.utils.DbgpXmlPacketParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DbgpPacketReceiver extends DbgpWorkingThread {
	private static class SyncMap {
		private HashMap map;

		public SyncMap() {
			map = new HashMap();
		}

		public synchronized void put(Object key, Object value) {
			map.put(key, value);
			notifyAll();
		}

		public synchronized Object get(Object key) throws InterruptedException {
			while (!map.containsKey(key)) {
				wait();
			}
			
			return map.remove(key);			
		}
		
		public synchronized int size() {
			return map.size();
		}
	}

	private static class SyncQueue {
		private LinkedList queue;

		public SyncQueue() {
			this.queue = new LinkedList();
		}

		public synchronized void put(Object obj) {
			queue.addLast(obj);
			notifyAll();
		}

		public synchronized Object get() throws InterruptedException {
			while (queue.isEmpty()) {
				wait();
			}

			return queue.removeFirst();
		}
	}

	private static final String INIT_TAG = "init";

	private static final String RESPONSE_TAG = "response";

	private static final String STREAM_TAG = "stream";

	private static final String NOTIFY_TAG = "notify";

	private SyncMap responseQueue;

	private SyncQueue notifyQueue;

	private SyncQueue streamQueue;

	private InputStream input;

	private IDbgpLogger logger;

	protected void workingCycle() throws Exception {
		
		while (!Thread.interrupted()) {
			DbgpRawPacket packet = DbgpRawPacket.readPacket(input);

			if (logger != null) {
				logger.logInput(packet.toString());
			}

			addDocument(packet.getParsedXml());
		}
		
	}

	protected void addDocument(Document doc) {
		Element element = (Element) doc.getFirstChild();
		String tag = element.getTagName();

		// TODO: correct init tag handling without this hack
		if (tag.equals(INIT_TAG)) {
			responseQueue.put(new Integer(-1), new DbgpResponsePacket(element,
					-1));
		} else if (tag.equals(RESPONSE_TAG)) {
			DbgpResponsePacket packet = DbgpXmlPacketParser
					.parseResponsePacket(element);
			responseQueue.put(new Integer(packet.getTransactionId()), packet);
		} else if (tag.equals(STREAM_TAG)) {
			streamQueue.put(DbgpXmlPacketParser.parseStreamPacket(element));
		} else if (tag.equals(NOTIFY_TAG)) {
			notifyQueue.put(DbgpXmlPacketParser.parseNotifyPacket(element));
		}
	}

	public DbgpNotifyPacket getNotifyPacket() throws InterruptedException {
		return (DbgpNotifyPacket) notifyQueue.get();
	}

	public DbgpStreamPacket getStreamPacket() throws InterruptedException {
		return (DbgpStreamPacket) streamQueue.get();
	}

	public DbgpResponsePacket getResponsePacket(int transactionId)
			throws InterruptedException {
				
		return (DbgpResponsePacket) responseQueue
				.get(new Integer(transactionId));
	}

	public DbgpPacketReceiver(InputStream input) {
		super("DBGP - Packet receiver");
		
		if (input == null) {
			throw new IllegalArgumentException();
		}

		this.input = input;
		this.notifyQueue = new SyncQueue();
		this.streamQueue = new SyncQueue();
		this.responseQueue = new SyncMap();
	}

	public void setLogger(IDbgpLogger logger) {
		this.logger = logger;
	}
}
