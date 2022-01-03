package code;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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




public class Main {
	static JFrame mainFrame = new JFrame();	
	static JLayeredPane mainPane = new JLayeredPane();
	static JPanel logoutStatePanel = new JPanel();
	static JPanel loginStatePanel = new JPanel();

	public static void main(String[] args) {
		
//		JFrame mainFrame = new JFrame();
		mainFrame.setSize(1500,1500);
		mainFrame.setLayout(null);
		mainFrame.setVisible(true);
		//mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//종료 버튼 눌렀을 때 꺼지기 및 자동 로그아웃
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//자동 로그아웃
				logout.logout();
				//창 꺼지기
				System.exit(0);
			}
		});
		
//		JLayeredPane mainPane = new JLayeredPane();
		mainPane.setBounds(0,0,1500,1500);
		
//		JPanel categoryPanel = new JPanel();
//		categoryPanel.setBounds(10, 180, 180, 600);
//		categoryPanel.setBorder(new TitledBorder(new LineBorder(Color.gray)));
//		categoryPanel.add(new JLabel("카테고리"));
//		//mainFrame.add(categoryPanel, BorderLayout.WEST);
//		mainPane.add(categoryPanel, BorderLayout.WEST);
		
		
		
//		JPanel searchPanel = new JPanel();
//		searchPanel.setBounds(500, 80, 500, 50);
//		//searchPanel.setBorder(new TitledBorder(new LineBorder(Color.black)));
//		JTextField textField = new JTextField(30);
//		searchPanel.add(textField);
//
//		JButton searchButton = new JButton("검색하기");
//		searchPanel.add(searchButton);
//		searchButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				//검색버튼 클릭 시 반응
//				
//			}
//		});
//		//mainFrame.add(searchPanel, BorderLayout.CENTER);
//		mainPane.add(searchPanel, BorderLayout.CENTER);
		
		
		logoutStatePanel.setBounds(1080, 80, 300, 50);
		logoutStatePanel.setVisible(true);
		loginStatePanel.setBounds(1080, 80, 300, 50);
		loginStatePanel.setVisible(false);

		JButton logoutButton = new JButton("로그아웃");
		loginStatePanel.add(logoutButton);
		
		JButton mypageButton = new JButton("마이페이지");
		loginStatePanel.add(mypageButton);
		
		JButton loginButton = new JButton("로그인");
		logoutStatePanel.add(loginButton);
		

		JButton signupButton = new JButton("회원가입");
		logoutStatePanel.add(signupButton);

		
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//로그인 버튼 클릭 시 반응
				mainPane.setVisible(false);
				login.loginPane.setVisible(true);
				login.login();
				
			}
		});
		
		signupButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//회원가입 버튼 클릭 시 반응
				mainPane.setVisible(false);
				signup.signupPane.setVisible(true);
				signup.signup();
				
			}
		});
		
		logoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//로그인 버튼 클릭 시 반응
				loginStatePanel.setVisible(false);
				logoutStatePanel.setVisible(true);
				logout.logout();
				
			}
		});
		
		mypageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//마이페이지 버튼 클릭 시 반응
				mainPane.setVisible(false);
				//mypage.mypagePane.setVisible(true);
				mypage.mypage();
				
			}
		});
		//mainFrame.add(logoutStatePanel, BorderLayout.CENTER);
		mainPane.add(logoutStatePanel, BorderLayout.CENTER);
		mainPane.add(loginStatePanel,BorderLayout.CENTER);

		//------------------------------------------------------
		//제품 버튼
		JPanel productPanel = new JPanel();
		productPanel.setPreferredSize(new Dimension(1000,1000));
		
		ArrayList<JButton> buttons = new ArrayList<>();
		

