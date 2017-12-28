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

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Async File based {@link Auditor}.
 * Events are printed to file.
 * The conversion to {@link String} is made using a {@link AuditEventFormatter}.
 * If a formatter is not specified then {@link SimpleAuditEventFormatter} is used.
 * The class is thread safe.
 *
 * Internally this class uses a {@link BlockingQueue} to store the audit events.
 * In {@link #audit(AuditEvent)} method, the audit event is added to the queue.
 * A thread takes the audit events from the queue and writes them to the file.
 *
 * @author Decebal Suiu
 */
public class AsyncFileAuditor extends FileAuditor implements Runnable {

    private final BlockingQueue<AuditEvent> queue;

    public AsyncFileAuditor(File file) throws IOException {
        this(file, SimpleAuditEventFormatter.get());
    }

    public AsyncFileAuditor(File file, AuditEventFormatter formatter) throws IOException {
        super(file, formatter);

        queue = new ArrayBlockingQueue<>(1024);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(this);
    }

    /**
     * Write the audit event to the queue.
     *
     * @param event
     */
    @Override
    public void audit(AuditEvent event) {
        try {
            queue.put(event);
        } catch (InterruptedException e) {
            e.printStackTrace(); // ?!
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                writer.println(formatter.formatEvent(queue.take()));
            } catch (InterruptedException e) {
                e.printStackTrace(); // ?!
            }
        }
    }

}
