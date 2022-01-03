package code;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
//import code.Main.*;

public class login {
	static JLayeredPane loginPane = new JLayeredPane();
	public static void login() {
		loginPane.setBounds(0, 0, 1500, 1500);
		Main.mainFrame.add(loginPane);
		
		
		JPanel loginPanel = new JPanel();
		loginPanel.setBounds(600,400,350,150);
		loginPanel.setBorder(new TitledBorder(new LineBorder(Color.black)));
		loginPanel.setLayout(null);
		

		JLabel idLabel = new JLabel("아이디 ");
		JTextField idField = new JTextField(20);

		JLabel pwLabel = new JLabel("비밀번호 ");
		JPasswordField pwField = new JPasswordField(20);
		
		JLabel typeLabel = new JLabel("회원 종류");
		String SearchOption[]= {"Customer","Saler"};
		JComboBox typecomboBox=new JComboBox<String>(SearchOption);
		
		JButton loginButton = new JButton("로그인");
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//로그인 버튼 클릭 시 반응
				String id = idField.getText();
				String pw = pwField.getText();
				String type = typecomboBox.getSelectedItem().toString();
				
				if(check_exit_id(id,type) && check_pw(id,pw,type)) {
					registerCurrentUser(id,type);
					Main.mainPane.setVisible(true);
					loginPane.setVisible(false);
					Main.loginStatePanel.setVisible(true);
					Main.logoutStatePanel.setVisible(false);
				}
			}
		});
		
		idLabel.setBounds(10,10,70,20);
		idField.setBounds(90, 10, 250, 20);
		pwLabel.setBounds(10,40,70,20);
		pwField.setBounds(90, 40, 250, 20);
		typeLabel.setBounds(10,70,70,20);
		typecomboBox.setBounds(90,70,250,20);
		loginButton.setBounds(260, 100, 80, 20);
		
		loginPanel.add(idLabel);
		loginPanel.add(idField);
		loginPanel.add(pwLabel);
		loginPanel.add(pwField);
		loginPanel.add(typeLabel);
		loginPanel.add(typecomboBox);
		loginPanel.add(loginButton);
		
		loginPanel.setLayout(null);
		loginPane.add(loginPanel);

		JPanel gobackPanel = new JPanel();
		gobackPanel.setBounds(30, 30, 50, 100);
		JButton gobackButton= new JButton("돌아가기");
		gobackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//돌아가기 버튼 클릭 시 반응
				Main.mainPane.setVisible(true);
				loginPane.setVisible(false);
				
			}
		});
		
		gobackPanel.add(gobackButton);
		loginPane.add(gobackPanel);
		
	}
	
	public static boolean check_exit_id(String id,String type) {
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		boolean exit = false;
		
		try {
			con = DriverManager.getConnection(url, user, pwd);
			String Exception_SQL;
			if(type.equals("Customer")) {
				Exception_SQL = "select Id from customer;";
			}
			else {
				Exception_SQL = "select Id from saler;";
			}
			PreparedStatement Exception_p=con.prepareStatement(Exception_SQL);
			Exception_p.clearParameters();
			ResultSet r = Exception_p.executeQuery();
			
			while(r.next()) {
				if(r.getString(1).equals(id)) {
					con.close();
					exit = true;
					break;
					//return;	
				}
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(exit==false) {
			JOptionPane.showMessageDialog(null, "해당 ID가 존재하지 않습니다.","경고",JOptionPane.WARNING_MESSAGE);
		}
		return exit;
	}
	
	public static boolean check_pw(String id, String pw, String type) {
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		boolean samePW = false;
		try {
			con = DriverManager.getConnection(url, user, pwd);
			String Exception_SQL;
			if(type.equals("Customer")) {
				Exception_SQL = "select pw from customer where Id=?;";
			}
			else {
				Exception_SQL = "select pw from saler where Id=?;";
			}
			PreparedStatement Exception_p=con.prepareStatement(Exception_SQL);
			Exception_p.clearParameters();
			Exception_p.setString(1, id);
			ResultSet r = Exception_p.executeQuery();
			
			while(r.next()) {
				if(r.getString(1).equals(pw)) {
					con.close();
					samePW = true;
					break;
					//return;	
				}
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(samePW==false) {
			JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.","경고",JOptionPane.WARNING_MESSAGE);
		}
		return samePW;
	}
	
	public static void registerCurrentUser(String id,String type) {
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, user, pwd);
			String SQL = "insert into currentUser values(?,?);";
			
			PreparedStatement p=con.prepareStatement(SQL);
			p.clearParameters();
			//p.setString(1, type);
			p.setString(1, id);
			p.setString(2, type);
			
			p.executeUpdate();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}
	
	
}
