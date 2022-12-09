package ncu.im3069.demo.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.Bargain;
import ncu.im3069.demo.app.BargainHelper;
import ncu.im3069.demo.app.MemberHelper;
import ncu.im3069.demo.app.PhotoHelper;
import ncu.im3069.demo.app.ProductHelper;
import ncu.im3069.tools.JsonReader;

// import ncu.im3069.demo.app.order;
// import ncu.im3069.demo.app.Product;
// import ncu.im3069.demo.app.ProductHelper;
// import ncu.im3069.demo.app.orderHelper;
// import ncu.im3069.tools.JsonReader;

import javax.servlet.annotation.WebServlet;

@WebServlet("/api/bargain.do")
public class BargainController extends HttpServlet {

    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

    // /** ph，ProductHelper 之物件與 Product 相關之資料庫方法（Sigleton） */
    // private ProductHelper ph =  ProductHelper.getHelper();

    /** Helper 之物件與 . 相關之資料庫方法（Sigleton） */
	private BargainHelper bh = BargainHelper.getHelper();
	private ProductHelper ph =  ProductHelper.getHelper();
	private PhotoHelper poh =  PhotoHelper.getHelper();
	private MemberHelper mh =  MemberHelper.getHelper();
	
     public BargainController() {
         super();
     }

    /**
     * 處理 Http Method 請求 GET 方法（新增資料）
     *
     * @param request Servlet 請求之 HttpServletRequest 之 Request 物件（前端到後端）
     * @param response Servlet 回傳之 HttpServletResponse 之 Response 物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);

        /** 取出經解析到 JsonReader 之 Request 參數 */
        //String id = jsr.getParameter("id");
        String customer_id = jsr.getParameter("customer_id");
        String supplier_id = jsr.getParameter("supplier_id");
        String product_id = jsr.getParameter("product_id");

        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();

