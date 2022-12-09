package ncu.im3069.demo.app;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.json.*;

import ncu.im3069.demo.util.Arith;

public class Photo {

    /** id，會員編號 */
    private int id;

    /** id，圖片類別 */
    private int member_id = 0;
    /** id，圖片類別 */
    private int product_id = 0;

    /** id，圖片名稱 */
    private String imgName;
    
    private Timestamp created_at;
    
    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param name 產品名稱
     * @param price 產品價格
     * @param image 產品圖片
     * @param describe 產品敘述
     */
	public Photo(boolean isProduct, int foreignId, String imgName) {
		this.imgName = imgName;
		if(isProduct) {
			this.product_id = foreignId;
		}
		else {
			this.member_id = foreignId;
		}
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
	public Photo(int id,boolean isProduct, int foreignId, String imgName, Timestamp created_at) {
		this.id = id;
		this.imgName = imgName;
		if(isProduct) {
			this.product_id = foreignId;
		}
		else {
			this.member_id = foreignId;
		}
        this.created_at = created_at;
	}
	//
	public Photo(String img_name) {
        this.imgName = img_name;
        this.created_at = Timestamp.valueOf(LocalDateTime.now());
    }

    /**
     * 取得產品名稱
     *
     * @return String 回傳產品名稱
     */
	public void setId(int id) {
		this.id = id;
	}

    /**
     * 取得產品編號
     *
     * @return int 回傳產品編號
     */
	public int getId() {
		return this.id;
	}

    /**
     * 取得產品名稱
     *
     * @return String 回傳產品名稱
     */
	public void setProductId(int product_id) {
		this.product_id = product_id;
	}

    /**
     * 取得產品名稱
     *
     * @return String 回傳產品名稱
     */
	public int getProductId() {
		return this.product_id;
	}

    /**
     * 取得產品名稱
     *
     * @return String 回傳產品名稱
     */
	public void setMemberId(int member_id) {
		this.member_id = member_id;
	}

    /**
     * 取得產品名稱
     *
     * @return String 回傳產品名稱
     */
	public int getMemberId() {
		return this.member_id;
	}

    /**
     * 取得產品名稱
     *
     * @return String 回傳產品名稱
     */
	public void setName(String imgName) {
		this.imgName = imgName;
	}

    /**
     * 取得產品名稱
     *
     * @return String 回傳產品名稱
     */
	public String getName() {
		return this.imgName;
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
     * 取得產品資訊
     *
     * @return JSONObject 回傳產品資訊
     */
	public JSONObject getData() {
        /** 透過JSONObject將該項產品所需之資料全部進行封裝*/
        JSONObject jso = new JSONObject();
        jso.put("photo_id", getId());
        jso.put("imgName", getName());
        jso.put("product_id", (getProductId() == -1 ? null : getProductId()));
        jso.put("member_id", (getMemberId() == -1 ? null : getMemberId()));
        jso.put("created_at", getCreateTime());
        return jso;
    }
}
