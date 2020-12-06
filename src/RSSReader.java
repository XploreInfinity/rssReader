
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import java.sql.DriverManager;
public class RSSReader {

    public static void readRSS(int fsourceID,String urlAddress) {
        String sourceCode = "", line, tempT, tempL, tempD="", tempPD;
        int firstPos, lastPos;
        boolean success=false;
        Vector<String> title = new Vector<String>();
        Vector<String> description = new Vector<String>();
        Vector<String> link = new Vector<String>();
        Vector<String> pubdate = new Vector<String>();

        try {
            URL rssURL = new URL(urlAddress);
            BufferedReader in = new BufferedReader(new InputStreamReader(rssURL.openStream()));
            boolean flag=true;
            while ((line = in.readLine()) != null) {
                if (!(line.contains("<item>")) && flag)
                    continue;
                flag=false;
                if (line.contains("<title>")) {
                    firstPos = line.indexOf("<title>");
                    tempT = line.substring(firstPos);
                    lastPos = tempT.indexOf("</title>");
                    if (line.contains("<title><![CDATA["))
                        tempT = tempT.substring(16, lastPos-3);
                    else
                        tempT = tempT.substring(7, lastPos);
                    tempT=tempT.replaceAll("'","`");
                    title.add(tempT);
                }
                if (line.contains("<description>")) {
                    firstPos = line.indexOf("<description>");
                    tempD = line.substring(firstPos);
                    lastPos = tempD.indexOf("</description>");
                    if (line.contains("<description><![CDATA["))
                        tempD = tempD.substring(22, lastPos-3);
                    else
                        tempD = tempD.substring(13, lastPos);
                    if (tempD.length()>105)
                        tempD=tempD.substring(0,105)+"...";
                    tempD= tempD.replaceAll("'","`");
                    /*if(tempD.isBlank()||tempD.equals(" ")) {
                        tempD="No Description";
                        description.add(tempD);
                    }*/
                    description.add(tempD);
                }/*
                else if(line.contains("<description/>")) {
                    tempD="No Description";
                    description.add(tempD);
                }*/
                if (line.contains("<link>")) {
                    firstPos = line.indexOf("<link>");
                    tempL = line.substring(firstPos);
                    lastPos = tempL.indexOf("</link>");
                    tempL = tempL.substring(6, lastPos);
                    tempL= tempL.replaceAll("'","`");
                    link.add(tempL);
                }
                if (line.contains("<pubDate>")) {
                    firstPos = line.indexOf("<pubDate>");
                    tempPD = line.substring(firstPos);
                    lastPos = tempPD.indexOf("</pubDate>");
                    tempPD = tempPD.substring(9, lastPos);
                    tempPD= tempPD.replaceAll("'","^");
                    pubdate.add(tempPD);
                }
            }
            in.close();
            success=true;

        } catch (MalformedURLException eM) {
            System.out.println("Bad URL! \n" + eM);
        } catch (IOException eIO) {
            System.out.println(eIO);
        }
        if(success){

            try{
                Class.forName("java.sql.DriverManager");
                Connection conn=(Connection)DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/feedReader","root","");
                Statement stmt=(Statement)conn.createStatement();
                String query="DELETE from feedContent where feedID="+fsourceID+";";
                stmt.executeUpdate(query);
                for(int i=0;i<title.size();i++) {
                    String query2;
                    if(!description.elementAt(i).isEmpty())
                    query2 = "INSERT INTO feedContent values(" + fsourceID + ",'" + title.elementAt(i) + "','"+description.elementAt(i)+"','"+pubdate.elementAt(i)+"','"+link.elementAt(i)+"');";
                    else query2 = "INSERT INTO feedContent (feedID,title,publish_date,link) values(" + fsourceID + ",'" + title.elementAt(i) + "','"+pubdate.elementAt(i)+"','"+link.elementAt(i)+"');";
                    stmt.executeUpdate(query2);
                }
            }catch (Exception e){
                JOptionPane.showMessageDialog(null,"Error:"+e+"\ntest Some feeds might not have updated","Error",ERROR_MESSAGE);
            }

        }

    }
}