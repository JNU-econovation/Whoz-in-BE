package com.whoz_in.network_api.common.util;

import java.io.BufferedReader;
import java.io.IOException;

public class NonBlockingBufferedReader {
    private final BufferedReader br;
    private final StringBuilder buffer = new StringBuilder();

    public NonBlockingBufferedReader(BufferedReader br) {
        this.br = br;
    }

    /*
    한 줄 단위의 데이터를 가져오며, 데이터가 없을경우 BufferedReader의 readLine과 다르게 블로킹되지 않고 null을 반환한다.
    처리할 데이터는 이 메서드가 실행되는 운영체제와 같은 운영체제에서 만들어진 데이터여야 한다.
     */
    public String readLine() throws IOException {
        while(true){
            int i = buffer.indexOf(System.lineSeparator());
            if (i != -1){
                String extracted = buffer.substring(0, i);
                buffer.delete(0, i+1);
                return extracted;
            }

            if (!br.ready()) return null;

            int len = 1024;
            char[] charsBuffer = new char[len];
            int readChars = br.read(charsBuffer, 0, len);
            if (readChars == -1) return null;
            buffer.append(charsBuffer, 0, readChars);
        }
    }
}