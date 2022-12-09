package ncu.im3069.demo.app;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import org.json.*;

public class Order {
	//order_id, customer_id, status, address, totalPrice, shipping_code, created_at
    /** order_id，訂單編號 */
    private int order_id;

    /** customer_id，買家編號 */
    private int customer_id;

    /** status，訂單狀態 */
    private int status;

    /** address，超商取貨門市 */
    private String address;

    /** totalPrice，訂單總價格 */
    private int totalPrice;

    /** list，訂單列表 */
    private ArrayList<OrderItem> list = new ArrayList<OrderItem>();

    /** shipping_code，物流編號 */
    private String shipping_code;

    /** created_at，訂單創建時間 */
    private Timestamp created_at;

    /** oph，OrderItemHelper 之物件與 Order 相關之資料庫方法（Sigleton） */
    private OrderItemHelper oph = OrderItemHelper.getHelper();

    public Order(int status) {
    	this.status = status;
    }
    
    public Order(int customer_id, String address, int totalPrice) {
        this.customer_id = customer_id;
        this.status = 1;
        this.address = address;
        this.totalPrice = totalPrice;
        this.shipping_code = null;
        this.created_at = Timestamp.valueOf(LocalDateTime.now());
    }
    
    
    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立訂單資料時，產生一個新的訂單
     *
     * @param customer_id 買家編號
     * @param status 訂單狀態
     * @param address 超商取貨門市
     * @param totalPrice 訂單總價格
     * @param shipping_code 物流編號
     */
    public Order(int customer_id, int status, String address, int totalPrice, String shipping_code) {
        this.customer_id = customer_id;
        this.status = status;
        this.address = address;
        this.totalPrice = totalPrice;
        this.shipping_code = shipping_code;
        this.created_at = Timestamp.valueOf(LocalDateTime.now());
    }

    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改訂單資料時，新改資料庫已存在的訂單
     *
     * @param customer_id 買家編號
     * @param status 訂單狀態
     * @param address 超商取貨門市
     * @param totalPrice 訂單總價格
     * @param shipping_code 物流編號
     * @param create_at 訂單創建時間
     */
    public Order(int order_id, int customer_id, int status, String address, int totalPrice, String shipping_code, Timestamp created_at) {
        this.order_id = order_id;
        this.customer_id = customer_id;
        this.status = status;
        this.address = address;
        this.totalPrice = totalPrice;
        this.shipping_code = shipping_code;
        this.created_at = created_at;
        getOrderProductFromDB();
    }
    
//    public Order(int customer_id, int status, String address, int totalPrice, String shipping_code, Timestamp created_at) {
//    	this.customer_id = customer_id;
//        this.status = status;
//        this.address = address;
//        this.totalPrice = totalPrice;
//        this.shipping_code = shipping_code;
//        this.created_at = created_at;
//	}

	/**
     * 新增一個訂單產品及其數量
     */
    public void addOrderProduct(Product pd, int quantity, int customer_id) {
        this.list.add(new OrderItem(pd, quantity, customer_id));
    }

    /**
     * 新增一個訂單產品
     */
    public void addOrderProduct(OrderItem op) {
        this.list.add(op);
    }

    /**
     * 設定訂單編號
     */
    public void setOrderId(int order_id) {
        this.order_id = order_id;
    }

    /**
     * 取得訂單編號
     *
     * @return int 回傳訂單編號
     */
    public int getOrderId() {
        return this.order_id;
    }

    /**
     * 取得訂單買家編號
     *
     * @return int 回傳訂單買家編號
     */
    public int getCustomerId() {
        return this.customer_id;
    }

    /**
     * 取得訂單狀態
     *
     * @return int 回傳訂單狀態
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * 取得超商取貨門市
     *
     * @return String 回傳超商取貨門市
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * 取得訂單總價格
     *
     * @return int 回傳訂單總價格
     */
    public int getTotalPrice() {
        return this.totalPrice;
    }
    
    /**
     * 取得物流編號
     *
     * @return String 回傳物流編號
     */
    public String getShippingCode() {
        return this.shipping_code;
    }
    
    /**
     * 取得訂單創建時間
     *
     * @return Timestamp 回傳訂單創建時間
     */
    public Timestamp getCreatedAtTime() {
        return this.created_at;
    }

    /**
     * 取得訂單修改時間
     *
     * @return Timestamp 回傳訂單修改時間
     *
    public Timestamp getModifyTime() {
        return this.modify;
    }	*/

    /**
     * 取得該名會員所有資料
     *
     * @return the data 取得該名會員之所有資料並封裝於JSONObject物件內
     */
    public ArrayList<OrderItem> getOrderProduct() {
        return this.list;
    }

    /**
     * 從 DB 中取得訂單產品
     */
    private void getOrderProductFromDB() {
        ArrayList<OrderItem> data = oph.getOrderProductByOrderId(this.order_id);
        this.list = data;
    }

    /**
     * 取得訂單基本資料
     *
     * @return JSONObject 取得訂單基本資料
     */
    public JSONObject getOrderData() {
        JSONObject jso = new JSONObject();
        jso.put("order_id", getOrderId());
        jso.put("customer_id", getCustomerId());
        jso.put("status", getStatus());
        jso.put("address", getAddress());
        jso.put("totalPrice", getTotalPrice());
        jso.put("shipping_code", getShippingCode());
        jso.put("created_at", getCreatedAtTime());

        return jso;
    }

    /**
     * 取得訂單產品資料
     *
     * @return JSONArray 取得訂單產品資料
     */
    public JSONArray getOrderProductData() {
        JSONArray result = new JSONArray();

        for(int i=0 ; i < this.list.size() ; i++) {
            result.put(this.list.get(i).getData());
        }

        return result;
    }

    /**
     * 取得訂單所有資訊
     *
     * @return JSONObject 取得訂單所有資訊
     */
    public JSONObject getOrderAllInfo() {
        JSONObject jso = new JSONObject();
        jso.put("order_info", getOrderData());
        jso.put("product_info", getOrderProductData());

        return jso;
    }

    /**
     * 設定訂單產品編號
     */
    public void setOrderProductId(JSONArray data) {
    	System.out.println(data);
        for(int i=0 ; i < this.list.size() ; i++) {
            this.list.get(i).setId((int) data.getLong(i));
        }
    }

}
