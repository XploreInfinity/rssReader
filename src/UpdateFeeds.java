import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import com.mysql.jdbc.Driver;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.awt.*;
import java.sql.Statement;

public class UpdateFeeds extends JFrame implements ActionListener {
    private JPanel header_panel, nav_panel;
    private JButton exit_btn, home_btn, feedSources_btn,update_feedsBtn;
    private JLabel logo_lbl,update_lbl,info_lbl;
    private JProgressBar updateProgress;


    public UpdateFeeds(){
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

        update_lbl=new JLabel("Update Feed Sources:");
        update_lbl.setFont(new Font("",Font.PLAIN,25));
        update_feedsBtn=new JButton("Update");
        info_lbl=new JLabel("Refreshes content feeds of all the sources");
        add(update_lbl);
        add(update_feedsBtn);
        add(info_lbl);
        setVisible(true);
        setTitle("Update Feeds of All Sources");

        //Action Listeners:
        home_btn.addActionListener(this);
        feedSources_btn.addActionListener(this);
        exit_btn.addActionListener(this);
        update_feedsBtn.addActionListener(this);
        FlowLayout FrameLayout=new FlowLayout();
        FrameLayout.setHgap(100);
        setLayout(FrameLayout);
        setResizable(false);
        setSize(500,500);

    }


    public void actionPerformed(ActionEvent ae){
        if(ae.getSource()==home_btn) {

            this.setVisible(false);
            new MainInterface();
        }
        if(ae.getSource()==feedSources_btn){
            this.setVisible(false);
            new Sources();
        }
        else if(ae.getSource()==exit_btn)
            System.exit(0);

        else if(ae.getSource()==update_feedsBtn)
            update();
    }

    void update(){
    update_feedsBtn.setEnabled(false);
    try{
        Class.forName("java.sql.DriverManager");
        Connection conn=(Connection)DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/feedReader","root","");
        Statement stmt=(Statement)conn.createStatement();
        String query="SELECT id,link FROM feedSources";
        ResultSet result=stmt.executeQuery(query);
        while(result.next()){
            RSSReader.readRSS(result.getInt("id"),result.getString("link"));
        }
        update_feedsBtn.setEnabled(true);
        info_lbl.setText("Sources Updated Successfully!");
    }catch(Exception e){
        JOptionPane.showMessageDialog(this,"Error:"+e+"\nSome Feeds Might not have been updated properly","Error",ERROR_MESSAGE);
        update_feedsBtn.setEnabled(true);
    }
    }
}

