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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This interface provides information about an auditing event.
 *
 * @author Decebal Suiu
 */
public class AuditEvent {

    /*
     * Audit levels of severity.
     */
    public static final int INFO = 0;
    public static final int ERROR = 1;

    private int level;
    private String username;
    private Date date;
    private String action;
    private String session;
    private String ip;
    private Map<String, Object> context;
    private String errorMessage;

    public AuditEvent(String action) {
        this(action, INFO);
    }

    public AuditEvent(String action, int level) {
        this.action = action;
        this.level = level;

        date = new Date();
        context = new HashMap<String, Object>();
    }

    public int getLevel() {
        return level;
    }

    public AuditEvent setLevel(int level) {
        this.level = level;

        return this;
    }

    public String getUsername() {
        return username;
    }

    public AuditEvent setUsername(String username) {
        this.username = username;

        return this;
    }

    public Date getDate() {
        return date;
    }

    public AuditEvent setDate(Date date) {
        this.date = date;

        return this;
    }

    public String getAction() {
        return action;
    }

    public AuditEvent setAction(String action) {
        this.action = action;

        return this;
    }

    public String getSession() {
        return session;
    }

    public AuditEvent setSession(String session) {
        this.session = session;

        return this;
    }

    public String getIp() {
        return ip;
    }

    public AuditEvent setIp(String ip) {
        this.ip = ip;

        return this;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public AuditEvent setContext(Map<String, Object> context) {
        this.context = context;

        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public AuditEvent setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        level = ERROR;

        return this;
    }

    @Override
    public String toString() {
        return SimpleAuditEventFormatter.get().formatEvent(this);
    }

}