//		for(int i = 0;i<20;i++) {
//			//product 버튼 생성 
//			buttons.add(new JButton("product"+(i+1)));
//
//			//버튼 사이즈 설정 
//			buttons.get(i).setPreferredSize(new Dimension(200,200));
//			//클릭 이벤트 설정 
//			buttons.get(i).addActionListener(new myActionListener(i));
//			//패널에 버튼 추가 
//			productPanel.add(buttons.get(i));
//		}
		
		for(int i=0;i<productDB().size();i++) {
			String salerId = productDB().get(i).get(0);
			String name = productDB().get(i).get(1);
			String price = productDB().get(i).get(2);
			String imagePath = productDB().get(i).get(3);
			
			ImageIcon img = new ImageIcon(imagePath);
			Image IconToImg = img.getImage();
			Image setSizeImg = IconToImg.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
			ImageIcon setImgIcon = new ImageIcon(setSizeImg);
			
			JButton button = new JButton(setImgIcon);
			buttons.add(button);
			

			//버튼 사이즈 설정 
			buttons.get(i).setPreferredSize(new Dimension(200,200));
			//클릭 이벤트 설정 
			buttons.get(i).addActionListener(new myActionListener(i+1));
			//패널에 버튼 추가 
			productPanel.add(buttons.get(i));
			

		}
		
		
		JScrollPane scroll = new JScrollPane();
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setViewportView(productPanel);

		scroll.setBounds(300, 180, 1080, 600);
		//mainFrame.add(scroll);
		mainPane.add(scroll);
		
		
		
		JPanel searchPanel = new JPanel();
		searchPanel.setBounds(500, 80, 500, 50);
		//searchPanel.setBorder(new TitledBorder(new LineBorder(Color.black)));
		JTextField textField = new JTextField(30);
		searchPanel.add(textField);

		JButton searchButton = new JButton("검색하기");
		searchPanel.add(searchButton);
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//검색버튼 클릭 시 반응
				String searchText = textField.getText();
				if(searchText.equals("")) {
					for(int i=0;i<buttons.size();i++) {
						buttons.get(i).setVisible(true);
					}
				}
				else {
					ArrayList<Integer> searchProductIdxList = new ArrayList<>();
					searchProductIdxList = searchProductIdx(searchText);
					for(int i=0;i<buttons.size();i++) {
						buttons.get(i).setVisible(false);
					}
					for(int i : searchProductIdxList) {
						buttons.get(i-1).setVisible(true);
					}
				}
				
			}
		});
		//mainFrame.add(searchPanel, BorderLayout.CENTER);
		mainPane.add(searchPanel, BorderLayout.CENTER);
		
		JPanel categoryPanel = new JPanel();
		categoryPanel.setBounds(10, 180, 180, 600);
		categoryPanel.setBorder(new TitledBorder(new LineBorder(Color.gray)));
		categoryPanel.setLayout(null);
		
		JLabel categoryLabel = new JLabel("카테고리");
		JCheckBox total = new JCheckBox("전체");
		JCheckBox digitalProduct = new JCheckBox("디지털기기");
		JCheckBox lifeDigitalProduct = new JCheckBox("생활가전");
		JCheckBox life = new JCheckBox("생활/가공식품");
		JCheckBox book = new JCheckBox("도서");
		JCheckBox sports = new JCheckBox("스포츠/레저");
		JCheckBox cloth = new JCheckBox("패션/잡화");
		JCheckBox game = new JCheckBox("게임/취미");
		JCheckBox beauty = new JCheckBox("뷰티/미용");
		JCheckBox dog = new JCheckBox("반려동물용품");
		JCheckBox etc = new JCheckBox("기타");
		
		JButton categoryButton = new JButton("카테고리 검색하기");
		categoryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//검색버튼 클릭 시 반응
				ArrayList<String> categoryList = new ArrayList<>();
				if(total.isSelected()) {
					for(int i=0;i<buttons.size();i++) {
						buttons.get(i).setVisible(true);
					}
				}
				else {
					if(digitalProduct.isSelected())
						categoryList.add("디지털기기");
					if(lifeDigitalProduct.isSelected())
						categoryList.add("생활가전");
					if(life.isSelected())
						categoryList.add("생활/가공식품");
					if(book.isSelected())
						categoryList.add("도서");
					if(sports.isSelected())
						categoryList.add("스포츠/레저");
					if(cloth.isSelected())
						categoryList.add("패션/잡화");
					if(game.isSelected())
						categoryList.add("게임/취미");
					if(beauty.isSelected())
						categoryList.add("뷰티/미용");
					if(dog.isSelected())
						categoryList.add("반려동물용품");
					if(etc.isSelected())
						categoryList.add("기타");
					
					ArrayList<Integer> categoryProductIdxList = new ArrayList<>();
					categoryProductIdxList = categoryProductIdx(categoryList);
					for(int i=0;i<buttons.size();i++) {
						buttons.get(i).setVisible(false);
					}
					for(int i : categoryProductIdxList) {
						buttons.get(i-1).setVisible(true);
					}
				}
				
			}
		});
		categoryLabel.setBounds(65,30,120,20);
		total.setBounds(30, 80, 120, 20);
		digitalProduct.setBounds(30, 110, 120, 20);
		lifeDigitalProduct.setBounds(30, 140, 120, 20);
		life.setBounds(30, 170, 120, 20);
		book.setBounds(30, 200, 120, 20);
		sports.setBounds(30, 230, 120, 20);
		cloth.setBounds(30, 260, 120, 20);
		game.setBounds(30, 290, 120, 20);
		beauty.setBounds(30, 320, 120, 20);
		dog.setBounds(30, 350, 120, 20);
		etc.setBounds(30, 380, 120, 20);
		categoryButton.setBounds(30, 430, 120, 20);
		
		categoryPanel.add(categoryLabel);
		categoryPanel.add(total);
		categoryPanel.add(digitalProduct);
		categoryPanel.add(lifeDigitalProduct);
		categoryPanel.add(life);
		categoryPanel.add(book);
		categoryPanel.add(sports);
		categoryPanel.add(cloth);
		categoryPanel.add(game);
		categoryPanel.add(beauty);
		categoryPanel.add(dog);
		categoryPanel.add(etc);
		categoryPanel.add(categoryButton);
		
		
		mainPane.add(categoryPanel, BorderLayout.WEST);
		
		mainFrame.add(mainPane);

		
		
	}
	
	public static ArrayList<ArrayList<String>> productDB() {
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();
		
		try {
			con = DriverManager.getConnection(url, user, pwd);
			String SQL = "select * from product;";
			
			PreparedStatement p=con.prepareStatement(SQL);
			p.clearParameters();
			ResultSet r = p.executeQuery();
			
			while(r.next()) {
				ArrayList<String> data = new ArrayList<>();
				//SalerID
				data.add(r.getString(1));
				//name
				data.add(r.getString(2));
				//price
				data.add(r.getInt(3)+"");
				//imagepath
				data.add(r.getString(6));
				
				datas.add(data);
			}
			
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}
	
	public static ArrayList<Integer> searchProductIdx(String searchText){
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;

		ArrayList<Integer> searchProductIdxList = new ArrayList<>();
		
		try {
			con = DriverManager.getConnection(url, user, pwd);
			String SQL = "select productIdx from product where name=?;";
			
			PreparedStatement p=con.prepareStatement(SQL);
			p.clearParameters();
			p.setString(1, searchText);
			ResultSet r = p.executeQuery();
			
			while(r.next()) {
				searchProductIdxList.add(r.getInt(1));
			}
			
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return searchProductIdxList;
	}
	
	public static ArrayList<Integer> categoryProductIdx(ArrayList<String> categoryList){
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;

		ArrayList<Integer> categoryProductIdxList = new ArrayList<>();
		
		try {
			con = DriverManager.getConnection(url, user, pwd);
			String SQL = "select productIdx from product where category=?;";
			
			for(String i : categoryList) {
				PreparedStatement p=con.prepareStatement(SQL);
				p.clearParameters();
				p.setString(1, i);
				ResultSet r = p.executeQuery();
				
				while(r.next()) {
					categoryProductIdxList.add(r.getInt(1));
				}
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return categoryProductIdxList;
	}
}

class myActionListener implements ActionListener{
	int index;
	public myActionListener(int index) {
		this.index = index;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//System.out.println(index);
		//product.productPane.setVisible(true);
		Main.mainPane.setVisible(false);
		product.product(index);
	}
	
}
