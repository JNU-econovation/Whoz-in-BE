package com.whoz_in.network_api.common.process;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractProcess {
    protected final ProcessCommand command;
    protected final Process process;

    protected void enterSudoPassword() throws IOException {
        String sudoPassword = System.getProperty("sudo-password");
        Writer writer = new OutputStreamWriter(this.process.getOutputStream());
        writer.write(sudoPassword + System.lineSeparator());
        writer.flush();
    }
}
