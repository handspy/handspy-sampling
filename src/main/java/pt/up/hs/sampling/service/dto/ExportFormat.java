package pt.up.hs.sampling.service.dto;

public enum ExportFormat {
    XML("xml", "application/xml"),
    JSON("json", "application/json"),
    PNG("png", "image/png"),
    SVG("svg", "image/svg+xml");

    private final String extension;
    private final String mimeType;

    ExportFormat(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public String getMimeType() {
        return mimeType;
    }
}
