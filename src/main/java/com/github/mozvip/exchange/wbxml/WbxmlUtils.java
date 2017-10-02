package com.github.mozvip.exchange.wbxml;

import org.kxml2.io.KXmlParser;
import org.kxml2.io.KXmlSerializer;
import org.kxml2.kdom.Document;
import org.kxml2.wap.WbxmlParser;
import org.kxml2.wap.WbxmlSerializer;
import org.xmlpull.v1.XmlPullParserException;

import java.io.*;

public class WbxmlUtils {
	
	public static Document wbxmlToXml(byte[] originalContent) throws XmlPullParserException, IOException {
		return WbxmlUtils.parseWbxml(new ByteArrayInputStream(originalContent), "UTF-8");
	}
	

	public static byte[] serializeToWbxml(Document dom, String encoding) throws IOException {
		WbxmlSerializer wbxmlSerializer = new WbxmlSerializer(false);
		for (int i = 0; i < WbxmlEASPages.pages.length; i++) {
			wbxmlSerializer.setTagTable(i, WbxmlEASPages.pages[i]);
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		wbxmlSerializer.setOutput(out, encoding);
		dom.write(wbxmlSerializer);
		wbxmlSerializer.flush();
		return out.toByteArray();
	}

	public static Document parseXml(String xmlString) throws XmlPullParserException, IOException {
		KXmlParser parser = new KXmlParser();
		parser.setInput(new StringReader(xmlString));
		Document dom = new Document();
		dom.parse(parser);
		return dom;
	}

	public static Document parseWbxml(InputStream wbxmlInput, String charset)
			throws XmlPullParserException, IOException {
		WbxmlParser parser = new WbxmlParser();
		for (int i = 0; i < WbxmlEASPages.pages.length; i++) {
			parser.setTagTable(i, WbxmlEASPages.pages[i]);
		}
		parser.setInput(wbxmlInput, charset);
		Document dom = new Document();
		dom.parse(parser);
		return dom;
	}

	public static String serializeToXml(Document dom) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		KXmlSerializer ser = new KXmlSerializer();
		ser.setOutput(out, null);
		dom.write(ser);
		ser.flush();

		byte[] content = out.toByteArray();
		return new String(content);
	}

}
