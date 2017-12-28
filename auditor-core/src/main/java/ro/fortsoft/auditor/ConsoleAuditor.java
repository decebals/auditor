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

/**
 * Console based {@link Auditor}.
 * Events are printed to console via {@link System#out#println}.
 * The conversion to {@link String} is made using a {@link AuditEventFormatter}.
 * If a formatter is not specified then {@link SimpleAuditEventFormatter} is used.
 * The class is thread safe.
 *
 * @author Decebal Suiu
 */
public class ConsoleAuditor implements Auditor {

    private final AuditEventFormatter formatter;

    public ConsoleAuditor() {
        this(SimpleAuditEventFormatter.get());
    }

    public ConsoleAuditor(AuditEventFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void audit(AuditEvent event) {
        System.out.println(formatter.formatEvent(event));
    }

}
