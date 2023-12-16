package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
public class Seat {
    JFrame seatFrame = new JFrame();
    private JPanel frame;
    private JTable table1;
    private JTextField name;
    private JTextField rollno;
    private JButton GETSEATButton;
    private JButton deleteButton;
    private JComboBox classDetail;
    private JLabel seatingArrrangementLabel;
    private JLabel rollnoLabel;
    private JLabel classDetailLabel;
    ArrayList<String> seat = new ArrayList<>();

    public Seat(){
        java.lang.System.out.println("entered");
        seatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        seatFrame.setContentPane(frame);
        seatFrame.pack();
        seatFrame.setLocationRelativeTo(null);
        seatFrame.setVisible(true);
        declareSeats();
        tableData();
        java.lang.System.out.println("entered_1");
        GETSEATButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                java.lang.System.out.println("entered_button");
                if( name.getText().equals("")|| rollno.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"Please fill all record to get seat.");
                }else{
                    java.lang.System.out.println("entered1");
                    try {
                        Random rn = new Random();
                        String seatNo="";
                        if(seat.size()==0){
                            java.lang.System.out.println("entered2");
                            JOptionPane.showMessageDialog(null,"THERE ARE NO SEATS AVAILABLE");
                        }else{
                            java.lang.System.out.println("entered3");
                            seatNo = seat.get(rn.nextInt(seat.size()));
                        }
                        String sql = "insert into seat"+"(NAME,ROLL_NO,CLASS,SEAT)"+"values (?,?,?,?)";
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/intern","root","Sairam#123");
                        PreparedStatement statement = connection.prepareStatement(sql);
                        statement.setString(1,name.getText());
                        statement.setString(2,rollno.getText());
                        statement.setString(3,""+classDetail.getSelectedItem());
                        statement.setString(4,seatNo);
                        seat.remove(seatNo);
                        statement.executeUpdate();
                        JOptionPane.showMessageDialog(null,"RECORD ADDED SUCCESSFULLY");
                        name.setText("");
                        rollno.setText("");
                        for(String i:seat){
                            System.out.println(i);
                        }
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(null,ex.getMessage());
                    }
                    tableData();
                }
            }
        });


        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRecord();
            }
        });





    }

    public void declareSeats(){
        java.lang.System.out.println("entered_declare");
        for(int i=1;i<=3;i++){
            for(int j=1;j<=10;j++){
                String s = "R"+i+"S"+j;
                seat.add(s);
            }
        }
        try {
            java.lang.System.out.println("entered_declare_1");
            String a= "Select SEAT from seat";
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.lang.System.out.println("entered_declare_2");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/intern","root","Sairam#123");
            java.lang.System.out.println("entered_declare_3");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(a);
            while (rs.next()){
                seat.remove(rs.getString(1));
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void tableData() {
        try{
            String a= "Select* from seat";
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/intern","root","Sairam#123");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(a);
            table1.setModel(buildTableModel(rs));
        }catch (Exception ex1){
            JOptionPane.showMessageDialog(null,ex1.getMessage());
        }
    }
    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
// names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }
// data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }
        return new DefaultTableModel(data, columnNames);
    }


    private void deleteSelectedRecord() {
        int selectedRow = table1.getSelectedRow();

        if (selectedRow != -1) {
            try {
                String seatToDelete = (String) table1.getValueAt(selectedRow, table1.getColumnCount() - 1);
                String sql = "DELETE FROM seat WHERE SEAT = ?";
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/intern", "root", "Sairam#123");
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, seatToDelete);
                statement.executeUpdate();

                JOptionPane.showMessageDialog(null, "Record deleted successfully");
                declareSeats();
                tableData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a record to delete.", "No Record Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

}
