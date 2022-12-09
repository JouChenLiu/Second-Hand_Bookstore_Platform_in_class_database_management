package ncu.im3069.demo.app;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import org.json.*;
import ncu.im3069.demo.util.Arith;


import org.json.*;
public class Bargain {

    /** id，議價編號 */
    private int id;

    /** customerId，買家編號 */
    private int customerId;

    /** pd，產品  for 賣家編號、商品編號*/
    private Product pd;
    //TODO pd.getSupplierId; 
    private int supplierId;
    private int productId;

    /** upper，買家議價上限 */
    private int upper;

    /** lower，買家議價下限 */
    private int lower;

    /** finalPrice，賣家最終價格 */
    private int finalPrice;

    /** isRejected，可否議價 */
    private char isRejected;

    /** create，議價創建時間 */
    private Timestamp create;
    //Timestamp.valueOf(LocalDateTime.now())

    /** ph，ProductHelper 之物件與 Bargain 相關之資料庫方法（Sigleton） */
    private ProductHelper ph =  ProductHelper.getHelper();

    /**
     * 實例化（Instantiates）一個新的（new Bargain 物件<br>
     * 採用多載（overload）方法進行，此建構子用於買家建立議價請求時
     * @param customerId 買家編號
     * @param pd 產品
     * @param upper 議價上限
     * @param lower 議價下限
     * @param create_time 議價提出時間
     */
    public Bargain(int customerId, Product pd, int upper, int lower) {
        this.customerId = customerId;
        this.pd = pd;
        // TODO this.supplierId = pd.getSupplierId();
        this.productId = pd.getProductID();
        this.upper = upper;
        this.lower = lower;
        this.finalPrice = 0;
        this.isRejected = 'n';
        this.create = Timestamp.valueOf(LocalDateTime.now());
    }
    public Bargain(int customerId, int supplierId, int productId, int upper, int lower) {
        this.customerId = customerId;
        this.supplierId = supplierId;
        this.productId = productId;
        this.upper = upper;
        this.lower = lower;
        this.finalPrice = 0;
        this.isRejected = 'n';
        this.create = Timestamp.valueOf(LocalDateTime.now());
    }

    /**
     * 實例化（Instantiates）一個新的（new Bargain 物件<br>
     * 採用多載（overload）方法進行，此建構子用於賣家回應議價要求時，set finalPrice & isRejected
     *
     * @param Bargain_id 議價編號 
     * @param customerId 買家編號
     * @param pd 產品
     * @param upper 議價上限
     * @param lower 議價下限
     * @param finalPrice 最終價格
     * @param isRejected 議價拒絕與否
     */
    public Bargain(int bargainId, int customerId, Product pd, int upper, int lower, int finalPrice, char isRejected){
        this.id = bargainId;
        this.customerId = customerId;
        this.pd = pd;
        this.upper = upper;
        this.lower = lower;
        this.finalPrice = finalPrice;
        this.isRejected = isRejected;
    }
    
    public Bargain(int bargain_id, int customer_id, int supplier_id, int product_id, int upper_limit, int lower_limit,
			int final_price, char is_rejected, Timestamp create_time) {
		// TODO Auto-generated constructor stub
    	this.id = bargain_id;
    	this.customerId = customer_id;
        this.supplierId = supplier_id;
        this.productId = product_id;
        this.upper = upper_limit;
        this.lower = lower_limit;
        this.finalPrice = final_price;
        this.isRejected = is_rejected;
        this.create = create_time;
	}

//    /**
//     * 從 DB 中取得產品
//     */
//    private void getProductFromDB(int product_id) {
//        String id = String.valueOf(product_id);
//        this.pd = ph.getById(id);
//    }

	/**
     * 取得產品
     *
     * @return Product 回傳產品
     */
    public Product getProduct() {
        return this.pd;
    }

    /**
     * 設定議價編號
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 取得議價編號
     *
     * @return int 回傳議價編號
     */
    public int getId() {
        return this.id;
    }

    /**
     * 取得買家編號
     *
     * @return int 回傳買家編號
     */
    public int getCustomerId() {
        return this.customerId;
    }
    
    /**
     * 取得買家編號
     *
     * @return int 回傳賣家編號
     */
    public int getSupplierId() {
        return this.supplierId;
    }

    /**
     * 取得買家編號
     *
     * @return int 回傳買家編號
     */
    public int getProductId() {
        return this.productId;
    }

    /**
     * 取得議價上限
     *
     * @return int 回傳議價上限編號
     */
    public int getUpper() {
        return this.upper;
    }

    /**
     * 取得議價下限
     *
     * @return int 回傳議價下限編號
     */
    public int getLower() {
        return this.lower;
    }

    /**
     * 取得最終議價價格
     *
     * @return int 回傳最終議價價格編號
     */
    public int getFinalPrice() {
        return this.finalPrice;
    }

    /**
     * 取得議價拒絕與否
     *
     * @return int 回傳議價拒絕與否
     */
    public char getIsRejected() {
        return this.isRejected;
    }

    /**
     * 取得買家提出議價請求時間
     *
     * @return int 回傳買家提出議價請求時間
     */
    public Timestamp getCreateTime() {
        return this.create;
    }
    
    //賣家更改
    public void setSeller(String finalPrice) {
    	this.finalPrice = Integer.parseInt(finalPrice);
    }
    public void setSeller(char isRejected) {
    	this.isRejected = isRejected;
    }

    /**
     * 取得議價資料
     *
     * @return JSONObject 回傳產品細項資料
     */
    public JSONObject getData() {
        JSONObject data = new JSONObject();
        data.put("bargain_id", getId());
        data.put("customer_id", getCustomerId());
        //TODO data.put("supplier_id", getProduct().getSupplierId());
        data.put("supplier_id", getSupplierId());
        //TODO data.put("product_id", getProduct().getID());
        data.put("product_id", getProductId());
        data.put("upper_limit", getUpper());
        data.put("lower_limit", getLower());
        data.put("final_price", getFinalPrice());
        data.put("is_rejected", String.valueOf(getIsRejected())); //jso put 不進char，改為string
        data.put("created_at", getCreateTime());

        return data;
    }
}
