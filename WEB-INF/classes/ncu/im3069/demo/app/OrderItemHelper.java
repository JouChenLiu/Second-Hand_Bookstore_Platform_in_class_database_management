package ncu.im3069.demo.app;

import java.sql.*;
import java.util.*;
import java.util.ArrayList;

import org.json.*;

import ncu.im3069.demo.util.DBMgr;

public class OrderItemHelper {
    
    private OrderItemHelper() {
        
    }
    
    private static OrderItemHelper oph;
    private Connection conn = null;
    private PreparedStatement pres = null;
    
    /**
     * 靜態方法<br>
     * 實作Singleton（單例模式），僅允許建立一個MemberHelper物件
     *
     * @return the helper 回傳MemberHelper物件
     */
    public static OrderItemHelper getHelper() {
        /** Singleton檢查是否已經有MemberHelper物件，若無則new一個，若有則直接回傳 */
        if(oph == null) oph = new OrderItemHelper();
        
        return oph;
    }
    
    public JSONArray createByList(int order_id, List<OrderItem> orderproduct) {
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        
        for(int i=0 ; i < orderproduct.size() ; i++) {
            OrderItem op = orderproduct.get(i);
            
            /** 取得所需之參數 */
            int product_id = op.getProduct().getProductID();
            int product_price = op.getProductPrice();
            int product_num = op.getProductNum();
            int customer_id = op.getCustomerId();
            int supplier_id = op.getSupplierId();
            Timestamp created_at = op.getCreatedAt();
            // double subtotal = op.getSubTotal();
            
            try {
                /** 取得資料庫之連線 */
                conn = DBMgr.getConnection();
                /** SQL指令 */
                String sql = "INSERT INTO `missa`.`order_details`(`order_id`, `product_id`,"
                        + " `product_price`, `product_num`, `customer_id`, `supplier_id`,"
                        + " `created_at`)"
                        + " VALUES(?, ?, ?, ?, ?, ?, ?)";
                
                /** 將參數回填至SQL指令當中 */
                pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pres.setInt(1, order_id);
                pres.setInt(2, product_id);
                pres.setInt(3, product_price);
                pres.setInt(4, product_num);
                pres.setInt(5, customer_id);
                pres.setInt(6, supplier_id);
                pres.setTimestamp(7, created_at);
                // pres.setDouble(5, subtotal);
                /** 執行新增之SQL指令並記錄影響之行數 */
                pres.executeUpdate();
                
                /** 紀錄真實執行的SQL指令，並印出 **/
                exexcute_sql = pres.toString();
                System.out.println(exexcute_sql);
                
                ResultSet rs = pres.getGeneratedKeys();

                if (rs.next()) {
                    long id = rs.getLong(1);
                    jsa.put(id);
                }
            } catch (SQLException e) {
                /** 印出JDBC SQL指令錯誤 **/
                System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                /** 若錯誤則印出錯誤訊息 */
                e.printStackTrace();
            } finally {
                /** 關閉連線並釋放所有資料庫相關之資源 **/
                DBMgr.close(pres, conn);
            }
        }
        
        return jsa;
    }
    
    public JSONObject getOrderProductByCustomerId(String customer_id) {
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        ResultSet rs = null;
        OrderItem op = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`order_details` WHERE `order_details`.`customer_id` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, customer_id);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            rs = pres.executeQuery();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
            	row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int order_id = rs.getInt("order_id");
                Timestamp created_at = rs.getTimestamp("created_at");
                double subtotal = rs.getDouble("subtotal");
                int status = rs.getInt("status");
                
                /** 將每一筆會員資料產生一名新Member物件 */
                op = new OrderItem(order_id, created_at, subtotal);
                // op = new OrderItem(order_details_id, order_id, product_id, product_price, product_num);//, subtotal);
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
                jsa.put(op.getData());
            }
        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);
        System.out.println(jsa);
        return response;
    }

    public JSONObject getOrderProductBySupplierId(String supplier_id) {
    	JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        ResultSet rs = null;
        OrderItem op = null;
        int [] array = {0};
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`order_details` WHERE `order_details`.`supplier_id` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, supplier_id);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            rs = pres.executeQuery();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                
                /** 將 ResultSet 之資料取出 */
                int order_id = rs.getInt("order_id");
                Timestamp created_at = rs.getTimestamp("created_at");
                double subtotal = rs.getDouble("subtotal");
                //int status = rs.getInt("status");
                
                /** 將每一筆會員資料產生一名新Member物件 */
                op = new OrderItem(order_id, created_at, subtotal);
                // op = new OrderItem(order_details_id, order_id, product_id, product_price, product_num);//, subtotal);
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
                jsa.put(op.getOrderData());
                
            }
        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);
        System.out.println(jsa);
        return response;
    }
    
    public JSONObject getSupplierOrderId(String supplier_id) {
    	JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        ResultSet rs = null;
        OrderItem op = null;
        int orderIds[] = new int[10];
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT `order_id` FROM `missa`.`order_details` WHERE `order_details`.`supplier_id` = ? GROUP BY `order_id" ;
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, supplier_id);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            rs = pres.executeQuery();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
            	//orderIds.add(rs.getInt("order_id"));
                /** 將 ResultSet 之資料取出 */
                int order_id = rs.getInt("order_id");
                Timestamp created_at = rs.getTimestamp("created_at");
                double subtotal = rs.getDouble("subtotal");
                //int status = rs.getInt("status");
                
                /** 將每一筆會員資料產生一名新Member物件 */
                op = new OrderItem(order_id, created_at, subtotal);
                // op = new OrderItem(order_details_id, order_id, product_id, product_price, product_num);//, subtotal);
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
                jsa.put(op.getOrderData());
                
            }
        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);
        System.out.println(jsa);
        return response;
    }

    public ArrayList<OrderItem> getOrderProductByOrderId(int order_id) {
        ArrayList<OrderItem> result = new ArrayList<OrderItem>();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        ResultSet rs = null;
        OrderItem op;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`order_details` WHERE `order_details`.`order_id` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, order_id);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            rs = pres.executeQuery();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                
                /** 將 ResultSet 之資料取出 */
                int order_details_id = rs.getInt("order_details_id");
                int product_id = rs.getInt("product_id");
                int product_price = rs.getInt("product_price");
                int product_num = rs.getInt("product_num");
                int customer_id = rs.getInt("customer_id");
                int supplier_id = rs.getInt("supplier_id");
                Timestamp created_at = rs.getTimestamp("created_at");
                // double subtotal = rs.getDouble("subtotal");
                
                /** 將每一筆會員資料產生一名新Member物件 */
                op = new OrderItem(order_details_id, order_id, product_id, product_price, product_num, 
                                    customer_id, supplier_id, created_at);
                // op = new OrderItem(order_details_id, order_id, product_id, product_price, product_num);//, subtotal);
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
                result.add(op);
            }
        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }
        
        return result;
    }
}
