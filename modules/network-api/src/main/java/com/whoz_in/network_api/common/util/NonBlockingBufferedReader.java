package com.whoz_in.network_api.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class NonBlockingBufferedReader extends BufferedReader {
    private final StringBuilder buffer = new StringBuilder();

    public NonBlockingBufferedReader(Reader reader) {
        super(reader);
    }

    @Override
    public String readLine() throws IOException {
        while (true) {
            int i = buffer.indexOf(System.lineSeparator());
            if (i != -1) {
                String extracted = buffer.substring(0, i);
                buffer.delete(0, i + 1);
                return extracted;
            }

            if (!ready()) return null;

            char[] charsBuffer = new char[1024];
            int readChars = super.read(charsBuffer, 0, charsBuffer.length);
            if (readChars == -1) return null;
            buffer.append(charsBuffer, 0, readChars);
        }
    }

    // TODO: lines() 오버라이딩
}
