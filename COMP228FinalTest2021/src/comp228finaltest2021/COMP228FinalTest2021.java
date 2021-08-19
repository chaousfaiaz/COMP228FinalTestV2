/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp228finaltest2021;

/**
 *
 * @author faiaz
 */
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class COMP228FinalTest2021 extends JFrame{
	JTabbedPane tabbedPane = new JTabbedPane();
	JPanel view = new JPanel();
	JPanel mainPanel;
	JPanel findAndEdit, 
		findAndEditHeader, 
		findAndEditHeader1, 
		findAndEditHeader2,
		editFields;
	JButton srchB, updB, refreshB;
	JTextField searchText, id, address, city, postalCode, province;
	
	JTable table, table2;
	Vector <Object>  rowsView, columnsView;
	Vector <Object> rowsSearch, columnsSearch;
	DefaultTableModel tableModel, tableModel2;
	JScrollPane scrollPane, scrollPane2;
	//
	Connection conn;
	PreparedStatement pst;
	Statement st;
	ResultSet rs;
	

	public COMP228FinalTest2021() {
		UpdateListener updListener = new UpdateListener(); 
		SearchListener searchListener = new SearchListener();
		RefreshListener refreshListener = new RefreshListener();

		rowsView=new Vector<>();
		columnsView= new Vector<>();
		rowsSearch= new Vector<>();
		columnsSearch = new Vector<>();

		tableModel = new DefaultTableModel();
		tableModel2 = new DefaultTableModel();
		table = new JTable(tableModel);
		table2 = new JTable(tableModel2);
		scrollPane= new JScrollPane(table);//ScrollPane
		scrollPane2= new JScrollPane(table2);//ScrollPane

		mainPanel=new JPanel();
		setSize(750,480);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add("North", new JLabel("Student List"));
		mainPanel.add("Center",scrollPane);
		refreshB=new JButton("Refresh");
		mainPanel.add("South",refreshB);
		
		findAndEdit = new JPanel();
		findAndEdit.setLayout(new BorderLayout());
		findAndEditHeader = new JPanel();
		findAndEditHeader1 = new JPanel(new GridLayout(7,2));
		editFields = new JPanel();
		editFields.add(new JLabel("ID"));
		
		//findAndEditHeader1.add(new JLabel("Name"));
		searchText = new JTextField(20);
		srchB = new JButton("Filter by Name");
		findAndEditHeader1.add(searchText);
		findAndEditHeader1.add(srchB);
		
		findAndEditHeader1.add(new JLabel("ID"));
		id = new JTextField(10);
		findAndEditHeader1.add(id);
		
		findAndEditHeader1.add(new JLabel("Address"));
		address = new JTextField(10);
		findAndEditHeader1.add(address);
		
		findAndEditHeader1.add(new JLabel("City"));
		city = new JTextField(10);
		findAndEditHeader1.add(city);
		
		findAndEditHeader1.add(new JLabel("Postal Code"));
		postalCode = new JTextField(10);
		findAndEditHeader1.add(postalCode);
		
		findAndEditHeader1.add(new JLabel("Province"));
		province = new JTextField(10);
		findAndEditHeader1.add(province);
		
		updB = new JButton("Update Address");
		findAndEditHeader.add(findAndEditHeader1);
		findAndEditHeader1.add(updB);
		
		findAndEditHeader.add(Box.createVerticalStrut(5));
		findAndEdit.add("North", findAndEditHeader);
		findAndEdit.add("Center", scrollPane2);

		tabbedPane.addTab("View", null, mainPanel, "Viewing table only");
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		setVisible(true);
		
		tabbedPane.addTab("Search and Edit", null, findAndEdit, "Change Address");
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		setVisible(true);
		
		updB.addActionListener(updListener);
		srchB.addActionListener(searchListener);
		refreshB.addActionListener(refreshListener);
		
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://localhost/Campus?"
					+ "user=root&password=password";
			conn = DriverManager.getConnection(url);

			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM Students");
			ResultSetMetaData md = rs.getMetaData();
			//create columns headers
			for( int i=1;i <= md.getColumnCount();i++)
			{
				columnsView.addElement(md.getColumnName(i));
			}

			int row=0;
			while(rs.next())
			{
				Vector vRow = new Vector(); //to store the current row
				//System.out.println("Row " +row+"\n");
				for( int i=1;i <= md.getColumnCount();i++)
				{

					String columnValue = rs.getString(i);
					vRow.addElement(columnValue);
				}
				row+=1;
				rowsView.addElement(vRow);

			}

			tableModel.setDataVector(rowsView,columnsView);
			
			//use the same data for the second JTable
			tableModel2.setDataVector(rowsView, columnsView);;
			rs.close();

		}
		catch(Exception e) {
			e.printStackTrace();

		}

	}
	public static void main(String[] args) {
		COMP228FinalTest2021 tc = new COMP228FinalTest2021();
		tc.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

	}
	
	class SearchListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			// Select only some of the stuff from the table
			try{
				tableModel.setRowCount(0);
				tableModel.setColumnCount(0);
				tableModel2.setRowCount(0);
				tableModel2.setColumnCount(0);
				st=conn.createStatement();
				rs=st.executeQuery("SELECT * from Students where LastName ='" + searchText.getText()+"'");
				ResultSetMetaData md = rs.getMetaData();
				//create columns headers
				for( int i=1;i <= md.getColumnCount();i++)
				{
					columnsView.addElement(md.getColumnName(i));
				}

				int row=0;
				while(rs.next())
				{
					Vector vRow = new Vector(); //to store the current row
					//System.out.println("Row " +row+"\n");
					for( int i=1;i <= md.getColumnCount();i++)
					{

						String columnValue = rs.getString(i);
						vRow.addElement(columnValue);
					}
					row+=1;
					rowsView.addElement(vRow);

				}

				tableModel.setDataVector(rowsView,columnsView);
				
				//use the same data for the second JTable
				tableModel2.setDataVector(rowsView, columnsView);;
				rs.close();

			}
			catch(Exception e) {
				e.printStackTrace();

			}
			}
			
			
		}
	

	class UpdateListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			// update the table with the values from the text fields
			try{
				st = conn.createStatement();
				
			pst=conn.prepareStatement("UPDATE Students SET Address ='"+address.getText()+"', City ='"+city.getText()+"', PostalCode ='"+postalCode.getText()+"', Province='"+province.getText()+"' WHERE StudentID="+id.getText()+";");
			pst.executeUpdate();
			/*pst.setString(1, address.getText());
			pst.setString(2, city.getText());
			pst.setString(3, postalCode.getText());
			pst.setString(4, province.getText());
			pst.setString(5, id.getText());*/
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		
		}
	}

	class RefreshListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			try{
				tableModel.setRowCount(0);
				tableModel.setColumnCount(0);
				tableModel2.setRowCount(0);
				tableModel2.setColumnCount(0);
				st=conn.createStatement();
				rs=st.executeQuery("SELECT * from Students");
				ResultSetMetaData md = rs.getMetaData();
				//create columns headers
				for( int i=1;i <= md.getColumnCount();i++)
				{
					columnsView.addElement(md.getColumnName(i));
				}

				int row=0;
				while(rs.next())
				{
					Vector vRow = new Vector(); //to store the current row
					//System.out.println("Row " +row+"\n");
					for( int i=1;i <= md.getColumnCount();i++)
					{

						String columnValue = rs.getString(i);
						vRow.addElement(columnValue);
					}
					row+=1;
					rowsView.addElement(vRow);

				}

				tableModel.setDataVector(rowsView,columnsView);
				
				//use the same data for the second JTable
				tableModel2.setDataVector(rowsView, columnsView);;
				rs.close();

			}
			catch(Exception e) {
				e.printStackTrace();

			}
		}
	}
	}