        /** 判斷該字串是否存在，若存在代表要取回個別議價之資料，否則代表要取回全部資料庫內議價之資料 */
//        if (!id.isEmpty()) {
//          /** 透過 BargainHelper 物件的 getByID() 方法自資料庫取回該筆議價之資料，回傳之資料為 JSONObject 物件 */
//          JSONObject query = bh.getById(id);
//          resp.put("status", "200");
//          resp.put("message", "單筆議價資料取得成功");
//          resp.put("response", query);
//        }
//        else
        if(!supplier_id.isEmpty() && !product_id.isEmpty()) {
        	/** 透過 BargainHelper 物件的 getByID() 方法自資料庫取回該筆議價之資料，回傳之資料為 JSONObject 物件 */
        	JSONObject query = bh.getBySIandPI(supplier_id, product_id); //bargain
        	JSONObject productInfo = ph.getById(product_id).getData(); //product
        	int productId = Integer.parseInt(product_id);
        	
        	//get customer name array
        	ArrayList<Object> members = new ArrayList<Object>();
        	for (int i = 0; i < query.length(); i++) {
        		members.add(mh.getBargainMemberNameById((int) query.getJSONArray("data").getJSONObject(i).get("customer_id")).get("data"));
			}
        	
        	//get photo array
        	ArrayList<Object> srcs = new ArrayList<Object>(); // array，用來存商品的photos
        	JSONObject photos = poh.getByProductId(productId); // 取product 的 photos, by id
			JSONArray photoData = photos.getJSONArray("data"); // 取photo的data
			for (int i = 0; i < photoData.length(); i++) {
				srcs.add("/NCU_MIS_SA/statics/files/" + photoData.getJSONObject(i).get("imgName"));
			}
        	query.put("src", srcs);
        	
//        	//get customer name array
//        	//ArrayList<Object> members = new ArrayList<Object>();
//        	JSONObject member = mh.getBargainMemberNameById(productId);
//        	JSONArray memberName = member.getJSONArray("data");
//        	for (int i = 0; i < memberName.length(); i++) {
//        		members.add(memberName.getJSONObject(i).get("name"));
//			}
        	query.put("memberName", members);
        	
        	query.put("product", productInfo);
            resp.put("status", "200");
            resp.put("message", "賣家該商品議價資料取得成功");
            resp.put("response", query);
        }
        else if (!customer_id.isEmpty()){
        	/** 透過 BargainHelper 物件的 getByID() 方法自資料庫取回該筆議價之資料，回傳之資料為 JSONObject 物件 */
            JSONObject query = bh.getByCustomerId(customer_id);
            resp.put("status", "200");
            resp.put("message", "買家議價資料取得成功");
            resp.put("response", query);
        }
        else if (!supplier_id.isEmpty()){
        	/** 透過 BargainHelper 物件的 getByID() 方法自資料庫取回該筆議價之資料，回傳之資料為 JSONObject 物件 */
            JSONObject query = bh.getBySupplierId(supplier_id);
            resp.put("status", "200");
            resp.put("message", "賣家議價資料取得成功");
            resp.put("response", query);
        }
        else {
          /** 透過 BargainHelper 物件之 getAll() 方法取回所有議價之資料，回傳之資料為 JSONObject 物件 */
          JSONObject query = bh.getAll();
          resp.put("status", "200");
          resp.put("message", "所有議價資料取得成功");
          resp.put("response", query);
        }

        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
        jsr.response(resp, response);
	}

    /**
     * 處理 Http Method 請求 POST 方法（新增資料）
     *
     * @param request Servlet 請求之 HttpServletRequest 之 Request 物件（前端到後端）
     * @param response Servlet 回傳之 HttpServletResponse 之 Response 物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    /** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        /** 取出經解析到 JSONObject 之 Request 參數 */
        int customer_id = jso.getInt("customer_id");
        int supplier_id = jso.getInt("supplier_id");
        int product_id = jso.getInt("product_id");
        int upper_limit = jso.getInt("upper_limit");
        int lower_limit = jso.getInt("lower_limit");
        //int final_price = jso.getInt("final_price");
        //int is_rejected = jso.getChar("is_rejected");
        // JSONArray item = jso.getJSONArray("item");
        // JSONArray quantity = jso.getJSONArray("quantity");

        /** 建立一個新的議價物件 */
        Bargain bg = new Bargain(customer_id, supplier_id, product_id, upper_limit, lower_limit);
  
        // TODO
        // /** 將每一筆議價細項取得出來 */
        // for(int i=0 ; i < item.length() ; i++) {
        //     String product_id = item.getString(i);
        //     int amount = quantity.getInt(i);

        //     /** 透過 ProductHelper 物件之 getById()，取得產品的資料並加進議價物件裡 */
        //     Product pd = ph.getById(product_id);
        //     bg.addbargainProduct(pd, amount);
        //}

        /** 透過 bargainHelper 物件的 create() 方法新建一筆議價至資料庫 */
        JSONObject result = null;
        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        
        if(!bh.checkDuplicate(bg)) {
        	result = bh.create(bg);
        	resp.put("status", "200");
            resp.put("message", "議價新增成功！");
            resp.put("response", result);
        }else {
        	resp.put("status", "201");
        	resp.put("message", "已傳送過議價！待賣家回覆");
        	resp.put("response", result);
        }

        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
        jsr.response(resp, response);
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    /** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        /** 取出經解析到 JSONObject 之 Request 參數 */
        String customer_id = Integer.toString(jso.getInt("customer_id"));
        String product_id = Integer.toString(jso.getInt("product_id"));
        String final_price = Integer.toString(jso.getInt("final_price"));
 
        /** 取出經解析到 JSONObject 之 Request 參數 */
//        String customer_id = jsr.getParameter("customer_id");
//        System.out.print(customer_id);
//        String product_id = jsr.getParameter("product_id");
        Bargain bg = bh.getByCIandPI(customer_id, product_id);
        //System.out.println(bg.getIsRejected());
        //String finalPrice = jsr.getParameter("final_price");
        if(!(final_price.equals("0"))) {
        	//System.out.print("here");
        	bg.setSeller(final_price);
        	//System.out.println(bg.getIsRejected());
        }
        else {
        	//System.out.print("there");
        	bg.setSeller('y');
        }
        //int final_price = jso.getInt("final_price");
        //int is_rejected = jso.getChar("is_rejected");
        // JSONArray item = jso.getJSONArray("item");
        // JSONArray quantity = jso.getJSONArray("quantity");
        /** 取出經解析到 JsonReader 之 Request 參數 */
        //String id = jsr.getParameter("id");
        //String customer_id = jsr.getParameter("customer_id");
        //String supplier_id = jsr.getParameter("supplier_id");
        //System.out.print(id);

        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();

        /** 透過 bargainHelper 物件的 create() 方法新建一筆議價至資料庫 */
        JSONObject result = bh.update(bg);

        /** 設定回傳回來的議價編號//與議價細項編號 */
        //bg.setId((int) result.getLong("bargain_id"));
        //bg.setbargainProductId(result.getJSONArray("bargain_product_id"));

        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        //JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "議價新增成功！");
        resp.put("response", result);
        //TODO
        // resp.put("response", bg.getbargainAllInfo());

        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
        jsr.response(resp, response);
	}
}
