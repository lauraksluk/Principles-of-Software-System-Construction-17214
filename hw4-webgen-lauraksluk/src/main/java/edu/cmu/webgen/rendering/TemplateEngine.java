package edu.cmu.webgen.rendering;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

import java.io.*;

/**
 * This class interacts with the template engine and creates HTML files
 * based on the templates in `/src/main/template/html` and the provided
 * objects from `Website`
 */
public class TemplateEngine {

    private final Handlebars handlebars;

    public TemplateEngine() {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/html");
        loader.setSuffix(".hbs");
        this.handlebars = new Handlebars(loader);
    }

    /**
     * render a template with a target object that holds the data used in the template
     * and write it into the targetFile
     *
     * @param templateName name of the template without the ".hbs" extension
     * @param root         object that provides the data used in the template as fields or getter methods
     * @param targetFile   file into which the result is written
     * @throws IOException if I/O problems occur
     */
    public void render(String templateName, Object root, File targetFile) throws IOException {
        targetFile.getParentFile().mkdirs();

        try (Writer out = new BufferedWriter(new FileWriter(targetFile))) {
            render(templateName, root, out);
        }
    }

    /**
     * render a template with a target object that holds the data used in the template
     * and write it into the provided writer.
     *
     * @param templateName name of the template without the ".hbs" extension
     * @param root         object that provides the data used in the template as fields or getter methods
     * @param writer       writer into which the result is written
     * @throws IOException if I/O problems occur
     */
    public void render(String templateName, Object root, Writer writer) throws IOException {
        Template template = handlebars.compile(templateName);
        template.apply(root, writer);
    }
}
