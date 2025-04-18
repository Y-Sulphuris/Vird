package com.ydo4ki.vird.lang;


import com.ydo4ki.vird.base.Type;
import com.ydo4ki.vird.base.Val;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode
@RequiredArgsConstructor
@Getter
public final class Blob implements Val {
	private final byte[] data;
	
	public static Blob ofInt(int value) {
		return new Blob(new byte[]{
				(byte) (value >>> 24),
				(byte) (value >>> 16),
				(byte) (value >>> 8),
				(byte) value});
	}
	
	@Override
	public String toString() {
		return "b" + bytesToHex(data);
	}
	
	@Override
	public Type getRawType() {
		return BlobType.of(data.length);
	}
	
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	private static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}
	
	public int toInt() {
		return data[0] << 24 | (data[1] & 0xFF) << 16 | (data[2] & 0xFF) << 8 | (data[3] & 0xFF);
	}
}
