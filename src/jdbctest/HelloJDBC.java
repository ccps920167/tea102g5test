package jdbctest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class HelloJDBC {
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe"; // 宣告一個常數為資料庫位置
	private static final String USER = "DAVID";
	private static final String PASSWORD = "123456";

	private static final String SQL = "INSERT INTO DEPARTMENT (DEPTNO, DNAME, LOC )VALUES (?,?,?)"; //為防範注入攻擊，改採用?PrepareStatement方案
//	private static final String SQL = "INSERT INTO DEPARTMENT (DEPTNO, DNAME, LOC )VALUES (50,'總務部','南京東路')";
//	private static final String SQL = "SELECT * FROM DEPARTMENT";

	public static void main(String[] args) {

		Connection con = null; // 為了讓底下的例外處理也能使用 外面要宣告，但一定要給初始值後面才能用if判斷(if是嚴謹的寫法避免例外堆疊)
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 模擬輸入要新增的部門資料
		Scanner sc = new Scanner(System.in);
		System.out.println("請輸入部門編號");
		int deptno = sc.nextInt();
		System.out.println("請輸入部門名稱");
		String dname = sc.next();
		System.out.println("請輸入所在地");
		String loc = sc.next();

		sc.close();

		try {
			// STEP 1: 載入驅動
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("載入成功");

			// STEP 2: 建立連線
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("連線成功");

//			// STEP 3:送出SQL指令
			pstmt = con.prepareStatement(SQL); // 預先將資料交給資料庫 //注意!!方法的prepare沒有d
			// 對每個問號設定對應的值(從scanner取得要新增的部門相關資料)
			pstmt.setInt(1, deptno);
			pstmt.setString(2, dname);
			pstmt.setString(3, loc);
			
			pstmt.executeUpdate();

//			stmt = con.createStatement();
//			int count = stmt.executeUpdate(SQL); // 確認是否回傳 所以設定回傳一個整數 宣告int count
//			System.out.println("更新" + count + "筆資料");

//			stmt = con.createStatement();
//			rs = stmt.executeQuery(SQL);
//			System.out.println("開始位置"+(rs.getRow()));
//			while (rs.next()) {
//				int deptno = rs.getInt("DEPTNO");//可換成1(第一個)
//				String dname = rs.getString("DNAME");
//				String loc = rs.getString("LOC");
//				
//				System.out.println("DEPNO ="+ deptno);
//				System.out.println("DNAME ="+ dname);
//				System.out.println("LOC ="+ loc);
//				System.out.println("=================================");
//			}

		} catch (ClassNotFoundException ce) {
			ce.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			if (pstmt != null) { // 後建立的先關閉，指令相同
				try {
					pstmt.close();
				} catch (SQLException se) {
				}
			}
			if (rs != null) { // 後建立的先關閉，指令相同
				try {
					rs.close();
				} catch (SQLException se) {
				}
			}

			if (stmt != null) { // 後建立的先關閉，指令相同
				try {
					stmt.close();
				} catch (SQLException se) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
		}
	}
}
