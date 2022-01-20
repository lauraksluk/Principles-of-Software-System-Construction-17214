package edu.cmu.cs214.hw3.gui;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import edu.cmu.cs214.hw3.core.GameSystem;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.util.Map;

public class App extends NanoHTTPD {
    private GameSystem game;
    private Template template;

    public App() throws IOException {
        super(8080);

        this.game = new GameSystem();
        Handlebars handlebars = new Handlebars();
        this.template = handlebars.compile("main");

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");
    }

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            String uri = session.getUri();

            // Extract the view-specific data from the game and apply it to the template.

            GameState gameplay = GameState.forGame(this.game);
            String HTML = this.template.apply(gameplay);
            return newFixedLengthResponse(HTML);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
