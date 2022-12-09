package ncu.im3069.demo.controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.Admin;
import ncu.im3069.demo.app.AdminHelper;
import ncu.im3069.tools.JsonReader;

import javax.servlet.annotation.WebServlet;
/**
 * Servlet implementation class Test
 */
@WebServlet("/api/admin.do")
public class AdminController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private AdminHelper ah =  AdminHelper.getHelper();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminController() {
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
        JSONObject query;
        /** 判斷該字串是否存在，若存在代表要取回個別訂單之資料，否則代表要取回全部資料庫內訂單之資料 */
        if (id.isEmpty()) {
          /** 透過 orderHelper 物件的 getByMemberId() 方法自資料庫取回該筆訂單之資料，回傳之資料為 JSONObject 物件 */
          query = ah.getAll();
        } 
        else{
        	query = ah.getById(Integer.parseInt(id)).getData();
        }
        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
        resp.put("status", "200");
        resp.put("message", "資料取得成功");
        resp.put("data", query);
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
        String email = jso.getString("email");
        String account = jso.getString("account");
        String password = jso.getString("password");
		Admin a = new Admin(email, password, account);
		System.out.println(email);
		System.out.println(password);
		System.out.println(account);
		/** 透過CartHelper物件的checkDuplicate()檢查該會員電子郵件信箱是否有重複 */
        if (!ah.checkDuplicate(a)) {
            /** 透過CartHelper物件的create()方法新建一個會員至資料庫 */
            JSONObject data = ah.create(a);
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "成功!");
            resp.put("response", data);

            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
        else {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'新增帳號失敗，此E-Mail或帳號名稱重複！\', \'response\': \'\'}";
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
        String adminId = jsr.getParameter("adminId");
        String password = jsr.getParameter("password");
        String newPassword = jsr.getParameter("newPassword");
		Admin a = ah.getById(Integer.parseInt(adminId));
        if(a.getPassword().equals(password)){
            a.setPassword(newPassword);
            JSONObject data = ah.update(a);
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "資料更改成功");
            resp.put("data", data);
            jsr.response(resp, response);
        }
        else {
        	/** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'更新密碼失敗，舊密碼不符合！\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        }
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JsonReader jsr = new JsonReader(request);

        /** 取出經解析到 JSONObject 之 Request 參數 */
        int id = Integer.parseInt(jsr.getParameter("id"));
        JSONObject query = ah.deleteByID(id);

		/** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "刪除成功！");
        resp.put("response", query);
		/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
	}

}
