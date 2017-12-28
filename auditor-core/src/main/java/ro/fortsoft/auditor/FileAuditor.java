/*
 * Copyright 2017 Decebal Suiu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.fortsoft.auditor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * File based {@link Auditor}.
 * Events are printed to file.
 * The conversion to {@link String} is made using a {@link AuditEventFormatter}.
 * If a formatter is not specified then {@link SimpleAuditEventFormatter} is used.

 * @author Decebal Suiu
 */
public class FileAuditor implements Auditor {

    private final PrintWriter writer;
    private final AuditEventFormatter formatter;

    public FileAuditor(File file) throws IOException {
        this(file, SimpleAuditEventFormatter.get());
    }

    public FileAuditor(File file, AuditEventFormatter formatter) throws IOException {
        writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        this.formatter = formatter;
    }

    @Override
    public void audit(AuditEvent event) {
        writer.println(formatter.formatEvent(event));
    }

    /**
     * Call this method to close the {@link PrintWriter} used to print lines in file.
     * This method must be call on shutdown application.
     */
    public void close() {
        writer.close();
    }

}
