package code;

import java.awt.Dimension;
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
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class registeredItem {
	public static void registeredItem() {
		JLayeredPane registeredItemPane = new JLayeredPane();
		registeredItemPane.setVisible(true);
		
		registeredItemPane.setBounds(0, 0, 1500, 1500);
		Main.mainFrame.add(registeredItemPane);
		//제품 버튼
		
		JPanel productPanel = new JPanel();
		productPanel.setPreferredSize(new Dimension(1000,5000));
		
		ArrayList<JButton> img_buttons = new ArrayList<>();
		ArrayList<JButton> content_buttons = new ArrayList<>();
		
		String currentSaler=null;
		String[] currentUserRegisteredItem = null;
		if(!(getCurrentUser().isEmpty())) {
			currentSaler = getCurrentUser().get(0);
			currentUserRegisteredItem = getCurrentUserRegisteredItem(currentSaler);
		}
//		//SalerID
//		data.add(r.getString(1));
//		//name
//		data.add(r.getString(2));
//		//price
//		data.add(r.getInt(3)+"");
//		//quantity
//		data.add(r.getInt(4)+"");
//		//category
//		data.add(r.getString(5));					
//		//imagepath
//		data.add(r.getString(6));
//		//cotnent
//		data.add(r.getString(7));
		
		for(int i=0;i<productDB(currentUserRegisteredItem).size();i++) {
			String salerId = productDB(currentUserRegisteredItem).get(i).get(0);
			String name = productDB(currentUserRegisteredItem).get(i).get(1);
			String price = productDB(currentUserRegisteredItem).get(i).get(2);
			String quantity = productDB(currentUserRegisteredItem).get(i).get(3);
			String category = productDB(currentUserRegisteredItem).get(i).get(4);
			String imagePath = productDB(currentUserRegisteredItem).get(i).get(5);
			String content = productDB(currentUserRegisteredItem).get(i).get(6);
			
			ImageIcon img = new ImageIcon(imagePath);
			Image IconToImg = img.getImage();
			Image setSizeImg = IconToImg.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
			ImageIcon setImgIcon = new ImageIcon(setSizeImg);
			
			JButton img_button = new JButton(setImgIcon);
			img_buttons.add(img_button);
			
			JButton content_button = new JButton("<html>"+"제품명 : "+name+"\n가격 : "+price+"\n수량 : "+quantity+"\n카테고리 : "+category+"\n설명글 : "+content+"</html>");
			content_buttons.add(content_button);
			
	
			//버튼 사이즈 설정 
			img_buttons.get(i).setPreferredSize(new Dimension(200,200));
			//패널에 버튼 추가 
			productPanel.add(img_buttons.get(i));
			
			//버튼 사이즈 설정 
			content_buttons.get(i).setPreferredSize(new Dimension(700,200));
			//패널에 버튼 추가 
			productPanel.add(content_buttons.get(i));
			
	
		}
		JScrollPane scroll = new JScrollPane();
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setViewportView(productPanel);
	
		scroll.setBounds(210, 100, 1080, 680);
		//mainFrame.add(scroll);
		registeredItemPane.add(scroll);
		
		JPanel gobackPanel = new JPanel();
		gobackPanel.setBounds(30, 30, 50, 100);
		JButton gobackButton= new JButton("돌아가기");
		gobackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//돌아가기 버튼 클릭 시 반응
				//mypage.mypagePane.setVisible(true);
				mypage.mypage();
				registeredItemPane.setVisible(false);
				
			}
		});
		
		gobackPanel.add(gobackButton);
		registeredItemPane.add(gobackPanel);
	}
	
	public static ArrayList<ArrayList<String>> productDB(String[] currentUserRegisteredItem) {
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();
		
		try {
			con = DriverManager.getConnection(url, user, pwd);
			String SQL = "select * from product where productIdx=?;";
			
			PreparedStatement p=con.prepareStatement(SQL);
			p.clearParameters();

			
			for(int i=1 ;i<currentUserRegisteredItem.length;i++) {
				p.setInt(1,Integer.parseInt(currentUserRegisteredItem[i]));
				ResultSet r = p.executeQuery();
			
			
				while(r.next()) {
					ArrayList<String> data = new ArrayList<>();
					//SalerID
					data.add(r.getString(1));
					//name
					data.add(r.getString(2));
					//price
					data.add(r.getInt(3)+"");
					//quantity
					data.add(r.getInt(4)+"");
					//category
					data.add(r.getString(5));					
					//imagepath
					data.add(r.getString(6));
					//cotnent
					data.add(r.getString(7));
					
					datas.add(data);
				}
				
			}
			
			con.close();
		} catch (SQLException e) {
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
	public static String[] getCurrentUserRegisteredItem(String currentSaler){
		String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
		String user = "root";
		String pwd = "*sja23022302";
		Connection con = null;
		String String_ProductIdx=null;
		String[] ProductIdxList = null;
		
		try {
			con = DriverManager.getConnection(url, user, pwd);
			String SQL = "select ProductIdx from saler where id = ?;";

			
			PreparedStatement p=con.prepareStatement(SQL);
			p.clearParameters();
			p.setString(1, currentSaler);

			ResultSet r = p.executeQuery();
			
			while(r.next()) {
				String_ProductIdx = r.getString(1);
				break;
			}
			
			
			con.close();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ProductIdxList = String_ProductIdx.split(",");
		return ProductIdxList;
	}
}
