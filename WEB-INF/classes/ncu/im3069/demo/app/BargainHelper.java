package ncu.im3069.demo.app;

import java.sql.*;
import java.util.*;

import org.json.*;

import ncu.im3069.demo.util.DBMgr;

public class BargainHelper {
    
    private static BargainHelper bh;
    private Connection conn = null;
    private PreparedStatement pres = null;
    //private orderItemHelper oph =  orderItemHelper.getHelper();
    
    private BargainHelper() {
    }
    
    public static BargainHelper getHelper() {
        if(bh == null) bh = new BargainHelper();
        
        return bh;
    }
    
    //買家提出議價請求
    public JSONObject create(Bargain bargain) {
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        long id = -1;
        JSONArray ba = new JSONArray();
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "INSERT INTO `missa`.`bargain`(`customer_id`, `supplier_id`, `product_id`, `upper_limit`, `lower_limit`, `final_price`, `is_rejected`, `created_at`)"
                    + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            
            /** 取得所需之參數 */
            int customer_id = bargain.getCustomerId();
            int supplier_id = bargain.getSupplierId();
            int product_id = bargain.getProductId(); 
            int upper_limit = bargain.getUpper();                
            int lower_limit = bargain.getLower();
            int final_price = bargain.getFinalPrice();
            char is_rejected = bargain.getIsRejected();
            Timestamp create_time = bargain.getCreateTime();

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pres.setInt(1, customer_id);
            pres.setInt(2, supplier_id);
            pres.setInt(3, product_id);
            pres.setInt(4, upper_limit);
            pres.setInt(5, lower_limit);
            pres.setInt(6, final_price);
            pres.setString(7, String.valueOf(is_rejected));
            pres.setTimestamp(8, create_time);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            pres.executeUpdate();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            //ResultSet rs = pres.getGeneratedKeys();
            
            
            // if (rs.next()) {
            //     id = rs.getLong(1);
            //     ArrayList<bargainItem> opd = bargain.getbargainProduct();
            //     ba = oph.createByList(id, opd);
            // }
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


        // /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("bargain_id", id);
        response.put("bargain_product_id", ba);

        return response;
    }
    
    //管理員查看所有議價請求
    public JSONObject getAll() {
        Bargain bg = null;
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`bargain`";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int bargain_id = rs.getInt("bargain_id");
                int customer_id = rs.getInt("customer_id");
                int supplier_id = rs.getInt("supplier_id");
                int product_id = rs.getInt("product_id");
                int upper_limit = rs.getInt("upper_limit");
                int lower_limit = rs.getInt("lower_limit");
                int final_price = rs.getInt("final_price");
                char is_rejected = rs.getString("is_rejected").charAt(0);
                Timestamp create_time = rs.getTimestamp("created_at");

                /** 將每一筆商品資料產生一名新Bargain物件 */
                bg = new Bargain(bargain_id, customer_id, supplier_id, product_id, upper_limit, lower_limit, final_price, is_rejected, create_time);
           
                // /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                // jsa.put(bg.getbargainAllInfo());
                jsa.put(bg.getData());
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

        return response;
    }
    
    //買家查看自己的所有議價請求
    public JSONObject getByCustomerId(String customer_id) {
        JSONArray jsa = new JSONArray();
        Bargain bg = null;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`bargain` WHERE `customer_id` = ?";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, customer_id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int bargain_id = rs.getInt("bargain_id");
                int supplier_id = rs.getInt("supplier_id");
                int product_id = rs.getInt("product_id");
                int upper_limit = rs.getInt("upper_limit");
                int lower_limit = rs.getInt("lower_limit");
                int final_price = rs.getInt("final_price");
                char is_rejected = rs.getString("is_rejected").charAt(0);
                Timestamp create_time = rs.getTimestamp("created_at");
                
//                String test = rs.getString("is_rejected");
//                System.out.print("here");
//                System.out.print(test.charAt(0));
                
                /** 將每一筆商品資料產生一名新Product物件 */
                int ci = Integer.valueOf(customer_id);
                bg = new Bargain(bargain_id, ci, supplier_id, product_id, upper_limit, lower_limit, final_price, is_rejected, create_time);

                jsa.put(bg.getData());
                // /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                // data = bg.getbargainAllInfo();
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
        System.out.print(jsa);
        return response;
    }
    
