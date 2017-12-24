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

import org.apache.log4j.MDC;
import org.junit.Before;
import org.junit.Test;
import ro.fortsoft.auditor.AuditEvent;
import ro.fortsoft.auditor.Auditor;

import java.util.UUID;

/**
 * @author Decebal Suiu
 */
public class Log4jAuditorTest {

    private Auditor auditor;

    @Before
    public void setUp() {
        auditor = new Log4jAuditor("audit");
    }

    @Test
    public void logEvent() {
        auditor.audit(new AuditEvent("Login").setUsername("decebal1").setSession(getUUID()).setIp("localhost"));
    }

    @Test
    public void logEventMDC() {
        MDC.put("username", "decebal2");
        MDC.put("session", getUUID());
        MDC.put("ip", "127.0.0.1");
        auditor.audit(new AuditEvent("Login"));
    }

    private static String getUUID() {
        return UUID.randomUUID().toString();
    }

}
