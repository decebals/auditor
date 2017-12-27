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

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * It's a very simple implementation of {@link AuditEventFormatter}.
 * If it's not specified a formatter, then in all cases this formatter is used (it's the default).
 *
 * @author Decebal Suiu
 */
public class SimpleAuditEventFormatter implements AuditEventFormatter {

    private static SimpleAuditEventFormatter singleton = new SimpleAuditEventFormatter();

    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    public static SimpleAuditEventFormatter get() {
        return singleton;
    }

    /**
     * Set the date format. The default pattern is "MM/dd/yyyy HH:mm:ss"
     *
     * @param dateFormat
     */
    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * Format an audit event.
     * The result will be:
     * {@code
     * [date] username? session? ip? - action context? - errorMessage?
     * }
     * where {@code ?} means optional, if it's null or empty the field is ignored.
     *
     * @param event
     * @return
     */
    @Override
    public String formatEvent(AuditEvent event) {
        StringBuilder sb = new StringBuilder();

        sb.append('[');
        sb.append(dateFormat.format(event.getDate()));
        sb.append("] ");

        String username = event.getUsername();
        if (username != null && !username.isEmpty()) {
            sb.append(username);
            sb.append(' ');
        }

        String session = event.getSession();
        if (session != null && !session.isEmpty()) {
            sb.append(session);
            sb.append(' ');
        }

        String ip = event.getIp();
        if (ip != null && !ip.isEmpty()) {
            sb.append(ip);
            sb.append(' ');
        }

        sb.append("- ");
        sb.append(event.getAction());

        sb.append(formatContext(event));
        String errorMessage = event.getErrorMessage();
        if (errorMessage != null && !errorMessage.isEmpty()) {
            sb.append(" - ");
            sb.append(errorMessage);
        }

        return sb.toString();
    }

    protected String formatContext(AuditEvent event) {
        if (event.getContext().isEmpty()) {
            return "";
        }

        return " " + event.getContext().toString();
    }

}
