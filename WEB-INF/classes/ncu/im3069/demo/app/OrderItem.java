package ncu.im3069.demo.app;

import org.json.JSONObject;
import ncu.im3069.demo.util.Arith;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class OrderItem {

    /** id，產品細項編號 */
    // private int id;

    /** pd，產品 */
    private Product pd;

    /** quantity，產品數量 */
    // private int quantity;

    /** price，產品價格 */
    // private double price;

    /** subtotal，產品小計 */
    private double subtotal;
    
    /** status，訂單狀態 */
    private int status;

    /** ph，ProductHelper 之物件與 OrderItem 相關之資料庫方法（Sigleton） */
    private ProductHelper ph =  ProductHelper.getHelper();
    
    /** ph，ProductHelper 之物件與 OrderItem 相關之資料庫方法（Sigleton） */
    private OrderHelper oh =  OrderHelper.getHelper();

    /** 訂單詳細編號 **/
    private int order_details_id;
    /** 會員密碼 **/
    private int order_id;
    /** 客戶編號 **/
    private int customer_id;
    /** 供應商編號 **/
    private int supplier_id;
    /** 產品編號 **/
    // private int product_id;
    /** 產品數量 **/
    private int product_num;
    /** 產品售價 **/
    private int product_price;
    /** 成立時間 **/
    private Timestamp created_at;

    /**
     * 實例化（Instantiates）一個新的（new）OrderItem 物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立訂單細項時
     *
     * @param pd 會員電子信箱
     * @param quantity 會員密碼
     */
    public OrderItem(Product pd, int product_num, int customer_id) {
        this.pd = pd;
        this.product_num = product_num;
        this.product_price = this.pd.getPrice();
        this.subtotal = Arith.mul((double) this.product_num, this.product_price);
        this.created_at = Timestamp.valueOf(LocalDateTime.now());
        this.customer_id = customer_id;
        this.supplier_id = pd.getSupplierID();
    }

    // /**
    //  * 實例化（Instantiates）一個新的（new）OrderItem 物件<br>
    //  * 採用多載（overload）方法進行，此建構子用於修改訂單細項時
    //  *
    //  * @param order_product_id 訂單產品編號
    //  * @param order_id 會員密碼
    //  * @param product_id 產品編號
    //  * @param price 產品價格
    //  * @param quantity 產品數量
    //  * @param subtotal 小計
    //  */
    // public OrderItem(int order_details_id, int order_id, int product_id, double product_price, int product_num){//, double subtotal) {
    //     // this.id = order_product_id;
    //     // this.quantity = quantity;
    //     // this.price = price;
    //     // this.subtotal = subtotal;
    //     // getProductFromDB(product_id);

    //     this.order_details_id = order_details_id;
    //     this.product_num = product_num;
    //     this.product_price = product_price;
    //     // this.subtotal = subtotal;
    //     this.subtotal = Arith.mul((double) this.product_num, this.product_price);
    //     getProductFromDB(product_id);
    // }

    /**
     * 實例化（Instantiates）一個新的（new）OrderItem 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改訂單細項時
     *
     * @param order_product_id 訂單產品編號
     * @param order_id 會員密碼
     * @param product_id 產品編號
     * @param price 產品價格
     * @param quantity 產品數量
     * @param subtotal 小計
     */
    public OrderItem(int order_details_id, int order_id, int product_id, int product_price, 
                        int product_num, int customer_id, int supplier_id, Timestamp created_at){
        this.order_details_id = order_details_id;
        this.order_id = order_id;
        this.customer_id = customer_id;
        this.supplier_id = supplier_id;
        this.product_num = product_num;
        this.product_price = product_price;
        this.created_at = created_at;
        this.subtotal = Arith.mul((double) this.product_num, this.product_price);
        getProductFromDB(product_id);
    }

    /**
     * 實例化（Instantiates）一個新的（new）OrderItem 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改訂單細項時
     *
     * @param order_product_id 訂單產品編號
     * @param order_id 會員密碼
     * @param product_id 產品編號
     * @param price 產品價格
     * @param quantity 產品數量
     * @param subtotal 小計
     */
    public OrderItem(int order_id, int product_id, int product_price, int product_num, int customer_id, 
                        int supplier_id){
        this.order_id = order_id;
        this.customer_id = customer_id;
        this.supplier_id = supplier_id;
        this.product_num = product_num;
        this.product_price = product_price;
        this.subtotal = Arith.mul((double) this.product_num, this.product_price);
        getProductFromDB(product_id);
    }
    
    public OrderItem(int order_id, Timestamp created_at, double subtotal) {
    	this.order_id = order_id;
    	this.created_at = created_at;
    	this.subtotal = subtotal;
	}

	public void setId(int id) {
    	this.order_details_id = id;
    }

    /**
     * 從 DB 中取得產品
     */
    private void getProductFromDB(int product_id) {
        String id = String.valueOf(product_id);
        this.pd = ph.getById(id);
    }
    
    /**
     * 從 DB 中取得訂單狀態
     */
    public void getStatus(String order_id) {
        this.status = oh.getStatusById(order_id);
//        JSONObject data = new JSONObject();
//        data.put("order_id", getStatusById());
//        return this.status;
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
     * 設定訂單細項編號
     */
    public void setOrderDetailsId(int order_details_id) {
        this.order_details_id = order_details_id;
    }

    /**
     * 取得訂單細項編號
     *
     * @return int 回傳訂單細項編號
     */
    public int getOrderDetailsId() {
        return this.order_details_id;
    }

    /**
     * 取得產品價格
     *
     * @return double 回傳產品價格
     */
    public int getProductPrice() {
        return this.product_price;
    }

    /**
     * 取得產品細項小計
     *
     * @return double 回傳產品細項小計
     */
    public double getSubTotal() {
        return this.subtotal;
    }

    /**
     * 取得產品數量
     *
     * @return int 回傳產品數量
     */
    public int getProductNum() {
        return this.product_num;
    }

    /**
     * 取得會員密碼
     *
     * @return int 回傳會員密碼
     */
    public int getOrderId() {
        return this.order_id;
    }

    /**
     * 取得客戶編號
     *
     * @return int 回傳客戶編號
     */
    public int getCustomerId() {
        return this.customer_id;
    }

    /**
     * 取得供應商編號
     *
     * @return int 回傳供應商編號
     */
    public int getSupplierId() {
        return this.supplier_id;
    }

    /**
     * 取得
     *
     * @return Timestamp 回傳
     */
    public Timestamp getCreatedAt() {
        return this.created_at;
    }

    /**
     * 取得產品細項資料
     *
     * @return JSONObject 回傳產品細項資料
     */
    public JSONObject getData() {
        JSONObject data = new JSONObject();
        data.put("order_details_id", getOrderDetailsId());
        data.put("product", getProduct().getData());
        data.put("product_price", getProductPrice());
        data.put("product_num", getProductNum());
        data.put("subtotal", getSubTotal());
        data.put("order_id", getOrderId());
        data.put("customer_id", getCustomerId());
        data.put("supplier_id", getSupplierId());
        data.put("created_at", getCreatedAt());

        return data;
    }
    
    public JSONObject getOrderData() {
        JSONObject data = new JSONObject();
        data.put("subtotal", getSubTotal());
        data.put("order_id", getOrderId());
        //data.put("status", getStatus());
        data.put("created_at", getCreatedAt());

        return data;
    }
}
