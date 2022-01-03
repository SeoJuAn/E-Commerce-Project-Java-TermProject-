package code;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class mypage {
	//static JLayeredPane mypagePane = new JLayeredPane();
	public static void mypage() {
		JLayeredPane mypagePane = new JLayeredPane();
		mypagePane.setBounds(0, 0, 1500, 1500);
		Main.mainFrame.add(mypagePane);
		mypagePane.setVisible(true);
		
		JPanel optionPanel = new JPanel();
		optionPanel.setLayout(null);
		optionPanel.setBounds(10, 180, 180, 600);
		optionPanel.setBorder(new TitledBorder(new LineBorder(Color.gray)));
		optionPanel.add(new JLabel("option"));
		
		JButton registerProductButton = new JButton("제품등록");
		registerProductButton.setBounds(50, 50, 80, 40);
		registerProductButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//제품등록 버튼 클릭 시 반응
				registerProduct.registerProductPane.setVisible(true);
				mypagePane.setVisible(false);
				registerProduct.registerProduct();
				
			}
		});
		if(check_saler()) {
			optionPanel.add(registerProductButton);
		}
		
		JButton registeredProductListButton = new JButton("등록한 제품");
		registeredProductListButton.setBounds(50, 120, 80, 40);
		registeredProductListButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//제품등록 버튼 클릭 시 반응
				mypagePane.setVisible(false);
				registeredItem.registeredItem();
				
			}
		});
		if(check_saler()) {
			optionPanel.add(registeredProductListButton);
		}
		
		JButton purchasedProductListButton = new JButton("구매한 제품");
		purchasedProductListButton.setBounds(50, 190, 80, 40);
		purchasedProductListButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//제품등록 버튼 클릭 시 반응
				mypagePane.setVisible(false);
				purchasedItem.purchasedItem();
				
			}
		});
		optionPanel.add(purchasedProductListButton);
		
		mypagePane.add(optionPanel);
		
		JPanel walletPanel = new JPanel();
		walletPanel.setLayout(null);
		walletPanel.setBounds(300, 100, 900, 300);
		walletPanel.setBorder(new TitledBorder(new LineBorder(Color.gray)));
		walletPanel.add(new JLabel("wallet"));
		
		JLabel walletLabel = new JLabel("Wallet");
		Font walletFont = new Font(null,Font.BOLD,40);
		walletLabel.setFont(walletFont);
		walletLabel.setHorizontalAlignment(JLabel.CENTER);
		
		ArrayList<String> datas = new ArrayList<String>();
		datas = getUserId_Point();
		String userId = datas.get(0);
		String type = datas.get(1);
		String point = datas.get(2);
		
		JLabel pointLabel = null;
		if(type.equals("Customer")) {
			pointLabel = new JLabel(userId+"고객님의 가상계좌 잔액은 "+point+"원 입니다.");
		}
		else {
			pointLabel = new JLabel(userId+"판매자님의 가상계좌 잔액은 "+point+"원 입니다.");	
		}
		Font pointFont = new Font(null,Font.BOLD,30);
		pointLabel.setFont(pointFont);
		pointLabel.setHorizontalAlignment(JLabel.CENTER);
		
		JButton chargeButton = new JButton("충전하기");
		chargeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//충전하기 버튼 클릭 시 반응
				String chargePoint = JOptionPane.showInputDialog(null,"충전하실 금액을 입력해주세요.","Charging",JOptionPane.OK_CANCEL_OPTION);
				if(chargePoint==null) {
					chargePoint = "0";
				}
				updatePoint(userId,type,Integer.parseInt(chargePoint));
				mypage();
			}
		});
		
		walletLabel.setBounds(250, 20, 400, 50);
		pointLabel.setBounds(50, 100, 800, 50);	
		chargeButton.setBounds(800, 240, 80, 40);
		
		walletPanel.add(walletLabel);
		walletPanel.add(pointLabel);
		walletPanel.add(chargeButton);

		
		mypagePane.add(walletPanel);

		JPanel gobackPanel = new JPanel();
		gobackPanel.setBounds(30, 30, 50, 100);
		JButton gobackButton= new JButton("돌아가기");
		gobackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//돌아가기 버튼 클릭 시 반응
				Main.mainPane.setVisible(true);
				mypagePane.setVisible(false);
				
			}
		});
		
		gobackPanel.add(gobackButton);
		mypagePane.add(gobackPanel);
		
	}
	public static boolean check_saler() {
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		boolean salerCheck = false;
		try {
			con = DriverManager.getConnection(url, user, pwd);
			String SQL = "select type from currentUser;";
			
			PreparedStatement p=con.prepareStatement(SQL);
			p.clearParameters();
			//p.setString(1, type);

			ResultSet r = p.executeQuery();
			
			while(r.next()) {
				if(r.getString(1).equals("Saler")) {
					con.close();
					salerCheck = true;
					break;
					//return;	
				}
			}
			
			con.close();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return salerCheck;
	}
	
	public static ArrayList<String> getUserId_Point(){
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		ArrayList<String> datas = new ArrayList<String>();
	
		try {
			con = DriverManager.getConnection(url, user, pwd);
			String SQL = "select U.id,U.type, C.point,S.point from currentUser U, customer C, saler S where U.id=C.id OR U.id=S.id;";
			
			PreparedStatement p=con.prepareStatement(SQL);
			p.clearParameters();
			//p.setString(1, type);

			ResultSet r = p.executeQuery();
			
			while(r.next()) {
				datas.add(r.getString(1));
				datas.add(r.getString(2));
				if(r.getString(2).equals("Customer")) {
					datas.add(r.getInt(3)+"");
					con.close();
					break;
					//return;	
				}
				else {
					datas.add(r.getInt(4)+"");
					con.close();
					break;
				}
			}
			
			con.close();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}
	
	public static void updatePoint(String userId, String type, int plusedPoint) {
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		int currentPoint = 0;
		int newPoint = 0;
		try {
			con = DriverManager.getConnection(url, user, pwd);
			String SQL = "select point from Customer where id=?;";
			if(type.equals("Saler")) {
				SQL = "select point from Saler where id=?;";
			}
			
			PreparedStatement p=con.prepareStatement(SQL);
			p.clearParameters();
			p.setString(1, userId);

			ResultSet r = p.executeQuery();
			
			while(r.next()) {
				currentPoint = r.getInt(1);
				break;
			}
			newPoint = currentPoint + plusedPoint;
			
			
			String updateSQL = "update customer set point=? where id=?";
			if(type.equals("Saler")) {
				updateSQL = "update saler set point=? where id=?";
			}
			PreparedStatement update_p = con.prepareStatement(updateSQL);
			update_p.clearParameters();
			update_p.setInt(1,newPoint);
			update_p.setString(2, userId);
			
			update_p.executeUpdate();
			con.close();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
