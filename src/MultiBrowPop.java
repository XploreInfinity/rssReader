import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MultiBrowPop {
//external code,slightly modified.Source: https://stackoverflow.com/a/18509384
    public MultiBrowPop(String url) {

        String myOS = System.getProperty("os.name").toLowerCase();

        try {
            if(Desktop.isDesktopSupported()) { // Probably Windows

                Desktop desktop = Desktop.getDesktop();
                desktop.browse(new URI(url));
            } else { // Definitely Non-windows
                Runtime runtime = Runtime.getRuntime();
                if(myOS.contains("mac")) { // Apples
                    runtime.exec("open " + url);
                }
                else if(myOS.contains("nix") || myOS.contains("nux")) { // Linux flavours

                    runtime.exec("xdg-open " + url);
                }
                else
                    OUT("OS not supported");
            }
        }
        catch(IOException | URISyntaxException eek) {
            OUT("**Stuff wrongly: "+ eek.getMessage());
        }
    }
 public  static void main(String[] args){

 }
    private static void OUT(String str) {
        System.out.println(str);
    }
}
