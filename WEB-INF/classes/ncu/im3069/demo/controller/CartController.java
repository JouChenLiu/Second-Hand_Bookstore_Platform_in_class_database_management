package ncu.im3069.demo.controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.Cart;
import ncu.im3069.demo.app.CartHelper;
import ncu.im3069.demo.app.Product;
import ncu.im3069.demo.app.ProductHelper;
import ncu.im3069.tools.JsonReader;

import javax.servlet.annotation.WebServlet;
/**
 * Servlet implementation class Test
 */
@WebServlet("/api/cart.do")
public class CartController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProductHelper ph =  ProductHelper.getHelper();
    private CartHelper ch =  CartHelper.getHelper();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CartController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);

        /** 取出經解析到 JsonReader 之 Request 參數 */
        String id = jsr.getParameter("id");
        System.out.println(id);

        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();

        /** 判斷該字串是否存在，若存在代表要取回個別訂單之資料，否則代表要取回全部資料庫內訂單之資料 */
        if (!id.isEmpty()) {
          /** 透過 orderHelper 物件的 getByMemberId() 方法自資料庫取回該筆訂單之資料，回傳之資料為 JSONObject 物件 */
          JSONObject query = ch.getByMemberId(Integer.parseInt(id));
          resp.put("status", "200");
          resp.put("message", "單筆訂單資料取得成功");
          resp.put("data", query);
        }
        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
        jsr.response(resp, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        /** 取出經解析到 JSONObject 之 Request 參數 */
        int memberId = jso.getInt("memberId");
        int productId = jso.getInt("productId");
        int quantity = jso.getInt("quantity");
        String bargain = jso.getString("bargain");
        int price;
		if(bargain.equals("n")){
			/** 建立一個新的購物車物件 */
			Product p = ph.getById(String.valueOf(productId));
			price = (int) p.getPrice();
			System.out.println(price);
		}
        else{
        	price = jso.getInt("price");
		}
		Cart c = new Cart(memberId, productId, quantity, price, bargain);
		/** 透過CartHelper物件的checkDuplicate()檢查該會員電子郵件信箱是否有重複 */
        if (!ch.checkDuplicate(c)) {
            /** 透過CartHelper物件的create()方法新建一個會員至資料庫 */
            JSONObject data = ch.create(c);
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "成功! 加入購物車...");
            resp.put("response", data);
            
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
        else {
            /** 透過CartHelper物件的create()方法新建一個會員至資料庫 */
            JSONObject data = ch.update(c);
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "成功! 加入購物車...");
            resp.put("response", data);
			/** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        }
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JsonReader jsr = new JsonReader(request);

        /** 取出經解析到 JSONObject 之 Request 參數 */
        String cartId = jsr.getParameter("cartId");
        int quantity = Integer.parseInt(jsr.getParameter("quantity"));
        System.out.println(quantity);
		Cart c = ch.getById(cartId);
		c.setProductNum(quantity);
		System.out.println(c.getProductNum());
        JSONObject data = ch.update(c);
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 加入購物車...");
        resp.put("response", data);
		/** 透過JsonReader物件回傳到前端（以字串方式） */
        jsr.response(resp, response);
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JsonReader jsr = new JsonReader(request);

        /** 取出經解析到 JSONObject 之 Request 參數 */
        int id = Integer.parseInt(jsr.getParameter("id"));
        JSONObject query = ch.deleteByID(id);

		/** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "購物車移除成功！");
        resp.put("response", query);
		/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
	}

}
