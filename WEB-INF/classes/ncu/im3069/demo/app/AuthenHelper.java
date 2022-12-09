package ncu.im3069.demo.app;

import java.sql.*;

import java.util.*;
import java.time.LocalDateTime;
import org.json.*;

import ncu.im3069.demo.util.DBMgr;

public class AuthenHelper {

    private AuthenHelper() {
        
    }
    
    /** 靜態變數，儲存MemberHelper物件 */
    private static AuthenHelper mh;
    
    /** 儲存JDBC資料庫連線 */
    private Connection conn = null;
    
    /** 儲存JDBC預準備之SQL指令 */
    private PreparedStatement pres = null;
    
    /**
     * 靜態方法<br>
     * 實作Singleton（單例模式），僅允許建立一個MemberHelper物件
     *
     * @return the helper 回傳MemberHelper物件
     */
    public static AuthenHelper getHelper() {
        /** Singleton檢查是否已經有MemberHelper物件，若無則new一個，若有則直接回傳 */
        if(mh == null) mh = new AuthenHelper();
        
        return mh;
    }

    public JSONObject getchecked(String raccount, String rpassword) {
    	/** 紀錄SQL總行數，若為「-1」代表資料庫檢索尚未完成 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        int flag = 0, type = 0; // 0 = not found, 1 = member, 2 = admin
        int id = 0;
        if(flag == 0) {
        	try {
                /** 取得資料庫之連線 */
                conn = DBMgr.getConnection();
                /** SQL指令 */
                String sql = "SELECT member_id, status, count(*) FROM `missa`.`member` WHERE `account` = ? AND `password` = ? LIMIT 1";
                
                /** 將參數回填至SQL指令當中 */
                pres = conn.prepareStatement(sql);
                pres.setString(1, raccount);
                pres.setString(2, rpassword);
                /** 執行查詢之SQL指令並記錄其回傳之資料 */
                rs = pres.executeQuery();

                /** 讓指標移往最後一列，取得目前有幾行在資料庫內 */
                rs.next();
                row = rs.getInt("count(*)");
                System.out.print(row);
                
                if(row == 0) {
                	flag = 1;
                }
                else{
                    type = 1;
                    id = rs.getInt("member_id");
                    System.out.println(id);
                    if(rs.getString("status").equals("n")) {
                    	type = 3;
                    }
                }

            } catch (SQLException e) {
                /** 印出JDBC SQL指令錯誤 **/
                System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                /** 若錯誤則印出錯誤訊息 */
                e.printStackTrace();
            }
        }
        if(flag == 1) {
        	try {
            	/** 取得資料庫之連線 */
                conn = DBMgr.getConnection();
                /** SQL指令 */
                String sql1 = "SELECT admin_id, count(*) FROM `missa`.`admin` WHERE `account` = ? AND `password` = ? LIMIT 1";
                
                /** 將參數回填至SQL指令當中 */
                pres = conn.prepareStatement(sql1);
                pres.setString(1, raccount);
                pres.setString(2, rpassword);
                /** 執行查詢之SQL指令並記錄其回傳之資料 */
                rs = pres.executeQuery();

                /** 讓指標移往最後一列，取得目前有幾行在資料庫內 */
                rs.next();
                row = rs.getInt("count(*)");
                System.out.print(row);
                if(row != 0){
                    type = 2;
                    id = rs.getInt("admin_id");
                    System.out.println(id);
                }
            } catch (SQLException e) {
                /** 印出JDBC SQL指令錯誤 **/
                System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                /** 若錯誤則印出錯誤訊息 */
                e.printStackTrace();
            }
        }
        
        /** 關閉連線並釋放所有資料庫相關之資源 **/
        DBMgr.close(rs, pres, conn);
        
        JSONObject response = new JSONObject();
        response.put("type", type);
        response.put("id", id);

        return response;
    }

    public JSONObject checkDuplicate(String email){
    	System.out.println("hello!");
    	/** 紀錄SQL總行數，若為「-1」代表資料庫檢索尚未完成 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        int flag = 0;
        int id = 0;
        if(flag == 0) {
        	try {
                /** 取得資料庫之連線 */
                conn = DBMgr.getConnection();
                /** SQL指令 */
                String sql = "SELECT member_id, count(*) FROM `missa`.`member` WHERE `email` = ? LIMIT 1";
                
                /** 將參數回填至SQL指令當中 */
                pres = conn.prepareStatement(sql);
                pres.setString(1, email);
                /** 執行查詢之SQL指令並記錄其回傳之資料 */
                rs = pres.executeQuery();

                /** 讓指標移往最後一列，取得目前有幾行在資料庫內 */
                rs.next();
                row = rs.getInt("count(*)");
                System.out.print(row);
                
                if(row == 0) {
                	flag = 1;
                }
                else{
                    id = rs.getInt("member_id");
                    System.out.println(id);
                }

            } catch (SQLException e) {
                /** 印出JDBC SQL指令錯誤 **/
                System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                /** 若錯誤則印出錯誤訊息 */
                e.printStackTrace();
            }
        }
        if(flag == 1) {
        	try {
            	/** 取得資料庫之連線 */
                conn = DBMgr.getConnection();
                /** SQL指令 */
                String sql1 = "SELECT admin_id, count(*) FROM `missa`.`admin` WHERE `email` = ? LIMIT 1";
                
                /** 將參數回填至SQL指令當中 */
                pres = conn.prepareStatement(sql1);
                pres.setString(1, email);
                /** 執行查詢之SQL指令並記錄其回傳之資料 */
                rs = pres.executeQuery();

                /** 讓指標移往最後一列，取得目前有幾行在資料庫內 */
                rs.next();
                row = rs.getInt("count(*)");
                System.out.print(row);
                if(row != 0){
                    id = rs.getInt("admin_id");
                    System.out.println(id);
                }
            } catch (SQLException e) {
                /** 印出JDBC SQL指令錯誤 **/
                System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                /** 若錯誤則印出錯誤訊息 */
                e.printStackTrace();
            }
        }
        
        /** 關閉連線並釋放所有資料庫相關之資源 **/
        DBMgr.close(rs, pres, conn);
        
        JSONObject response = new JSONObject();
        response.put("id", id);
        response.put("row", row);

        return response;
    }
}
