import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.awt.*;
import java.sql.Statement;
public class AccessFeed extends JFrame implements ActionListener{
    private JPanel header_panel,nav_panel;
    private JButton exit_btn,home_btn,feedSources_btn,articleView_btn;
    private JTable feedList_tbl;
    private JLabel logo_lbl,sourceTitle_lbl;
    private JScrollPane feedList_tbl_scroll_pane;
    int id;
    String source;
    public AccessFeed(int idval,String sourceval){
        id=idval;
        source=sourceval;
        header_panel=new JPanel();
        header_panel.setLayout(new FlowLayout());
        logo_lbl=new JLabel("Reader");
        logo_lbl.setFont(new Font("Zekton",Font.ITALIC,24));
        ImageIcon icon = new ImageIcon("/home/xploreinfinity/IdeaProjects/feedReader/src/resources/logo.png");
        Image img=icon.getImage();
        Image newimg = img.getScaledInstance(100, 80,  java.awt.Image.SCALE_SMOOTH);
        icon=new ImageIcon(newimg);
        logo_lbl.setIcon(icon);
        header_panel.add(logo_lbl);
        header_panel.setPreferredSize(new Dimension(650,90));
        //nav panel stuff:
        FlowLayout nav_panelLayout=new FlowLayout();
        nav_panelLayout.setHgap(30);
        nav_panel=new JPanel();
        nav_panel.setLayout(nav_panelLayout);
        Border blackline = BorderFactory.createLineBorder(Color.black);
        nav_panel.setBorder(blackline);
        home_btn=new JButton("Home");
        feedSources_btn=new JButton("Feed Sources");
        exit_btn=new JButton("Exit");
        nav_panel.add(home_btn);
        nav_panel.add(feedSources_btn);
        nav_panel.add(exit_btn);
        header_panel.setSize(500,100);

        add(header_panel);
        add(nav_panel);
        sourceTitle_lbl=new JLabel("Feed from "+sourceval);
        sourceTitle_lbl.setFont(new Font("",Font.PLAIN,23));
        sourceTitle_lbl.setHorizontalAlignment(SwingConstants.CENTER);
        sourceTitle_lbl.setPreferredSize(new Dimension(800,50));
        add(sourceTitle_lbl);
        //table:
        String[][] data={};
        String[] col={"Publish Date","Title","Description","Article URL"};
        feedList_tbl=new JTable(new DefaultTableModel(data,col));
       feedList_tbl_scroll_pane=new JScrollPane(feedList_tbl,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       feedList_tbl.setAutoscrolls(true);
       add(feedList_tbl_scroll_pane);
        feedList_tbl_scroll_pane.setPreferredSize(new Dimension(790,350));
        getFeed();
        articleView_btn=new JButton("Open selected article");
        add(articleView_btn);

        //action Listeners:
        home_btn.addActionListener(this);
        feedSources_btn.addActionListener(this);
        exit_btn.addActionListener(this);
        articleView_btn.addActionListener(this);
        setSize(800,630);
        setTitle("Feed From "+sourceval);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setResizable(false);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource()==home_btn) {

            this.setVisible(false);
            new MainInterface();
        }
        else if(ae.getSource()==feedSources_btn){
            this.setVisible(false);
            new Sources();
        }
        else if(ae.getSource()==exit_btn)
            System.exit(0);
        else if(ae.getSource()==articleView_btn)
            visitArticle();

    }
    void getFeed(){
        try{
            Class.forName("java.sql.DriverManager");
            Connection conn=(Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/feedReader","root","");
            Statement stmt=(Statement)conn.createStatement();
            String q="SELECT * FROM feedContent WHERE feedID="+id+" ORDER BY publish_date desc;";
            ResultSet result=stmt.executeQuery(q);
            DefaultTableModel tmodel=(DefaultTableModel)feedList_tbl.getModel();
            while(result.next()){
                tmodel.addRow(new Object[]{result.getString("publish_date"),result.getString("title"),result.getString("description"),result.getString("link")});
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this,"Error:"+e,"Error",ERROR_MESSAGE);
        }
    }
    void visitArticle(){
        try {
            int row = feedList_tbl.getSelectedRow();
            String url = (String) feedList_tbl.getValueAt(row, 3);
            new MultiBrowPop(url);
        }catch(ArrayIndexOutOfBoundsException e){
            JOptionPane.showMessageDialog(this,"Please select the row to remove(only one at a time)","Exception",ERROR_MESSAGE);
        }
    }

}
