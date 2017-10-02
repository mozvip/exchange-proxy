package com.github.mozvip.exchange.core;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ASBinaryQueryString extends ActiveSyncQueryString {

	private final static Logger LOGGER = LoggerFactory.getLogger(ASBinaryQueryString.class);

	List<Parameter> parameters = new ArrayList<>();

	private class Parameter {

		int tag;
		String value;

		public Parameter(int tag, String value) {
			super();
			this.tag = tag;
			this.value = value;

			LOGGER.debug("{}={}", tag, value);
		}

		public int getTag() {
			return tag;
		}

		public String getValue() {
			return value;
		}

		public void setTag(int tag) {
			this.tag = tag;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

	private String readString(DataInputStream dis, int length) throws IOException {
		byte[] data = readBytes(dis, length);
		return (data != null ? new String(data) : null);
	}

	private byte[] readBytes(DataInputStream dis, int length) throws IOException {
		if (length == 0) {
			return null;
		}
		byte[] data = new byte[length];
		dis.readFully(data);
		return data;
	}

	private void decodeFromBinary(byte[] decodedBytes) throws IOException {

		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(decodedBytes));
		try {

			protocolVersion = dis.readUnsignedByte();
			command = ActiveSyncCommand.values()[ dis.readUnsignedByte() ];
			locale = dis.readUnsignedShort();

			int deviceIdLength = dis.readUnsignedByte();
			deviceId = new String( readBytes(dis, deviceIdLength) );

			int policyKeyLength = dis.readUnsignedByte();
			policyKey = readBytes(dis, policyKeyLength);

			int deviceTypeLength = dis.readUnsignedByte();
			deviceType = readString(dis, deviceTypeLength);

			try {
				while (true) {
					int tag = dis.readUnsignedByte();
					int length = dis.readUnsignedByte();
					String value = readString(dis, length);
					// add parameter
					parameters.add(new Parameter(tag, value));
				}
			} catch (EOFException e) {
			}

			LOGGER.info(
					"Original Query String : ProtocolVersion={} Command={} Locale={} DeviceId=0x{} PolicyKey=0x{} DeviceType={}",
					protocolVersion, command.getLabel(), locale, Hex.encodeHexString(deviceId.getBytes()), Hex.encodeHexString(policyKey),
					deviceType);
			
		} finally {
			dis.close();
		}


	}

	public ASBinaryQueryString(String queryString) throws IOException {

		byte[] decodedBytes = Base64.getDecoder().decode(queryString.getBytes());
		decodeFromBinary(decodedBytes);
		
	}

	@Override
	public String encode() throws IOException {
	
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			try (DataOutputStream dos = new DataOutputStream(out)) {
				dos.write(protocolVersion);
				dos.write(command.ordinal());
				dos.writeShort(locale);
				
				byte[] deviceIdBytes;
				try {
					deviceIdBytes = Hex.decodeHex(deviceId.toCharArray());
				} catch (DecoderException e) {
					throw new IOException( e );
				}
				dos.write(deviceIdBytes.length);
				dos.write(deviceIdBytes);

				dos.write(policyKey.length);
				dos.write(policyKey);
				dos.write(deviceType.length());
				dos.write(deviceType.getBytes());

				for (Parameter parameter : parameters) {
					dos.write(parameter.getTag());
					dos.write(parameter.getValue().length());
					dos.write(parameter.getValue().getBytes());
				}

			}

			return Base64.getEncoder().encodeToString(out.toByteArray());
		}
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}	

}
