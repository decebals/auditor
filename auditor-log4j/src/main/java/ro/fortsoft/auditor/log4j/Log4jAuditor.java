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
package ro.fortsoft.auditor.log4j;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.spi.LoggingEvent;
import ro.fortsoft.auditor.AuditEvent;
import ro.fortsoft.auditor.Auditor;

import java.util.Enumeration;

/**
 * Log4J based {@link Auditor}.
 * The {@link AuditEvent}s are converted to {@link LoggingEvent}s and added to {@link Appender}s.
 * Information like {@code username}, {@code session}, {@code ip} are added as properties in LoggingEvent.
 * Before send the event to appenders, some eventual missing property (username, session, ip) are extracted
 * from {@link MDC} if they exist.
 * In your {@code log4j.properties} you can use {@link AuditPatternLayout} if you want to capturing
 * audit event details via patterns.
 *
 * @author Decebal Suiu
 */
public class Log4jAuditor implements Auditor {

    private final Logger log;

    public Log4jAuditor() {
        this(Log4jAuditor.class.getName());
    }

    public Log4jAuditor(String loggerName) {
        log = Logger.getLogger(loggerName);
    }

    @SuppressWarnings("unchecked")
    public void audit(AuditEvent event) {
        initFromMDC(event);

        // create the logging event from audit event
        LoggingEvent loggingEvent = new LoggingEvent(log.getName(), log, Level.DEBUG, event.getAction(), null);
        loggingEvent.setProperty("username", event.getUsername());
        loggingEvent.setProperty("session", event.getSession());
        loggingEvent.setProperty("ip", event.getIp());

        // add the logging event to all appenders
        Enumeration<Appender> en = log.getAllAppenders();
        while (en.hasMoreElements()) {
            en.nextElement().doAppend(loggingEvent);
        }
    }

    protected void initFromMDC(AuditEvent event) {
        // set some attributes in event object using MDC
        if (event.getUsername() == null) {
            event.setUsername((String) MDC.get("username"));
        }
        if (event.getSession() == null) {
            event.setSession((String) MDC.get("session"));
        }
        if (event.getIp() == null) {
            event.setIp((String) MDC.get("ip"));
        }
    }

}
