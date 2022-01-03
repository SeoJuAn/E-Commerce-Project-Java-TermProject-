package code;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class product {
	//static JLayeredPane productPane = new JLayeredPane();
	public static void product(int index) {
		//동적으로 반응하도록 하기 위해(함수가 호출될 때마다 새로운 페이지 생성) pane을 로컬 변수로 선
		JLayeredPane productPane = new JLayeredPane();
		productPane.setBounds(0, 0, 1500, 1500);
		Main.mainFrame.add(productPane);
		productPane.setVisible(true);
		
		ArrayList<String> datas = new ArrayList<String>();
		datas = getProductDB(index);
		String salerId = datas.get(0);
		String name = datas.get(1);
		String price = datas.get(2);
		String quantity = datas.get(3);
		String category = datas.get(4);
		String imagePath = datas.get(5);
		String content = datas.get(6);
		
//		String currentUser=null;
//		String currentUserType=null;
//		int currentUserPoint = 0;
//		if(getCurrentUser()!=null) {
//			currentUser = getCurrentUser().get(0);
//			currentUserType = getCurrentUser().get(1);
//			currentUserPoint = getCurrentUserPoint(currentUser,currentUserType);
//		}
		
		System.out.println("\nindex : "+index);
		for(String i : datas)
			System.out.print(i+" ");
		
		//ImagePanel imgPanel = new ImagePanel(new ImageIcon(imagePath).getImage());
//		JPanel imgPanel = new JPanel() {
//			Image background = new ImageIcon(imagePath).getImage();
//			Image setBackground = background.getScaledInstance(500, 500, java.awt.Image.SCALE_SMOOTH);
//			public void paint(Graphics g) {
//				g.drawImage(setBackground, 0, 0,null);
//			}
//		};
		//--------------------제품사진 패널----------------------
		JPanel imgPanel = new JPanel();
		
		ImageIcon img = new ImageIcon(imagePath);
		Image IconToImg = img.getImage();
		Image setSizeImg = IconToImg.getScaledInstance(500, 500, java.awt.Image.SCALE_SMOOTH);
		ImageIcon setImgIcon = new ImageIcon(setSizeImg);
		
		JButton button = new JButton(setImgIcon);
		button.setPreferredSize(new Dimension(500,500));
		button.setBounds(0, 0, 500, 500);
		imgPanel.setLayout(null);
		imgPanel.setBounds(230, 110, 500, 500);
		imgPanel.setBorder(new TitledBorder(new LineBorder(Color.gray)));
		imgPanel.add(new JLabel("img"));
		imgPanel.add(button);
		
		productPane.add(imgPanel);

		//--------------------제품설명 및 구매 패널----------------------
		
		
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(null);
		contentPanel.setBounds(770, 110, 500, 700);
		contentPanel.setBorder(new TitledBorder(new LineBorder(Color.gray)));
		contentPanel.add(new JLabel("content"));
		
		JLabel nameLabel = new JLabel(name);
		Font nameFont = new Font(null,Font.BOLD,30);
		nameLabel.setFont(nameFont);
		
		JLabel priceLabel = new JLabel("가격 : "+price+"원");
		Font priceFont = new Font(null,Font.BOLD,20);
		priceLabel.setFont(priceFont);
		priceLabel.setForeground(Color.red);
		
		JLabel contentLabel = new JLabel("<html>"+content+"</html>");
		contentLabel.setPreferredSize(new Dimension(380,300));
		JScrollPane scroll = new JScrollPane(contentLabel);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JLabel quantityLabel = new JLabel("수량 : ");
		
		String quantityOption[]= {"1","2","3","4","5","6","7","8","9","10"};
		JComboBox quantityComboBox=new JComboBox<String>(quantityOption);
		
		JButton buyButton = new JButton("구매하기");
		buyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//제품구매 버튼 클릭 시 반응
				String buyQuantity = quantityComboBox.getSelectedItem().toString();
				int int_buyQuantity = Integer.parseInt(buyQuantity);
				int payment = int_buyQuantity * Integer.parseInt(price);
				int result = JOptionPane.showConfirmDialog(null, name+"제품을 "+buyQuantity+"개 구입하시겠습니까?\n총 결재금액 : "+payment+"원", "Buying",JOptionPane.OK_CANCEL_OPTION);
                
				String currentUser=null;
				String currentUserType=null;
				int currentUserPoint = 0;
				if(!(getCurrentUser().isEmpty())) {
					currentUser = getCurrentUser().get(0);
					currentUserType = getCurrentUser().get(1);
					currentUserPoint = getCurrentUserPoint(currentUser,currentUserType);
				}
				
				if (result == 0) { //OK=0 , Cancel=2 리턴
                	if(currentUser == null) {
                		JOptionPane.showMessageDialog(null, "로그인 후 이용해주세요!", "경고", JOptionPane.WARNING_MESSAGE);
                	}
                	else {
                		System.out.println(currentUser+"->"+salerId+" "+payment+"  수량 :"+(Integer.parseInt(quantity)-int_buyQuantity));
                		System.out.println("예외처리1 : "+"현재수량<=0 이면 거래 X");
                		System.out.println("예외처리2 : "+ "currentUser의 계좌잔고<payment이면 거래X");
                		
                		if(Integer.parseInt(quantity)<=0) {
                			JOptionPane.showMessageDialog(null, "해당 상품은 품절입니다.", "경고", JOptionPane.WARNING_MESSAGE);
                		}
                		else if(currentUserPoint<payment) {
                			JOptionPane.showMessageDialog(null, "가상계좌의 잔액을 확인해주세요!\n마이페이지에서 충전하실 수 있습니다.", "경고", JOptionPane.WARNING_MESSAGE);
                		}
                		else {
                			updateDBofTransaction(currentUserType,currentUser,salerId,payment);
                			int newQuantity = Integer.parseInt(quantity)-int_buyQuantity;
                			updateDBofQuantity(index,newQuantity);
                			updateBuyList(index,currentUserType,currentUser);
                			JOptionPane.showMessageDialog(null, name+" 제품 "+buyQuantity+"개의 구매가 완료되었습니다.", "경고", JOptionPane.WARNING_MESSAGE);
                		}
                	}
                }
			}
		});
		
		JLabel remainingQuantityLabel = new JLabel("잔여수량 : "+quantity);
		JLabel categoryLabel = new JLabel("카테고리 : "+category);
		JLabel salerLabel = new JLabel("판매자 : "+salerId);
		
		nameLabel.setBounds(50, 20, 400, 50);
		priceLabel.setBounds(50, 80, 400, 50);
		scroll.setBounds(50, 140, 400, 300);
		quantityLabel.setBounds(50, 480, 35, 30);
		quantityComboBox.setBounds(90, 480, 80, 30);
		buyButton.setBounds(190, 480, 260, 30);
		remainingQuantityLabel.setBounds(50, 550, 200, 30);
		categoryLabel.setBounds(50, 590, 200, 30);
		salerLabel.setBounds(50, 630, 200, 30);
		
		contentPanel.add(nameLabel);
		contentPanel.add(priceLabel);
		contentPanel.add(scroll);
		contentPanel.add(quantityLabel);
		contentPanel.add(quantityComboBox);
		contentPanel.add(buyButton);
		contentPanel.add(remainingQuantityLabel);
		contentPanel.add(categoryLabel);
		contentPanel.add(salerLabel);
		
		
		productPane.add(contentPanel);
		
		
		
		
		
		JPanel gobackPanel = new JPanel();
		gobackPanel.setBounds(30, 30, 50, 100);
		JButton gobackButton= new JButton("돌아가기");
		gobackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//돌아가기 버튼 클릭 시 반응
				Main.mainPane.setVisible(true);
				productPane.setVisible(false);
				
			}
		});
		
		gobackPanel.add(gobackButton);
		productPane.add(gobackPanel);
	}
	
	public static ArrayList<String> getProductDB(int index){
		
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		ArrayList<String> datas = new ArrayList<String>();
		
		try {
			con = DriverManager.getConnection(url, user, pwd);
			String SQL = "select * from product where productIdx=?;";
			
			PreparedStatement p=con.prepareStatement(SQL);
			p.clearParameters();
			p.setString(1, index+"");

			ResultSet r = p.executeQuery();
			
			while(r.next()) {
				datas.add(r.getString(1));
				datas.add(r.getString(2));
				datas.add(r.getInt(3)+"");
				datas.add(r.getInt(4)+"");
				datas.add(r.getString(5));
				datas.add(r.getString(6));
				datas.add(r.getString(7));
				break;
			}
			
			con.close();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}
	public static ArrayList<String> getCurrentUser(){
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		ArrayList<String> datas = new ArrayList<String>();
		
		try {
			con = DriverManager.getConnection(url, user, pwd);
			String SQL = "select id,type from currentUser;";
			
			PreparedStatement p=con.prepareStatement(SQL);
			p.clearParameters();
			//p.setString(1, type);

			ResultSet r = p.executeQuery();
			
			while(r.next()) {
				datas.add(r.getString(1));
				datas.add(r.getString(2));
				break;
			}
				
			
			con.close();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}
	public static int getCurrentUserPoint(String currentUser,String currentUserType) {
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		int point=0;
		
		try {
			con = DriverManager.getConnection(url, user, pwd);
			String SQL = "select point from customer where id = ?;";
			if(currentUserType.equals("Saler")) {
				SQL = "select point from saler where id = ?;";
			}
			
			PreparedStatement p=con.prepareStatement(SQL);
			p.clearParameters();
			p.setString(1, currentUser);

			ResultSet r = p.executeQuery();
			
			while(r.next()) {
				point = r.getInt(1);
				break;
			}
				
			
			con.close();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return point;
	}
	public static void updateDBofTransaction(String currentUserType, String currentUser,String salerId,int payment) {
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		int currentUserPoint = 0;
		int salerPoint = 0;
		int currentUserNewPoint = 0;
		int salerNewPoint = 0;
		try {
			con = DriverManager.getConnection(url, user, pwd);
			//----------currentUser point 구하기------------
			String SQL1 = "select point from Customer where id=?;";
			if(currentUserType.equals("Saler")) {
				SQL1 = "select point from Saler where id=?;";
			}
			
			PreparedStatement p1=con.prepareStatement(SQL1);
			p1.clearParameters();
			p1.setString(1, currentUser);

			ResultSet r1 = p1.executeQuery();
			
			while(r1.next()) {
				currentUserPoint = r1.getInt(1);
				break;
			}
			currentUserNewPoint = currentUserPoint - payment;
			
			//----------saler point 구하기------------
			String SQL2 = "select point from Saler where id=?;";
			
			PreparedStatement p2=con.prepareStatement(SQL2);
			p2.clearParameters();
			p2.setString(1, salerId);

			ResultSet r2 = p2.executeQuery();
			
			while(r2.next()) {
				salerPoint = r2.getInt(1);
				break;
			}
			salerNewPoint = salerPoint + payment;
			
			//----------------currentUser update--------------------
			String updateSQL1 = "update customer set point=? where id=?";
			if(currentUserType.equals("Saler")) {
				updateSQL1 = "update saler set point=? where id=?";
			}
			PreparedStatement update_p1 = con.prepareStatement(updateSQL1);
			update_p1.clearParameters();
			update_p1.setInt(1,currentUserNewPoint);
			update_p1.setString(2, currentUser);
			
			update_p1.executeUpdate();
			
			//----------------saler update--------------------
			String updateSQL2 = "update saler set point=? where id=?";

			PreparedStatement update_p2 = con.prepareStatement(updateSQL2);
			update_p2.clearParameters();
			update_p2.setInt(1,salerNewPoint);
			update_p2.setString(2, salerId);
			
			update_p2.executeUpdate();
			con.close();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void updateDBofQuantity(int index,int newQuantity) {
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		int currentPoint = 0;
		int newPoint = 0;
		try {
			con = DriverManager.getConnection(url, user, pwd);
			
			String updateSQL = "update product set quantity=? where productIdx=?";

			PreparedStatement update_p = con.prepareStatement(updateSQL);
			update_p.clearParameters();
			update_p.setInt(1,newQuantity);
			update_p.setInt(2, index);
			
			update_p.executeUpdate();
			con.close();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void updateBuyList(int index,String currentUserType,String currentUser) {
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		String currentPurchasedItem = "";
		String newPurchasedItem = "";
		try {
			con = DriverManager.getConnection(url, user, pwd);
			//----------currentUser point 구하기------------
			String SQL1 = "select Purchaseditem from Customer where id=?;";
			if(currentUserType.equals("Saler")) {
				SQL1 = "select Purchaseditem from Saler where id=?;";
			}
			
			PreparedStatement p1=con.prepareStatement(SQL1);
			p1.clearParameters();
			p1.setString(1, currentUser);

			ResultSet r1 = p1.executeQuery();
			
			while(r1.next()) {
				currentPurchasedItem = r1.getString(1);
				break;
			}
			newPurchasedItem = currentPurchasedItem+","+index;
			
			
			//----------------currentUser update--------------------
			String updateSQL1 = "update customer set Purchaseditem=? where id=?";
			if(currentUserType.equals("Saler")) {
				updateSQL1 = "update saler set Purchaseditem=? where id=?";
			}
			PreparedStatement update_p1 = con.prepareStatement(updateSQL1);
			update_p1.clearParameters();
			update_p1.setString(1,newPurchasedItem);
			update_p1.setString(2, currentUser);
			
			update_p1.executeUpdate();
		}
			

		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


