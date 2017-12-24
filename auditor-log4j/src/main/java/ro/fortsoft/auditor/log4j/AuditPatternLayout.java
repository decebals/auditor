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

import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;

/**
 * A log4j pattern layout implementation capable of capturing audit event details.
 *
 * Available patterns:
 * [%S] - session
 * [%U] - username
 * [%H] - host name/address
 *
 * How to use these patterns:
 * Configure the log4j.properties file using above patterns.
 * eg: for the audit appender,
 * log4j.appender.file.layout=ro.fortsoft.audit.log4j.AuditPatternLayout
 * log4j.appender.file.layout.ConversionPattern=[%d{MM/dd/yyyy HH:mm:ss}] %-5p %U %S %H %m%n
 *
 * @author Decebal Suiu
 */
public class AuditPatternLayout extends PatternLayout {

    @Override
    protected PatternParser createPatternParser(String pattern) {
        return new AuditPatternParser(pattern);
    }

    private static class AuditPatternParser extends PatternParser {

        private static final char USERNAME = 'U';
        private static final char SESSION = 'S';
        private static final char IP = 'H';

        private AuditPatternParser(String pattern) {
            super(pattern);
        }

        @Override
        protected void finalizeConverter(char c) {
            PatternConverter converter = null;
            switch (c) {
                case USERNAME:
                    converter = new PropertyPatternConverter(formattingInfo, "username");
                    break;
                case SESSION:
                    converter = new PropertyPatternConverter(formattingInfo, "session");
                    break;
                case IP:
                    converter = new PropertyPatternConverter(formattingInfo, "ip");
                    break;
                default:
                    super.finalizeConverter(c);
            }

            if (converter != null) {
                currentLiteral.setLength(0);
                addConverter(converter);
            }
        }

    }

    private static class PropertyPatternConverter extends PatternConverter {

        private String propertyName;

        private PropertyPatternConverter(FormattingInfo formattingInfo, String propertyName) {
            super(formattingInfo);

            this.propertyName = propertyName;
        }

        protected String convert(LoggingEvent loggingEvent) {
            return loggingEvent.getProperty(propertyName);
        }

    }

}
