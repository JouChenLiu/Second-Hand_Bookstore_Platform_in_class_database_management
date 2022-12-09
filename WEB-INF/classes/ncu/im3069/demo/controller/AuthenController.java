package ncu.im3069.demo.controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import org.json.*;
import ncu.im3069.demo.app.Member;
import ncu.im3069.demo.app.AuthenHelper;
import ncu.im3069.tools.JsonReader;

import javax.servlet.annotation.WebServlet;

@WebServlet("/api/authen.do")
public class AuthenController extends HttpServlet{
	/** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** mh，MemberHelper之物件與Member相關之資料庫方法（Sigleton） */
    private AuthenHelper mh =  AuthenHelper.getHelper();
    public AuthenController() {
        super();
    }
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        String rpassword = jso.getString("password");
        String raccount = jso.getString("account");
        
        /** 建立一個新的會員物件 */
        Member m = new Member(rpassword, raccount);
        
        /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
        if(rpassword.isEmpty() || raccount.isEmpty()) {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        }
        else {
        	JSONObject data = mh.getchecked(raccount, rpassword);
    		if(data.getInt("type") == 0){
    			/** 以字串組出JSON格式之資料 */
                String resp = "{\"status\": \'400\', \"message\": \'登入失敗！\', \'response\': \'\'}";
                /** 透過JsonReader物件回傳到前端（以字串方式） */
                jsr.response(resp, response);
    		}
            else {
            	/** 新建一個JSONObject用於將回傳之資料進行封裝 */
            	System.out.println("here");
                JSONObject resp = new JSONObject();
                resp.put("status", "200");
                resp.put("message", "成功登入!");
                resp.put("data", data);
                
                /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
                jsr.response(resp, response);
            }
        }
		
	}

    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    	/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        String remail = jso.getString("email");
        System.out.println(remail);
        
        /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
        if(!remail.isEmpty()) {
        	JSONObject data = mh.checkDuplicate(remail);
        	
        	/** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        	JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "檢查成功!");
            resp.put("data", data);
            
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
            System.out.println("hi!");
        }
        else {
        	/** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        	JSONObject resp = new JSONObject();
            resp.put("status", "400");
            resp.put("message", "檢查失敗!");
            System.out.println("bad!");
            
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
        }
    }

