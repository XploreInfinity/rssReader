import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.awt.*;
import java.sql.Statement;

public class Sources extends JFrame implements ActionListener{
    private JPanel header_panel,nav_panel,addSrc_panel;
    private JTextField sname_tf,slink_tf;
    private JButton exit_btn,home_btn,Updatefeeds_btn,addFeed_btn,removeFeed_btn,accessFeed_btn;
    private JTable source_tbl;
    private JLabel logo_lbl,sname_lbl,slink_lbl;
    private JScrollPane sources_tbl_scroll_pane;
    public Sources(){
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
    Updatefeeds_btn=new JButton("Update All Feeds");
    exit_btn=new JButton("Exit");
    nav_panel.add(home_btn);
    nav_panel.add(Updatefeeds_btn);
    nav_panel.add(exit_btn);
    header_panel.setSize(500,100);

    add(header_panel);
    add(nav_panel);

    //table:
       String[][] data={};
        String[] col={"Sr.no","Source Name","Source Link"};
        source_tbl=new JTable(new DefaultTableModel(data,col));

        sources_tbl_scroll_pane=new JScrollPane(source_tbl);
        add(sources_tbl_scroll_pane);
        sources_tbl_scroll_pane.setPreferredSize(new Dimension(450,200));
        removeFeed_btn=new JButton("Remove selected source");
        add(removeFeed_btn);
        accessFeed_btn=new JButton("Access selected source's feed");
        add(accessFeed_btn);
        //Add source panel:
        addSrc_panel=new JPanel();
        addSrc_panel.setLayout(new FlowLayout());
        addSrc_panel.setPreferredSize(new Dimension(450,80));
        addSrc_panel.setBorder(blackline);
        sname_lbl=new JLabel("Add a new Source :   Source Name:");
        sname_tf=new JTextField(15);
        slink_lbl=new JLabel("Source Link:");
        slink_tf=new JTextField(15);
        addFeed_btn=new JButton("Add Source");
        add(addSrc_panel);
        addSrc_panel.add(sname_lbl);
        addSrc_panel.add(sname_tf);
        addSrc_panel.add(slink_lbl);
        addSrc_panel.add(slink_tf);
        addSrc_panel.add(addFeed_btn);
    //action listeners:
    home_btn.addActionListener(this);
    Updatefeeds_btn.addActionListener(this);
    exit_btn.addActionListener(this);
    addFeed_btn.addActionListener(this);
    removeFeed_btn.addActionListener(this);
    accessFeed_btn.addActionListener(this);
    setSize(500,500);
    setLayout(new FlowLayout());
    setTitle("Add or Remove RSS Sources");
    setResizable(false);
    setVisible(true);
    //get data from database and display in table
    getSources();
    }
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource()==home_btn) {

            this.setVisible(false);
            new MainInterface();
        }
        else if(ae.getSource()==Updatefeeds_btn){
            this.setVisible(false);
            new UpdateFeeds();
        }
        else if(ae.getSource()==exit_btn)
        System.exit(0);
        else if(ae.getSource()==addFeed_btn)
        AddSource();
        else if(ae.getSource()==removeFeed_btn)
        RemoveSource();
        else if(ae.getSource()==accessFeed_btn){
           try{
            int selected_row=source_tbl.getSelectedRow();
            int id=(int)source_tbl.getValueAt(selected_row,0);
            String source=(String)source_tbl.getValueAt(selected_row,1);
            this.setVisible(false);
            new AccessFeed(id,source);
           }catch(ArrayIndexOutOfBoundsException e){
               JOptionPane.showMessageDialog(this,"Please select the source to access(only one at a time)","Exception",ERROR_MESSAGE);
           }
        }

    }

    //get data from database and display in table
    void getSources(){
        try{
            Class.forName("java.sql.DriverManager");
            Connection conn=(Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/feedReader","root","");
            Statement stmt=(Statement)conn.createStatement();
            String q="SELECT * FROM feedSources";
            ResultSet result=stmt.executeQuery(q);
            DefaultTableModel tmodel=(DefaultTableModel)source_tbl.getModel();
            while(result.next()){
                tmodel.addRow(new Object[]{result.getInt("id"),result.getString("name"),result.getString("link")});
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this,"Error:"+e,"Error",ERROR_MESSAGE);
        }
    }
    void AddSource(){
        String sname=sname_tf.getText();
        String slink=slink_tf.getText();
        if(!sname.isEmpty()&&!slink.isEmpty()) {
            try {
                Class.forName("java.sql.DriverManager");
                Connection conn=(Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/feedReader","root","");
                Statement stmt=(Statement)conn.createStatement();
                String q="INSERT  INTO feedSources(name,link) VALUES('"+sname+"','"+slink+"');";
                stmt.executeUpdate(q);
                DefaultTableModel tmodel=(DefaultTableModel)source_tbl.getModel();
                tmodel.setRowCount(0);
                getSources();
                slink_tf.setText("");
                sname_tf.setText("");
                JOptionPane.showMessageDialog(this,"Added Successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e,"Error!",ERROR_MESSAGE);
            }
        }
        else JOptionPane.showMessageDialog(this,"Please fill all the fields first!");
    }
    void RemoveSource(){
        try {
            int selected_row = source_tbl.getSelectedRow();
            int id = (int) source_tbl.getValueAt(selected_row, 0);
            Class.forName("java.sql.DriverManager");
            Connection conn=(Connection)DriverManager.getConnection("jdbc:mysql://127.0.0.1/feedReader","root","");
            Statement stmt=conn.createStatement();
            String query="DELETE from feedSources where id="+id+";";
            stmt.executeUpdate(query);
            DefaultTableModel tmodel = (DefaultTableModel) source_tbl.getModel();
            tmodel.setRowCount(0);
            getSources();
            JOptionPane.showMessageDialog(this,"Removed Successfully");

        }
        catch (ArrayIndexOutOfBoundsException e){
            JOptionPane.showMessageDialog(this,"Please select the row to remove(only one at a time)","Exception",ERROR_MESSAGE);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this,"Error:"+e,"Error",ERROR_MESSAGE);
        }
    }
}
