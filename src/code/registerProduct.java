package code;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class registerProduct {
	static JLayeredPane registerProductPane = new JLayeredPane();
	public static void registerProduct() {
		registerProductPane.setBounds(0, 0, 1500, 1500);
		Main.mainFrame.add(registerProductPane);
		
		JPanel registerProductPanel = new JPanel();
		registerProductPanel.setBounds(400,150,700,600);
		registerProductPanel.setBorder(new TitledBorder(new LineBorder(Color.black)));
		registerProductPanel.setLayout(null);
		
		JLabel nameLabel = new JLabel("제품명 ");
		JTextField nameField = new JTextField(20);

		JLabel priceLabel = new JLabel("가격 ");
		JTextField priceField = new JTextField(20);
		
		JLabel quantityLabel = new JLabel("수량 ");
		JTextField quantityField = new JTextField(20);
		
		JLabel categoryLabel = new JLabel("카테고리 ");
		String categoryOption[]= {"디지털기기","생활가전","생활/가공식품","도서","스포츠/레저","패션/잡화","게임/취미","뷰티/미용","반려동물용품","기타"};
		JComboBox categorycomboBox=new JComboBox<String>(categoryOption);
		
		JLabel imageLabel = new JLabel("이미지 ");
		//JTextField imageField = new JTextField(100);
		JButton imageButton = new JButton("이미지 등록");
		imageButton.addActionListener(new OpenActionListener());
		
		JLabel contentLabel = new JLabel("설명 ");
		JTextField contentField = new JTextField(500);
		
		JButton registerButton = new JButton("제품등록");
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//제품등록 버튼 클릭 시 반응
				
				String salerId = findSalerId();
				String name = nameField.getText();
				int price = Integer.parseInt(priceField.getText());
				int quantity = Integer.parseInt(quantityField.getText());
				String category = categorycomboBox.getSelectedItem().toString();
				String imagePath = OpenActionListener.filePath;
				String content = contentField.getText();
				int productIdx = getLastProductIdx()+1;
				
				insertIntoProductDB(salerId,name,price,quantity,category,imagePath,content,productIdx);
				//updateLastProductIdx(getLastProductIdx()+1);
				updateProductIdx(productIdx,salerId);
				
				//mypage.mypagePane.setVisible(true);
				mypage.mypage();
				registerProductPane.setVisible(false);
			}
		});
		
		nameLabel.setBounds(50,10,70,20);
		nameField.setBounds(130,10,520,20);
		priceLabel.setBounds(50,40,70,20);
		priceField.setBounds(130,40,520,20);
		quantityLabel.setBounds(50,70,70,20);
		quantityField.setBounds(130,70,520,20);
		categoryLabel.setBounds(50,100,70,20);
		categorycomboBox.setBounds(130,100,520,20);
		imageLabel.setBounds(50,130,70,20);
		imageButton.setBounds(130,130,520,20);
		contentLabel.setBounds(50,160,70,20);
		contentField.setBounds(130,160,520,300);
		registerButton.setBounds(570, 470, 80, 20);
		
		registerProductPanel.add(nameLabel);
		registerProductPanel.add(nameField);
		registerProductPanel.add(priceLabel);
		registerProductPanel.add(priceField);
		registerProductPanel.add(quantityLabel);
		registerProductPanel.add(quantityField);
		registerProductPanel.add(categoryLabel);
		registerProductPanel.add(categorycomboBox);
		registerProductPanel.add(imageLabel);
		registerProductPanel.add(imageButton);
		registerProductPanel.add(contentLabel);
		registerProductPanel.add(contentField);
		registerProductPanel.add(registerButton);
		
		registerProductPane.add(registerProductPanel);

		JPanel gobackPanel = new JPanel();
		gobackPanel.setBounds(30, 30, 50, 100);
		JButton gobackButton= new JButton("돌아가기");
		gobackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//돌아가기 버튼 클릭 시 반응
				//mypage.mypagePane.setVisible(true);
				mypage.mypage();
				registerProductPane.setVisible(false);
				
			}
		});
		
		gobackPanel.add(gobackButton);
		registerProductPane.add(gobackPanel);
	}
	
	
	public static String findSalerId() {
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		String salerId = null;
		try {
			con = DriverManager.getConnection(url, user, pwd);
			String SQL = "select id from currentUser;";
			
			PreparedStatement p=con.prepareStatement(SQL);
			p.clearParameters();
			//p.setString(1, type);

			ResultSet r = p.executeQuery();
			
			while(r.next()) {
				salerId = r.getString(1);
				break;
			}
			
			con.close();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return salerId;
	}
	
	public static int getLastProductIdx() {
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		int lastProductIdx = 0;
		try {
			con = DriverManager.getConnection(url, user, pwd);
			String SQL = "select lastProduct from lastProduct;";
			
			PreparedStatement p=con.prepareStatement(SQL);
			p.clearParameters();
			//p.setString(1, type);

			ResultSet r = p.executeQuery();
			
			while(r.next()) {
				lastProductIdx = r.getInt(1);
				break;
			}
			
			con.close();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lastProductIdx;
	}
	
	public static void insertIntoProductDB(String salerId,String name,int price,int quantity,String category,String imagePath,String content,int productIdx) {
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, user, pwd);
			String SQL = "insert into product values(?,?,?,?,?,?,?,?);";

			PreparedStatement p=con.prepareStatement(SQL);
			p.clearParameters();
			
			p.setString(1, salerId);
			p.setString(2, name);
			p.setInt(3, price);
			p.setInt(4, quantity);
			p.setString(5, category);
			p.setString(6, imagePath);
			p.setString(7, content);
			p.setInt(8, productIdx);
			
			p.executeUpdate();
			
			//insert에 성공했으면 lastProductIdx +1;
			updateLastProductIdx(getLastProductIdx()+1);
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void updateLastProductIdx(int currentProductIdx) {
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		int lastProductIdx = 0;
		try {
			con = DriverManager.getConnection(url, user, pwd);
			String SQL = "update lastProduct set lastProduct=?;";
			
			PreparedStatement p=con.prepareStatement(SQL);
			p.clearParameters();
			
			p.setInt(1, currentProductIdx);
			
			p.executeUpdate();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void updateProductIdx(int productIdx,String salerId) {
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		String currentProductIdx = "";
		String newProductIdx = "";
		try {
			con = DriverManager.getConnection(url, user, pwd);
			//----------현재 등록한 상품 idx 구하기------------
			String SQL1 = "select ProductIdx from Saler where id=?;";
			
			PreparedStatement p1=con.prepareStatement(SQL1);
			p1.clearParameters();
			p1.setString(1, salerId);

			ResultSet r1 = p1.executeQuery();
			
			while(r1.next()) {
				currentProductIdx = r1.getString(1);
				break;
			}
			newProductIdx = currentProductIdx+","+productIdx;
			
			
			//----------------saler update--------------------
			String updateSQL1 = "update saler set ProductIdx=? where id=?";
			
			PreparedStatement update_p1 = con.prepareStatement(updateSQL1);
			update_p1.clearParameters();
			update_p1.setString(1,newProductIdx);
			update_p1.setString(2, salerId);
			
			update_p1.executeUpdate();
		}
			

		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

class OpenActionListener implements ActionListener{
	JFileChooser chooser;
	static String filePath;
	OpenActionListener(){
		chooser = new JFileChooser();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"JPG & GIF & PNG images",
				"jpg","gif","png"
		);
		chooser.setFileFilter(filter);
		
		int ret = chooser.showOpenDialog(null);
		if(ret != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다.","경고",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		filePath = chooser.getSelectedFile().getPath();
		//System.out.println(filePath);
	}
	
}
