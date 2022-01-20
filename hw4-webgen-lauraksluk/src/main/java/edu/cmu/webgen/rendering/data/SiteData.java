package edu.cmu.webgen.rendering.data;

import java.util.List;

public record SiteData(String relPath, String projectTitle, String organization, List<SiteLink> headers,
                       String lastUpdated) {
}
