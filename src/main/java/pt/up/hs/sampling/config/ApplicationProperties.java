package pt.up.hs.sampling.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Sampling.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Preview preview = new Preview();
    private final Importing importing = new Importing();

    public Preview getPreview() {
        return preview;
    }

    public Importing getImporting() {
        return importing;
    }

    public static class Preview {
        private boolean cleanOnStartup = false;
        private String cron = "0 0 * * * *";
        private String path = "previews/";

        public boolean isCleanOnStartup() {
            return cleanOnStartup;
        }

        public void setCleanOnStartup(boolean cleanOnStartup) {
            this.cleanOnStartup = cleanOnStartup;
        }

        public String getPath() {
            return path;
        }

        public Preview setPath(String path) {
            this.path = path;
            return this;
        }

        public String getCron() {
            return cron;
        }

        public Preview setCron(String cron) {
            this.cron = cron;
            return this;
        }
    }

    public static class Importing {
        private String path = "importing/";

        public String getPath() {
            return path;
        }

        public Importing setPath(String path) {
            this.path = path;
            return this;
        }
    }
}
