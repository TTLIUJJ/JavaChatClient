package com.cherry.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Map;

public class ParseUtil {
    public static String parseByteBuffer(ByteBuffer buffer){
        try{
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            CharBuffer charBuffer = decoder.decode(buffer);

            return charBuffer.toString();
        }catch (Exception e){
            e.getStackTrace();
        }
        return "";
    }

    public static void parseString(String str, ByteBuffer buffer){
        try{
            ByteBuffer t = ByteBuffer.wrap(str.getBytes());
            buffer.put(t);
        }catch (Exception e){
            e.getStackTrace();
        }
    }



}
