package ncu.im3069.demo.controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.CartHelper;
import ncu.im3069.demo.app.MemberHelper;
import ncu.im3069.demo.app.Order;
import ncu.im3069.demo.app.Product;
import ncu.im3069.demo.app.ProductHelper;
import ncu.im3069.demo.app.OrderHelper;
import ncu.im3069.demo.app.OrderItemHelper;
import ncu.im3069.tools.JsonReader;

import javax.servlet.annotation.WebServlet;

@WebServlet("/api/order.do")
public class OrderController extends HttpServlet {

    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

    /** ph，ProductHelper 之物件與 Product 相關之資料庫方法（Sigleton） */
    private ProductHelper ph =  ProductHelper.getHelper();

    /** oh，OrderHelper 之物件與 order 相關之資料庫方法（Sigleton） */
	private OrderHelper oh =  OrderHelper.getHelper();

    // /** oih，OrderItemHelper 之物件與 order 相關之資料庫方法（Sigleton） */
	private OrderItemHelper oih =  OrderItemHelper.getHelper();
    
	private MemberHelper mh =  MemberHelper.getHelper();
	private CartHelper ch =  CartHelper.getHelper();

    public OrderController() {
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
        String id = jsr.getParameter("id");
        String customer_id = jsr.getParameter("customer_id");
        String supplier_id = jsr.getParameter("supplier_id");

        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();

        /** 判斷該字串是否存在，若存在代表要取回個別訂單之資料，否則代表要取回全部資料庫內訂單之資料 */
        if (!id.isEmpty()) {
            /** 透過 orderHelper 物件的 getByID() 方法自資料庫取回該筆訂單之資料，回傳之資料為 JSONObject 物件 */
            String getSupllier = jsr.getParameter("type");
            if(!getSupllier.isEmpty()){
            	if(getSupllier.equals("c")) {
                    JSONObject query = oh.getByCustomerId(id);
                    resp.put("customer", query);
            		
            	}
            	else {
                    JSONObject supplier = oh.getBySupplierId(id);
                    resp.put("supplier", supplier);
            		
            	}
                resp.put("message", "資料取得成功");
                resp.put("status", "200");
            
            }
            else{
                JSONObject query = oh.getById(id);
                resp.put("status", "200");
                resp.put("message", "資料取得成功");
                resp.put("response", query);
                JSONObject member = mh.getByID(query.getJSONObject("data").getJSONObject("order_info").getInt("customer_id"));
                resp.put("member", member);
            }
        }
        else if (!customer_id.isEmpty()) {
          /** 透過 orderItemHelper 物件的 getByCustomerID() 方法自資料庫取回該筆訂單之資料，回傳之資料為 JSONObject 物件 */
          JSONObject query = oih.getOrderProductByCustomerId(customer_id);
          resp.put("status", "200");
          resp.put("message", "單筆訂單資料取得成功");
          resp.put("response", query);
        }
        else if (!supplier_id.isEmpty()) {
          /** 透過 orderItemHelper 物件的 getBySupplierID() 方法自資料庫取回該筆訂單之資料，回傳之資料為 JSONObject 物件 */
          JSONObject query = oih.getOrderProductBySupplierId(supplier_id);
          
          resp.put("status", "200");
          resp.put("message", "單筆訂單資料取得成功");
          resp.put("response", query);
        }
        else {
          /** 透過 orderHelper 物件之 getAll() 方法取回所有訂單之資料，回傳之資料為 JSONObject 物件 */
          JSONObject query = oh.getAll();
          resp.put("status", "200");
          resp.put("message", "所有訂單資料取得成功");
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
        String address = jso.getString("address");
        if(address.equals("first")) {
        	address = "中壢中央一店(松果)";
        }
        else if(address.equals("second")) {
        	address = "中壢中央一店(男九)";
        }
        else if(address.equals("third")) {
        	address = "中壢中央一店(松苑)";
        }
        int totalPrice = jso.getInt("totalPrice");
        JSONArray item = jso.getJSONArray("item");
        JSONArray quantity = jso.getJSONArray("quantity");
        JSONArray cart = jso.getJSONArray("cart");

        /** 建立一個新的訂單物件 */
        Order od = new Order(customer_id, address, totalPrice);

        /** 將每一筆訂單細項取得出來 */
        for(int i=0 ; i < item.length() ; i++) {
            String product_id = Integer.toString( item.getInt(i));
            int amount = quantity.getInt(i);

            /** 透過 ProductHelper 物件之 getById()，取得產品的資料並加進訂單物件裡 */
            Product pd = ph.getById(product_id);
            od.addOrderProduct(pd, amount, customer_id);
        }

        /** 透過 orderHelper 物件的 create() 方法新建一筆訂單至資料庫 */
        JSONObject result = oh.create(od);

        /** 設定回傳回來的訂單編號與訂單細項編號 */
        od.setOrderId((int) result.getLong("order_id"));
        od.setOrderProductId(result.getJSONArray("order_product_id"));
        
        /** 將每一筆訂單細項取得出來 */
        for(int i=0 ; i < cart.length() ; i++) {
            int cart_id =cart.getInt(i);

            /** 透過 ProductHelper 物件之 getById()，取得產品的資料並加進訂單物件裡 */
            JSONObject res = ch.deleteByID(cart_id);
        }
        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "訂單新增成功！");
        resp.put("response", od.getOrderAllInfo());

        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
        jsr.response(resp, response);
	}

}
