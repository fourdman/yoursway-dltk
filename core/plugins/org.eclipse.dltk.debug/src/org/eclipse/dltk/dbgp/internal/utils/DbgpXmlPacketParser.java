package org.eclipse.dltk.dbgp.internal.utils;

import org.eclipse.dltk.dbgp.internal.packets.DbgpNotifyPacket;
import org.eclipse.dltk.dbgp.internal.packets.DbgpResponsePacket;
import org.eclipse.dltk.dbgp.internal.packets.DbgpStreamPacket;
import org.w3c.dom.Element;

public class DbgpXmlPacketParser extends DbgpXmlParser {
	protected DbgpXmlPacketParser() {

	}

	public static DbgpResponsePacket parseResponsePacket(Element element) {
		final String ATTR_TRANSACTION_ID = "transaction_id";

		int id = Integer.parseInt(element.getAttribute(ATTR_TRANSACTION_ID));
		return new DbgpResponsePacket(element, id);
	}

	public static DbgpNotifyPacket parseNotifyPacket(Element element) {
		final String ATTR_NAME = "name";

		String name = element.getAttribute(ATTR_NAME);
		return new DbgpNotifyPacket(element, name);
	}

	public static DbgpStreamPacket parseStreamPacket(Element element) {
		final String ATTR_TYPE = "type";

		String type = element.getAttribute(ATTR_TYPE);
		String content = DbgpXmlParser.parseBase64Content(element);
		return new DbgpStreamPacket(type, content);
	}
}
