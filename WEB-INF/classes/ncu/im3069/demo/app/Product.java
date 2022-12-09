package ncu.im3069.demo.app;

import org.json.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Product {

    /** 產品編號 **/
    private int product_id;
    /** 供應商編號 **/
    private int supplier_id;
    /** 名稱 **/
    private String name;
    /** 售價 **/
    private int price;
    /** 庫存 **/
    private int stock_num;
    /** 作者 **/
    private String author;
    /** 書況 **/
    private int condition;
    /** 交易 **/
    private String is_bargain;
    /** 創建時間 **/
    private Timestamp created_at;
    /** photos */
    private ArrayList<Photo> photos = new ArrayList<Photo>();
    
    /** oph，OrderItemHelper 之物件與 Order 相關之資料庫方法（Sigleton） */
    private PhotoHelper poh = PhotoHelper.getHelper();
    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param id 產品編號
     */
	public Product(int product_id) {
		this.product_id = product_id;
        this.created_at = Timestamp.valueOf(LocalDateTime.now());
	}

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param id 產品編號
     */
    public Product(int product_id, int price, int stock_num) {
        this.product_id = product_id;
        this.price = price;
        this.stock_num = stock_num;
        this.created_at = Timestamp.valueOf(LocalDateTime.now());
    }

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改產品時
     *
     * @param id 產品編號
     * @param name 產品名稱
     * @param price 產品價格
     * @param image 產品圖片
     * @param describe 產品敘述
     */
    public Product(int product_id, int supplier_id, String name, int price, 
                    int stock_num, String author, int condition, String is_bargain) {
        this.product_id = product_id;
        this.supplier_id = supplier_id;
        this.name = name;
        this.price = price;
        this.stock_num = stock_num;
        this.author = author;
        this.condition = condition;
        this.is_bargain = is_bargain;
        this.created_at = Timestamp.valueOf(LocalDateTime.now());
    }

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改產品時
     *
     * @param name 產品名稱
     * @param price 產品價格
     * @param image 產品圖片
     * @param describe 產品敘述
     */
    // 新增商品，賣家上傳
    public Product(int supplier_id, String name, int price, 
                    int stock_num, String author, int condition, String is_bargain) {
        this.supplier_id = supplier_id;
        this.name = name;
        this.price = price;
        this.stock_num = stock_num;
        this.author = author;
        this.condition = condition;
        this.is_bargain = is_bargain;
        this.created_at = Timestamp.valueOf(LocalDateTime.now());
    }

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改產品時
     *
     * @param id 產品編號
     * @param name 產品名稱
     * @param price 產品價格
     * @param image 產品圖片
     * @param describe 產品敘述
     */
	public Product(int product_id, int supplier_id, String name, int price, 
                    int stock_num, String author, int condition, String is_bargain, 
                    Timestamp created_at) {
        this.product_id = product_id;
        this.supplier_id = supplier_id;
        this.name = name;
        this.price = price;
        this.stock_num = stock_num;
        this.author = author;
        this.condition = condition;
        this.is_bargain = is_bargain;
        this.created_at = created_at;
        getProductPhotoFromDB();
	}
	// 新增商品的照片
	public void addProductPhoto(String img_name) {
        this.photos.add(new Photo(img_name));
    }
	public void addProductPhoto(Photo po) {
        this.photos.add(po);
    }
	// 設定訂單編號，這樣一建立完商品，才可回傳至前端此商品編號
	public void setProductId(int product_id) {
        this.product_id = product_id;
    }
	
    /**
     * 取得產品編號
     *
     * @return int 回傳產品編號
     */
	public int getProductID() {
		return this.product_id;
	}

    /**
     * 取得供應商編號
     *
     * @return String 回傳供應商編號
     */
    public int getSupplierID() {
        return this.supplier_id;
    }

    /**
     * 取得產品名稱
     *
     * @return String 回傳產品名稱
     */
	public String getName() {
		return this.name;
	}

    /**
     * 取得產品價格
     *
     * @return int 回傳產品價格
     */
	public int getPrice() {
		return this.price;
	}

    /**
     * 取得產品庫存
     *
     * @return String 回傳產品庫存
     */
	public int getStockNum() {
		return this.stock_num;
	}

    /**
     * 取得作者
     *
     * @return String 回傳作者
     */
	public String getAuthor() {
		return this.author;
	}

    /**
     * 取得書況
     *
     * @return String 回傳書況
     */
    public int getCondition() {
        return this.condition;
    }

    /**
     * 取得
     *
     * @return String 回傳
     */
    public String getIsBargain() {
        return this.is_bargain;
    }

    /**
     * 取得創建時間
     *
     * @return Timestamp 回傳創建時間
     */
    public Timestamp getCreatedAt() {
        return this.created_at;
    }
    
    // 取得商品照片array
    public ArrayList<Photo> getProductPhotos() {
        return this.photos;
    }
    // 商品取得照片資訊
    private void getProductPhotoFromDB() {
        ArrayList<Photo> data = poh.getProductPhotoByProductId(this.product_id);
        this.photos = data;
    }

    /**
     * 取得產品資訊
     *
     * @return JSONObject 回傳產品資訊
     */
    //取得商品 getProductData
	public JSONObject getData() {
        /** 透過JSONObject將該項產品所需之資料全部進行封裝*/
        JSONObject jso = new JSONObject();
        jso.put("product_id", getProductID());
        jso.put("supplier_id", getSupplierID());
        jso.put("name", getName());
        jso.put("price", getPrice());
        jso.put("stock_num", getStockNum());
        jso.put("author", getAuthor());
        jso.put("condition", getCondition());
        jso.put("is_bargain", getIsBargain());
        jso.put("created_at", getCreatedAt());

        return jso;
    }
	//取得商品的照片
	public JSONArray getProductPhotoData() {
        JSONArray result = new JSONArray();

        for(int i=0 ; i < this.photos.size() ; i++) {
            result.put(this.photos.get(i).getData());
        }

        return result;
    }
	//取得商品&照片
	public JSONObject getProductAllInfo() {
        JSONObject jso = new JSONObject();
        jso.put("product_info", getData());
        jso.put("photo_info", getProductPhotoData());

        return jso;
    }
	//設定商品的照片編號
	public void setProductPhotoId(JSONArray data) {
        for(int i=0 ; i < this.photos.size() ; i++) {
            this.photos.get(i).setId((int) data.getLong(i));
        }
    }
}
