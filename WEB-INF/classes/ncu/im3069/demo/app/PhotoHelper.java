package ncu.im3069.demo.app;

import java.sql.*;
import java.util.*;
import java.time.LocalDateTime;

import org.json.*;

import ncu.im3069.demo.util.DBMgr;
import ncu.im3069.demo.app.Photo;

public class PhotoHelper {
    private PhotoHelper() {
        
    }
    
    private static PhotoHelper poh;
    private Connection conn = null;
    private PreparedStatement pres = null;
    
    public static PhotoHelper getHelper() {
        /** Singleton檢查是否已經有ProductHelper物件，若無則new一個，若有則直接回傳 */
        if(poh == null) poh = new PhotoHelper();
        
        return poh;
    }
    
    // 建立照片資訊，for 建立商品
    public JSONArray createByList(int product_id, List<Photo> productPhoto) {
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        
        for(int i=0 ; i < productPhoto.size() ; i++) {
            Photo po = productPhoto.get(i);
            
            /** 取得所需之參數 */
            
            String imgName = po.getName();
            Timestamp created_at = po.getCreateTime();
            
            try {
                /** 取得資料庫之連線 */
                conn = DBMgr.getConnection();
                /** SQL指令 */
                String sql = "INSERT INTO `missa`.`photo`(`product_id`, `imgName`, `created_at`)"
                        + " VALUES(?, ?, ?)";
                
                /** 將參數回填至SQL指令當中 */
                pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pres.setInt(1, product_id);
                pres.setString(2, imgName);
                pres.setTimestamp(3, created_at);
                
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
    
    public JSONObject getAll() {
        /** 新建一個 Product 物件之 m 變數，用於紀錄每一位查詢回之商品資料 */
    	Photo p = null;
        /** 用於儲存所有檢索回之商品，以JSONArray方式儲存 */
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
            String sql = "SELECT * FROM `missa`.`photo`";
            
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
                boolean isProduct;
                int photo_id = rs.getInt("photo_id");
                int id = rs.getInt("member_id");
                if (rs.wasNull()) {
                    isProduct = true;
                    id = rs.getInt("product_id");
                }
                else {
                	isProduct = false;
                }
                String imgName = rs.getString("imgName");
                Timestamp created_at = rs.getTimestamp("created_at");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                p = new Photo(photo_id, isProduct, id, imgName, created_at);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(p.getData());
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
    public JSONObject getByMemberId(int id) {
        /** 新建一個 Product 物件之 m 變數，用於紀錄每一位查詢回之商品資料 */
    	Photo p = null;
        /** 用於儲存所有檢索回之商品，以JSONArray方式儲存 */
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
            String sql = "SELECT * FROM `missa`.`photo` WHERE `member_id` IS NOT NULL AND `member_id` = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
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
                boolean isProduct = false;
                int photo_id = rs.getInt("photo_id");
                String imgName = rs.getString("imgName");
                Timestamp created_at = rs.getTimestamp("created_at");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                p = new Photo(photo_id, isProduct, id, imgName, created_at);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(p.getData());
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
    public JSONObject getByProductId(int id) {
        /** 新建一個 Product 物件之 m 變數，用於紀錄每一位查詢回之商品資料 */
    	Photo p = null;
        /** 用於儲存所有檢索回之商品，以JSONArray方式儲存 */
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
            String sql = "SELECT * FROM `missa`.`photo` WHERE `product_id` IS NOT NULL AND `product_id` = ?";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
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
                boolean isProduct = true;
                int photo_id = rs.getInt("photo_id");
                String imgName = rs.getString("imgName");
                Timestamp created_at = rs.getTimestamp("created_at");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                p = new Photo(photo_id, isProduct, id, imgName, created_at);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(p.getData());
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
    
    public ArrayList<Photo> getProductPhotoByProductId(int product_id) {
        ArrayList<Photo> result = new ArrayList<Photo>();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        ResultSet rs = null;
        Photo po;
                
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`photo` WHERE `product_id` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, product_id);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            rs = pres.executeQuery();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                
                /** 將 ResultSet 之資料取出 */
                int photo_id = rs.getInt("photo_id");
                //int product_id = rs.getInt("product_id");
                String img_name = rs.getString("imgName");
                Timestamp created_at = rs.getTimestamp("created_at");
                
                /** 將每一筆會員資料產生一名新Photo物件, foreignId = productId */
                po = new Photo(photo_id, true, product_id, img_name, created_at);
                
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
                result.add(po);
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
    
//     public JSONObject getByIdList(String data) {
//       /** 新建一個 Product 物件之 m 變數，用於紀錄每一位查詢回之商品資料 */
//       Photo p = null;
//       /** 用於儲存所有檢索回之商品，以JSONArray方式儲存 */
//       JSONArray jsa = new JSONArray();
//       /** 記錄實際執行之SQL指令 */
//       String exexcute_sql = "";
//       /** 紀錄程式開始執行時間 */
//       long start_time = System.nanoTime();
//       /** 紀錄SQL總行數 */
//       int row = 0;
//       /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
//       ResultSet rs = null;

//       try {
//           /** 取得資料庫之連線 */
//           conn = DBMgr.getConnection();
//           String[] in_para = DBMgr.stringToArray(data, ",");
//           /** SQL指令 */
//           String sql = "SELECT * FROM `missa`.`photo` WHERE `photo`.`photo_id`";
//           for (int i=0 ; i < in_para.length ; i++) {
//               sql += (i == 0) ? "in (?" : ", ?";
//               sql += (i == in_para.length-1) ? ")" : "";
//           }
          
//           /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
//           pres = conn.prepareStatement(sql);
//           for (int i=0 ; i < in_para.length ; i++) {
//             pres.setString(i+1, in_para[i]);
//           }
//           /** 執行查詢之SQL指令並記錄其回傳之資料 */
//           rs = pres.executeQuery();

//           /** 紀錄真實執行的SQL指令，並印出 **/
//           exexcute_sql = pres.toString();
//           System.out.println(exexcute_sql);
          
//           /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
//           while(rs.next()) {
//               /** 每執行一次迴圈表示有一筆資料 */
//               row += 1;
              
//               /** 將 ResultSet 之資料取出 */
//               int product_id = rs.getInt("id");
//               String name = rs.getString("name");
//               double price = rs.getDouble("price");
//               String image = rs.getString("image");
//               String describe = rs.getString("describe");
              
//               /** 將每一筆商品資料產生一名新Product物件 */
//               p = new Product(product_id, name, price, image, describe);
//               /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
//               jsa.put(p.getData());
//           }

//       } catch (SQLException e) {
//           /** 印出JDBC SQL指令錯誤 **/
//           System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
//       } catch (Exception e) {
//           /** 若錯誤則印出錯誤訊息 */
//           e.printStackTrace();
//       } finally {
//           /** 關閉連線並釋放所有資料庫相關之資源 **/
//           DBMgr.close(rs, pres, conn);
//       }
      
//       /** 紀錄程式結束執行時間 */
//       long end_time = System.nanoTime();
//       /** 紀錄程式執行時間 */
//       long duration = (end_time - start_time);
      
//       /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
//       JSONObject response = new JSONObject();
//       response.put("sql", exexcute_sql);
//       response.put("row", row);
//       response.put("time", duration);
//       response.put("data", jsa);

//       return response;
//   }
    
    public Photo getById(int id) {
        /** 新建一個 Product 物件之 m 變數，用於紀錄每一位查詢回之商品資料 */
        Photo p = null;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`photo` WHERE `photo`.`photo_id` = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 將 ResultSet 之資料取出 */
                boolean isProduct;
                int foreignId = rs.getInt("member_id");
                if (rs.wasNull()) {
                    isProduct = true;
                    foreignId = rs.getInt("product_id");
                }
                else {
                	isProduct = false;
                }
                String imgName = rs.getString("imgName");
                Timestamp created_at = rs.getTimestamp("created_at");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                p = new Photo(id, isProduct, foreignId, imgName, created_at);
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

        return p;
    }

    public JSONObject create(Photo p) {
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
            String sql = "INSERT INTO `missa`.`photo`(`member_id`, `product_id`, `imgName`, `created_at`)"
                    + " VALUES(?, ?, ?, ?)";
            
            /** 取得所需之參數 */
            int member_id = p.getMemberId();
            String imgName = p.getName();
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            
            if(member_id != 0){
                pres.setInt(1, member_id);
                pres.setNull(2, Types.INTEGER);
            }
            else{
                
                pres.setNull(1, Types.INTEGER);
                pres.setInt(2, p.getProductId());
            }
            pres.setString(3, imgName);
            pres.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            
            /** 執行新增之SQL指令並記錄影響之行數 */
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
        response.put("time", duration);
        response.put("row", row);

        return response;
    }

    public JSONObject deleteById(int id) {
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
            String sql = "DELETE FROM `missa`.`photo` WHERE `photo_id` = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
            /** 執行刪除之SQL指令並記錄影響之行數 */
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
            DBMgr.close(rs, pres, conn);
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

        return response;
    }
}
