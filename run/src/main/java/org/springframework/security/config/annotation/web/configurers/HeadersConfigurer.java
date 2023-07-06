package org.springframework.security.config.annotation.web.configurers;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.header.writers.CacheControlHeadersWriter;
import org.springframework.security.web.header.writers.ContentSecurityPolicyHeaderWriter;
import org.springframework.security.web.header.writers.FeaturePolicyHeaderWriter;
import org.springframework.security.web.header.writers.HpkpHeaderWriter;
import org.springframework.security.web.header.writers.HstsHeaderWriter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.XContentTypeOptionsHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class HeadersConfigurer<H extends HttpSecurityBuilder<H>> extends AbstractHttpConfigurer<HeadersConfigurer<H>, H> {
    private List<HeaderWriter> headerWriters = new ArrayList();
    private final ContentTypeOptionsConfig contentTypeOptions = new ContentTypeOptionsConfig();
    private final XXssConfig xssProtection = new XXssConfig();
    private final CacheControlConfig cacheControl = new CacheControlConfig();
    private final HstsConfig hsts = new HstsConfig();
    private final FrameOptionsConfig frameOptions = new FrameOptionsConfig();
    private final HpkpConfig hpkp = new HpkpConfig();
    private final ContentSecurityPolicyConfig contentSecurityPolicy = new ContentSecurityPolicyConfig();
    private final ReferrerPolicyConfig referrerPolicy = new ReferrerPolicyConfig();
    private final FeaturePolicyConfig featurePolicy = new FeaturePolicyConfig();

    public HeadersConfigurer() {
    }

    public HeadersConfigurer<H> addHeaderWriter(HeaderWriter headerWriter) {
        Assert.notNull(headerWriter, "headerWriter cannot be null");
        this.headerWriters.add(headerWriter);
        return this;
    }

    public ContentTypeOptionsConfig contentTypeOptions() {
        return this.contentTypeOptions.enable();
    }

    public XXssConfig xssProtection() {
        return this.xssProtection.enable();
    }

    public CacheControlConfig cacheControl() {
        return this.cacheControl.enable();
    }

    public HstsConfig httpStrictTransportSecurity() {
        return this.hsts.enable();
    }

    public FrameOptionsConfig frameOptions() {
        return this.frameOptions.enable();
    }

    public HpkpConfig httpPublicKeyPinning() {
        return this.hpkp.enable();
    }

    public ContentSecurityPolicyConfig contentSecurityPolicy(String policyDirectives) {
        this.contentSecurityPolicy.writer = new ContentSecurityPolicyHeaderWriter(policyDirectives);
        return this.contentSecurityPolicy;
    }

    public HeadersConfigurer<H> defaultsDisabled() {
        this.contentTypeOptions.disable();
        this.xssProtection.disable();
        this.cacheControl.disable();
        this.hsts.disable();
        this.frameOptions.disable();
        return this;
    }

    @Override
    public void configure(H http) throws Exception {
        HeaderWriterFilter headersFilter = this.createHeaderWriterFilter();
        http.addFilter(headersFilter);
    }

    private HeaderWriterFilter createHeaderWriterFilter() {
        List<HeaderWriter> writers = this.getHeaderWriters();
        if (writers.isEmpty()) {
            throw new IllegalStateException("Headers security is enabled, but no headers will be added. Either add headers or disable headers security");
        } else {
            HeaderWriterFilter headersFilter = new HeaderWriterFilter(writers);
            headersFilter = (HeaderWriterFilter)this.postProcess(headersFilter);
            return headersFilter;
        }
    }

    private List<HeaderWriter> getHeaderWriters() {
        List<HeaderWriter> writers = new ArrayList();
        this.addIfNotNull(writers, this.contentTypeOptions.writer);
        this.addIfNotNull(writers, this.xssProtection.writer);
        this.addIfNotNull(writers, this.cacheControl.writer);
        this.addIfNotNull(writers, this.hsts.writer);
        this.addIfNotNull(writers, this.frameOptions.writer);
        this.addIfNotNull(writers, this.hpkp.writer);
        this.addIfNotNull(writers, this.contentSecurityPolicy.writer);
        this.addIfNotNull(writers, this.referrerPolicy.writer);
        this.addIfNotNull(writers, this.featurePolicy.writer);
        writers.addAll(this.headerWriters);
        return writers;
    }

    private <T> void addIfNotNull(List<T> values, T value) {
        if (value != null) {
            values.add(value);
        }

    }

    public ReferrerPolicyConfig referrerPolicy() {
        this.referrerPolicy.writer = new ReferrerPolicyHeaderWriter();
        return this.referrerPolicy;
    }

    public ReferrerPolicyConfig referrerPolicy(ReferrerPolicy policy) {
        this.referrerPolicy.writer = new ReferrerPolicyHeaderWriter(policy);
        return this.referrerPolicy;
    }

    public FeaturePolicyConfig featurePolicy(String policyDirectives) {
        this.featurePolicy.writer = new FeaturePolicyHeaderWriter(policyDirectives);
        return this.featurePolicy;
    }

    public final class FeaturePolicyConfig {
        private FeaturePolicyHeaderWriter writer;

        private FeaturePolicyConfig() {
        }

        public HeadersConfigurer<H> and() {
            return HeadersConfigurer.this;
        }
    }

    public final class ReferrerPolicyConfig {
        private ReferrerPolicyHeaderWriter writer;

        private ReferrerPolicyConfig() {
        }

        public HeadersConfigurer<H> and() {
            return HeadersConfigurer.this;
        }
    }

    public final class ContentSecurityPolicyConfig {
        private ContentSecurityPolicyHeaderWriter writer;

        private ContentSecurityPolicyConfig() {
        }

        public ContentSecurityPolicyConfig reportOnly() {
            this.writer.setReportOnly(true);
            return this;
        }

        public HeadersConfigurer<H> and() {
            return HeadersConfigurer.this;
        }
    }

    public final class HpkpConfig {
        private HpkpHeaderWriter writer;

        private HpkpConfig() {
        }

        public HpkpConfig withPins(Map<String, String> pins) {
            this.writer.setPins(pins);
            return this;
        }

        public HpkpConfig addSha256Pins(String... pins) {
            this.writer.addSha256Pins(pins);
            return this;
        }

        public HpkpConfig maxAgeInSeconds(long maxAgeInSeconds) {
            this.writer.setMaxAgeInSeconds(maxAgeInSeconds);
            return this;
        }

        public HpkpConfig includeSubDomains(boolean includeSubDomains) {
            this.writer.setIncludeSubDomains(includeSubDomains);
            return this;
        }

        public HpkpConfig reportOnly(boolean reportOnly) {
            this.writer.setReportOnly(reportOnly);
            return this;
        }

        public HpkpConfig reportUri(URI reportUri) {
            this.writer.setReportUri(reportUri);
            return this;
        }

        public HpkpConfig reportUri(String reportUri) {
            this.writer.setReportUri(reportUri);
            return this;
        }

        public HeadersConfigurer<H> disable() {
            this.writer = null;
            return this.and();
        }

        public HeadersConfigurer<H> and() {
            return HeadersConfigurer.this;
        }

        private HpkpConfig enable() {
            if (this.writer == null) {
                this.writer = new HpkpHeaderWriter();
            }

            return this;
        }
    }

    public final class FrameOptionsConfig {
        private XFrameOptionsHeaderWriter writer;

        private FrameOptionsConfig() {
            this.enable();
        }

        public HeadersConfigurer<H> deny() {
            this.writer = new XFrameOptionsHeaderWriter(XFrameOptionsMode.DENY);
            return this.and();
        }

        public HeadersConfigurer<H> sameOrigin() {
            this.writer = new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN);
            return this.and();
        }

        public HeadersConfigurer<H> disable() {
            this.writer = null;
            return this.and();
        }

        public HeadersConfigurer<H> and() {
            return HeadersConfigurer.this;
        }

        private FrameOptionsConfig enable() {
            if (this.writer == null) {
                this.writer = new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN);
            }

            return this;
        }
    }

    public final class HstsConfig {
        private HstsHeaderWriter writer;

        private HstsConfig() {
            this.enable();
        }

        public HstsConfig maxAgeInSeconds(long maxAgeInSeconds) {
            this.writer.setMaxAgeInSeconds(maxAgeInSeconds);
            return this;
        }

        public HstsConfig requestMatcher(RequestMatcher requestMatcher) {
            this.writer.setRequestMatcher(requestMatcher);
            return this;
        }

        public HstsConfig includeSubDomains(boolean includeSubDomains) {
            this.writer.setIncludeSubDomains(includeSubDomains);
            return this;
        }

        public HeadersConfigurer<H> disable() {
            this.writer = null;
            return HeadersConfigurer.this;
        }

        public HeadersConfigurer<H> and() {
            return HeadersConfigurer.this;
        }

        private HstsConfig enable() {
            if (this.writer == null) {
                this.writer = new HstsHeaderWriter();
            }

            return this;
        }
    }

    public final class CacheControlConfig {
        private CacheControlHeadersWriter writer;

        private CacheControlConfig() {
            this.enable();
        }

        public HeadersConfigurer<H> disable() {
            this.writer = null;
            return HeadersConfigurer.this;
        }

        public HeadersConfigurer<H> and() {
            return HeadersConfigurer.this;
        }

        private CacheControlConfig enable() {
            if (this.writer == null) {
                this.writer = new CacheControlHeadersWriter();
            }

            return this;
        }
    }

    public final class XXssConfig {
        private XXssProtectionHeaderWriter writer;

        private XXssConfig() {
            this.enable();
        }

        public XXssConfig block(boolean enabled) {
            this.writer.setBlock(enabled);
            return this;
        }

        public XXssConfig xssProtectionEnabled(boolean enabled) {
            this.writer.setEnabled(enabled);
            return this;
        }

        public HeadersConfigurer<H> disable() {
            this.writer = null;
            return this.and();
        }

        public HeadersConfigurer<H> and() {
            return HeadersConfigurer.this;
        }

        private XXssConfig enable() {
            if (this.writer == null) {
                this.writer = new XXssProtectionHeaderWriter();
            }

            return this;
        }
    }

    public final class ContentTypeOptionsConfig {
        private XContentTypeOptionsHeaderWriter writer;

        private ContentTypeOptionsConfig() {
            this.enable();
        }

        public HeadersConfigurer<H> disable() {
            this.writer = null;
            return this.and();
        }

        public HeadersConfigurer<H> and() {
            return HeadersConfigurer.this;
        }

        private ContentTypeOptionsConfig enable() {
            if (this.writer == null) {
                this.writer = new XContentTypeOptionsHeaderWriter();
            }

            return this;
        }
    }
}