    //賣家查看該商品議價請求
    public Bargain getByCIandPI(String customer_id, String product_id) {
    	//JSONArray jsa = new JSONArray();
        Bargain bg = null;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`bargain` WHERE `customer_id` = ? AND `product_id` = ?";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, customer_id);
            pres.setString(2, product_id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int bargain_id = rs.getInt("bargain_id");
                int supplier_id = rs.getInt("supplier_id");
                int upper_limit = rs.getInt("upper_limit");
                int lower_limit = rs.getInt("lower_limit");
                int final_price = rs.getInt("final_price");
                char is_rejected = rs.getString("is_rejected").charAt(0);
                Timestamp create_time = rs.getTimestamp("created_at");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                int ci = Integer.valueOf(customer_id);
                int pi = Integer.valueOf(product_id);
                bg = new Bargain(bargain_id, ci, supplier_id, pi, upper_limit, lower_limit, final_price, is_rejected, create_time);
                
                //jsa.put(bg.getData());
                // /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                // data = bg.getbargainAllInfo();
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
        
        return bg;
    }
    
    //檢查是否已經議價過
    public boolean checkByCIandPI(String customer_id, String product_id) {
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";

        /** 紀錄SQL總行數 */
        int row = 0;
        boolean isDuplicated = false;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT count(*) FROM `missa`.`bargain` WHERE `customer_id` = ? AND `product_id` = ?";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, customer_id);
            pres.setString(2, product_id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            //System.out.print(row);
            rs.next();
            row = rs.getInt("count(*)");
            //System.out.print(row);
            
            if(row>0) {
            	isDuplicated = true;
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
        //System.out.print(isDuplicated);
        return isDuplicated;
    }
    
    //賣家查看所有議價請求
    public JSONObject getBySupplierId(String supplier_id) {
    	JSONArray jsa = new JSONArray();
        Bargain bg = null;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`bargain` WHERE `supplier_id` = ?";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, supplier_id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int bargain_id = rs.getInt("bargain_id");
                int customer_id = rs.getInt("customer_id");
                int product_id = rs.getInt("product_id");
                int upper_limit = rs.getInt("upper_limit");
                int lower_limit = rs.getInt("lower_limit");
                int final_price = rs.getInt("final_price");
                char is_rejected = rs.getString("is_rejected").charAt(0);
                Timestamp create_time = rs.getTimestamp("created_at");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                int si = Integer.valueOf(supplier_id);
                bg = new Bargain(bargain_id, customer_id, si, product_id, upper_limit, lower_limit, final_price, is_rejected, create_time);
                
                jsa.put(bg.getData());
                // /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                // data = bg.getbargainAllInfo();
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

        return response;
    }

    //賣家查看該商品議價請求
    public JSONObject getBySIandPI(String supplier_id, String product_id) {
    	JSONArray jsa = new JSONArray();
        Bargain bg = null;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`bargain` WHERE `supplier_id` = ? AND `product_id` = ?";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, supplier_id);
            pres.setString(2, product_id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int bargain_id = rs.getInt("bargain_id");
                int customer_id = rs.getInt("customer_id");
                int upper_limit = rs.getInt("upper_limit");
                int lower_limit = rs.getInt("lower_limit");
                int final_price = rs.getInt("final_price");
                char is_rejected = rs.getString("is_rejected").charAt(0);
                Timestamp create_time = rs.getTimestamp("created_at");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                int si = Integer.valueOf(supplier_id);
                int pi = Integer.valueOf(product_id);
                bg = new Bargain(bargain_id, customer_id, si, pi, upper_limit, lower_limit, final_price, is_rejected, create_time);
                
                jsa.put(bg.getData());
                // /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                // data = bg.getbargainAllInfo();
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

        return response;
    }
    
    // 透過議價編號取
    public Bargain getById(String bargain_id) {
        //JSONObject data = new JSONObject();
        Bargain bg = null;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`bargain` WHERE `bargain`.`bargain_id` = ?";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, bargain_id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int customer_id = rs.getInt("customer_id");
                int supplier_id = rs.getInt("supplier_id");
                int product_id = rs.getInt("product_id");
                int upper_limit = rs.getInt("upper_limit");
                int lower_limit = rs.getInt("lower_limit");
                int final_price = rs.getInt("final_price");
                char is_rejected = rs.getString("is_rejected").charAt(0);
                Timestamp create_time = rs.getTimestamp("created_at");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                int bi = Integer.valueOf(bargain_id);
                bg = new Bargain(bi, customer_id, supplier_id, product_id, upper_limit, lower_limit, final_price, is_rejected, create_time);
                
                // /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                // data = bg.getbargainAllInfo();
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
        
        return bg;
//        /** 紀錄程式結束執行時間 */
//        long end_time = System.nanoTime();
//        /** 紀錄程式執行時間 */
//        long duration = (end_time - start_time);
//        
//        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
//        
//        JSONObject response = new JSONObject();
//        response.put("sql", exexcute_sql);
//        response.put("row", row);
//        response.put("time", duration);
//        response.put("data", data);
//
//        return response;
    }

	// 賣家回應議價，更新資料庫
	public JSONObject update(Bargain bg) {
	    /** 紀錄回傳之資料 */
	    JSONArray jsa = new JSONArray();
	    /** 記錄實際執行之SQL指令 */
	    String exexcute_sql = "";
	    /** 紀錄程式開始執行時間 */
	    long start_time = System.nanoTime();
	    /** 紀錄SQL總行數 */
	    int row = 0;
	    
	    try {
	        /** 取得資料庫之連線 */
	        conn = DBMgr.getConnection();
	        /** SQL指令 */
	        String sql = "Update `missa`.`bargain` SET `final_price` = ? ,`is_rejected` = ? WHERE `customer_id` = ? AND `product_id` = ?";
	        /** 取得所需之參數 */
	        int final_price = bg.getFinalPrice();
	        char is_rejected = bg.getIsRejected();
	        int customer_id = bg.getCustomerId();
	        int product_id = bg.getProductId();
	        /** 將參數回填至SQL指令當中 */
	        pres = conn.prepareStatement(sql);
	        pres.setInt(1, final_price);
	        pres.setString(2, String.valueOf(is_rejected));
	        pres.setInt(3, customer_id);
	        pres.setInt(4, product_id);
	        /** 執行更新之SQL指令並記錄影響之行數 */
	        row = pres.executeUpdate();
	
	        /** 紀錄真實執行的SQL指令，並印出 **/
	        exexcute_sql = pres.toString();
	        System.out.println(exexcute_sql);
	
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
	    
	    /** 紀錄程式結束執行時間 */
	    long end_time = System.nanoTime();
	    /** 紀錄程式執行時間 */
	    long duration = (end_time - start_time);
	    
	    /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
	    JSONObject response = new JSONObject();
	    response.put("sql", exexcute_sql);
	    response.put("row", row);
	    response.put("time", duration);
	    response.put("data", jsa);
	
	    return response;
	}
	
	// check one customer only apply one bargain to a product
	public boolean checkDuplicate(Bargain bg){
        /** 紀錄SQL總行數，若為「-1」代表資料庫檢索尚未完成 */
        int row = -1;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT count(*) FROM `missa`.`bargain` WHERE `customer_id` = ? AND `product_id` = ?";
            
            /** 取得所需之參數 */
            int customer_id = bg.getCustomerId();
            int product_id = bg.getProductId();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, customer_id);
            pres.setInt(2, product_id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 讓指標移往最後一列，取得目前有幾行在資料庫內 */
            rs.next();
            row = rs.getInt("count(*)");
            //System.out.print(row);

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
        
        /** 
         * 判斷是否已經有一筆該議價之資料
         * 若無一筆則回傳False，否則回傳True 
         */
        return (row == 0) ? false : true;  //true:有重複
    }
	
}