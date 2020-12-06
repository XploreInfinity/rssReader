import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.EventQueue.*;
import java.util.concurrent.Flow;

public class MainInterface extends JFrame implements ActionListener {
    //components declaration:
    private JPanel header_panel,nav_panel,addSrc_panel;
    private JButton exit_btn,feedSources_btn,Updatefeeds_btn;
    private JLabel logo_lbl;
    JTextArea ta_about;
    public MainInterface(){
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
       feedSources_btn=new JButton("Feed Sources");
        Updatefeeds_btn=new JButton("Update All Feeds");
        exit_btn=new JButton("Exit");
        nav_panel.add(feedSources_btn);
        nav_panel.add(Updatefeeds_btn);
        nav_panel.add(exit_btn);
        header_panel.setSize(500,100);
        ta_about=new JTextArea("RSS Reader is a simple RSS Feed reader that fetches RSS feeds from  user defined feed sources.");
        ta_about.append("\n\nFeeds are stored in a MariaDB database.This database will be re created automatically,if the application doesn't detect it.");
        ta_about.setFont(new Font("Zekton", Font.ITALIC,18));
        ta_about.setEditable(false);
        ta_about.setLineWrap(true);
        ta_about.setWrapStyleWord(true);
        ta_about.setSize(new Dimension(400,200));
        add(header_panel);
        add(nav_panel);
        add(ta_about);

        //action listeners:
        feedSources_btn.addActionListener(this);
        Updatefeeds_btn.addActionListener(this);
        exit_btn.addActionListener(this);
        setSize(500,500);
        FlowLayout FrameLayout=new FlowLayout();
        FrameLayout.setVgap(50);
        setLayout(FrameLayout);
        setTitle("RSS Reader- A simple feed Reader");
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource()==feedSources_btn){
            this.setVisible(false);
            new Sources();
        }
        else if(ae.getSource()==Updatefeeds_btn){
            this.setVisible(false);
            new UpdateFeeds();
        }
        else if(ae.getSource()==exit_btn)
            System.exit(0);
    }

}


