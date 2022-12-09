package ncu.im3069.demo.app;

import java.sql.*;
import java.time.LocalDateTime;

import org.json.*;

public class Cart {

    /** id，購物車編號 */
    private int id;

    /** id，會員編號 */
    private int customer_id;

    /** id，商品編號 */
    private int product_id;

    /** id，商品數量 */
    private int product_num;
    /** id，商品價格 */
    private int product_price;
    /** id，商品編號 */
    private Timestamp created_at;
    private Product pd;
    private String is_bargained;
    /** ph，ProductHelper 之物件與 OrderItem 相關之資料庫方法（Sigleton） */
    private ProductHelper ph =  ProductHelper.getHelper();

    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立訂單資料時，產生一個新的訂單
     *
     * @param first_name 會員名
     * @param last_name 會員姓
     * @param email 會員電子信箱
     * @param address 會員地址
     * @param phone 會員姓名
     */
    public Cart(int customer_id, int product_id, int product_num, String is_bargained) {
        this.customer_id = customer_id;
        this.product_id = product_id;
        this.product_num = product_num;
        this.is_bargained = is_bargained;
        this.created_at = Timestamp.valueOf(LocalDateTime.now());
        getProductFromDB(product_id);
    }

    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立議價產生的購物車資料時，產生一個新的購物車
     *
     * @param first_name 會員名
     * @param last_name 會員姓
     * @param email 會員電子信箱
     * @param address 會員地址
     * @param phone 會員姓名
     */
    public Cart(int customer_id, int product_id, int product_num, int product_price, String is_bargained) {
        this.customer_id = customer_id;
        this.product_id = product_id;
        this.product_num = product_num;
        this.product_price = product_price;
        this.is_bargained = is_bargained;
        this.created_at = Timestamp.valueOf(LocalDateTime.now());
        getProductFromDB(product_id);
    }

    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立訂單資料時，產生一個新的訂單
     *
     * @param first_name 會員名
     * @param last_name 會員姓
     * @param email 會員電子信箱
     * @param address 會員地址
     * @param phone 會員姓名
     */
    public Cart(int id, int customer_id, int product_id, int product_num, int product_price, Timestamp created_at, String is_bargained) {
    	this.id = id;
        this.customer_id = customer_id;
        this.product_id = product_id;
        this.product_num = product_num;
        this.product_price = product_price;
        this.is_bargained = is_bargained;
        this.created_at = created_at;
        getProductFromDB(product_id);
    }

    /**
     * 設定訂單編號
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 取得訂單編號
     *
     * @return int 回傳訂單編號
     */
    public int getId() {
        return this.id;
    }
    /**
     * 設定訂單編號
     */
    public void setProductNum(int product_num) {
        this.product_num = product_num;
    }

    /**
     * 取得訂單編號
     *
     * @return int 回傳訂單編號
     */
    public int getProductNum() {
        return this.product_num;
    }
    /**
     * 設定訂單編號
     */
    public void setProductPrice(int product_price) {
        this.product_price = product_price;
    }

    /**
     * 取得訂單編號
     *
     * @return int 回傳訂單編號
     */
    public int getProductPrice() {
        return this.product_price;
    }
    /**
     * 設定訂單編號
     */
    public void setMemberId(int customer_id) {
        this.customer_id = customer_id;
    }

    /**
     * 取得訂單編號
     *
     * @return int 回傳訂單編號
     */
    public int getMemberId() {
        return this.customer_id;
    }

    /**
     * 設定訂單編號
     */
    public void setProductId(int product_id) {
        this.product_id = product_id;
    }

    /**
     * 取得訂單編號
     *
     * @return int 回傳訂單編號
     */
    public int getProductId() {
        return this.product_id;
    }

    /**
     * 取得訂單編號
     *
     * @return int 回傳訂單編號
     */
    public void setIsBargained(String is_bargained) {
        this.is_bargained = is_bargained;
    }

    /**
     * 取得訂單編號
     *
     * @return int 回傳訂單編號
     */
    public String getIsBargained() {
        return this.is_bargained;
    }

    /**
     * 取得訂單創建時間
     *
     * @return Timestamp 回傳訂單創建時間
     */
    public void getProductFromDB(int product_id) {
        this.pd = ph.getById(String.valueOf(product_id));;
    }

    /**
     * 取得產品
     *
     * @return Product 回傳產品
     */
    public Product getProduct() {
        return this.pd;
    }


    /**
     * 取得訂單創建時間
     *
     * @return Timestamp 回傳訂單創建時間
     */
    public Timestamp getCreateTime() {
        return this.created_at;
    }

    /**
     * 取得訂單基本資料
     *
     * @return JSONObject 取得訂單基本資料
     */
    public JSONObject getData() {
        JSONObject jso = new JSONObject();
        jso.put("id", getId());
        jso.put("customer_id", getMemberId());
        jso.put("product_id", getProductId());
        jso.put("product_num", getProductNum());
        jso.put("product_price", getProductPrice());
        jso.put("created_at", getCreateTime());
        jso.put("isBargain", getIsBargained());
        jso.put("product_name", getProduct().getName());
        jso.put("product_stock", getProduct().getStockNum());

        return jso;
    }

}
