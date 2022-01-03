package code;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class signup {
static JLayeredPane signupPane = new JLayeredPane();
	
	public static void signup() {
		
		
		
		signupPane.setBounds(0, 0, 1500, 1500);
		Main.mainFrame.add(signupPane);
		
		JPanel signupPanel = new JPanel();
		signupPanel.setBounds(600,400,350,200);
		signupPanel.setBorder(new TitledBorder(new LineBorder(Color.black)));
		signupPanel.setLayout(null);
		
		JLabel idLabel = new JLabel("아이디 ");
		JTextField idField = new JTextField(20);

		
		JLabel pwLabel = new JLabel("비밀번호 ");
		JPasswordField pwField = new JPasswordField(20);

		
		JLabel pwcheckLabel = new JLabel("비밀번호 확인 ");
		JPasswordField pwcheckField = new JPasswordField(20);

		
		JLabel typeLabel = new JLabel("회원 종류");
		String SearchOption[]= {"Customer","Saler"};
		JComboBox typecomboBox=new JComboBox<String>(SearchOption);
		
		
		JButton signupButton = new JButton("회원가입");
		signupButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//회원가입 버튼 클릭 시 반응
				
				String id = idField.getText();
				String pw = pwField.getText();
				String checkpw = pwcheckField.getText();
				String type = typecomboBox.getSelectedItem().toString();
				
				if(check_duplicated_id(id,type) && check_pw(pw, checkpw)) {
					insertInfo(id,pw,type);

					Main.mainPane.setVisible(true);
					signupPane.setVisible(false);
				}
				
				
			}
		});
		
		idLabel.setBounds(10,10,70,20);
		idField.setBounds(90,10,250,20);
		pwLabel.setBounds(10,40,70,20);
		pwField.setBounds(90,40,250,20);
		pwcheckLabel.setBounds(10,70,70,20);
		pwcheckField.setBounds(90,70,250,20);
		typeLabel.setBounds(10,100,70,20);
		typecomboBox.setBounds(90,100,250,20);
		signupButton.setBounds(260, 130, 80, 20);
		
		signupPanel.add(idLabel);
		signupPanel.add(idField);
		signupPanel.add(pwLabel);
		signupPanel.add(pwField);
		signupPanel.add(pwcheckLabel);
		signupPanel.add(pwcheckField);
		signupPanel.add(typeLabel);
		signupPanel.add(typecomboBox);
		signupPanel.add(signupButton);
		
		signupPane.add(signupPanel);

		JPanel gobackPanel = new JPanel();
		gobackPanel.setBounds(30, 30, 50, 100);
		JButton gobackButton= new JButton("돌아가기");
		gobackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//돌아가기 버튼 클릭 시 반응
				Main.mainPane.setVisible(true);
				signupPane.setVisible(false);
				
			}
		});
		
		gobackPanel.add(gobackButton);
		signupPane.add(gobackPanel);
		
		
	}
	
	public static boolean check_duplicated_id(String id, String type) {
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		boolean available = true;
		
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
					JOptionPane.showMessageDialog(null, "이미 사용중인 아이디입니다.","경고",JOptionPane.WARNING_MESSAGE);
					con.close();
					available = false;
					break;
					//return;	
				}
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return available;
		
	}
	
	public static boolean check_pw(String pw, String checkpw) {
		if(pw.equals(checkpw)==false)
			JOptionPane.showMessageDialog(null, "비밀번호를 확인하세요.","경고",JOptionPane.WARNING_MESSAGE);
		return pw.equals(checkpw);
		
	}
	
	public static void insertInfo(String id, String pw, String type) {
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, user, pwd);
			String SQL = "insert into customer values(?,?,?,?);";
			if(type.equals("Customer"))
				SQL = "insert into customer values(?,?,?,?);";
			else
				SQL = "insert into saler values(?,?,?,?,?);";
			PreparedStatement p=con.prepareStatement(SQL);
			p.clearParameters();
			//p.setString(1, type);
			p.setString(1, id);
			p.setString(2, pw);
			p.setInt(3, 0);
			p.setString(4, "");
			if(type.equals("Saler"))
				p.setString(5, "");
			
			p.executeUpdate();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
