package ncu.im3069.demo.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Member;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.MemberHelper;
import ncu.im3069.demo.app.Photo;
import ncu.im3069.demo.app.PhotoHelper;
import ncu.im3069.demo.app.SendEmail;
import ncu.im3069.tools.JsonReader;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

@WebServlet("/api/photo.do")
@MultipartConfig
public class PhotoController extends HttpServlet {
	private PhotoHelper poh =  PhotoHelper.getHelper();
	private MemberHelper mh =  MemberHelper.getHelper();
    /**
     * 處理 Http Method 請求 GET 方法（新增資料）
     *
     * @param request Servlet 請求之 HttpServletRequest 之 Request 物件（前端到後端）
     * @param response Servlet 回傳之 HttpServletResponse 之 Response 物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String type = request.getParameter("type");
//        int id = Integer.parseInt(request.getParameter("id"));
//		if(type == "getById") {
//	        Photo p = poh.getById(id);
//	        response.sendRedirect("/NCU_MIS_SA/statics/files/"+p.getName());
//		}
//		else {
//			JSONObject query = poh.getByProductId(id);
//			JSONArray data = query.getJSONArray("data");
//			System.out.println(data);
//			for (int i = 0; i < data.length(); i++) {
//				System.out.println(data.getJSONObject(i).get("imgName"));
//			}
//			// data is the array of product's photo information, imgName can be seen in console
//	        response.sendRedirect("/NCU_MIS_SA/statics/files/photo1.jpg");
//		}
//	}
    /**
     * 處理 Http Method 請求 POST 方法（新增資料）
     *
     * @param request Servlet 請求之 HttpServletRequest 之 Request 物件（前端到後端）
     * @param response Servlet 回傳之 HttpServletResponse 之 Response 物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ah");
		//JsonReader jsr = new JsonReader(request);
        String type = request.getParameter("type");
        int member_id = Integer.parseInt(request.getParameter("memberid"));
        if(type.equals("photo")){
            response.setContentType("text/html;charset=UTF-8");
            List<Part> fileParts = request.getParts().stream().filter(part -> "files".equals(part.getName()) && part.getSize() > 0).collect(Collectors.toList());
            String fileName = "";
            for (Part filePart : fileParts) {
                fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
                System.out.println(fileName);
                ServletContext s1 = this.getServletContext();
                String path =s1.getRealPath("/statics/files")+File.separator+fileName;
                System.out.println(path);
                String[] parts = fileName.split(Pattern.quote("."));
                System.out.println(parts[1]);
                InputStream fileContent = filePart.getInputStream();
                BufferedImage image = ImageIO.read(fileContent);
                ImageIO.write(image , parts[1], new File(path));
            }
            Photo p = new Photo(false, member_id, fileName);
            JSONObject resp = poh.create(p);
            System.out.println(resp);
            HttpSession session  = request.getSession();
            session.setAttribute("verifyType", "photo");
            response.sendRedirect("/NCU_MIS_SA/verify.html");
        }
        else{
            String webmail = request.getParameter("webmail");
            SendEmail sm = new SendEmail();
            String code = sm.getRandom();
            String subject = "驗證信箱";
            String message = "註冊已成功，請在網站輸入以下的隨機碼來驗證信箱: "+code;
            //call the send email method
            boolean test = sm.sendEmail(webmail, subject, message);
            if(test){
                HttpSession session  = request.getSession();
                session.setAttribute("verifyType", "webmail");
                session.setAttribute("authCode", code);
                response.sendRedirect("/NCU_MIS_SA/verify.html");
            }else if(type.equals("resend")){
            	System.out.println("Failed to send verification email");
                response.sendRedirect("/NCU_MIS_SA/verify.html?resend=false");
            }else{
                    System.out.println("Failed to send verification email");
                    response.sendRedirect("/NCU_MIS_SA/authentication.html");
            }
        }
		
	}

    /**
     * 處理Http Method請求DELETE方法（刪除）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        int id = Integer.parseInt(request.getParameter("id"));
        Photo p = poh.getById(id);
        ServletContext s1 = this.getServletContext();
        String path =s1.getRealPath("/statics/files")+File.separator+p.getName();
        File f = new File(path);
        if(f.delete()) {
        	System.out.println(f.getName() + " deleted");   //getting and printing the file name  
        }  
        JSONObject query = poh.deleteById(id);
        response.sendRedirect("/NCU_MIS_SA/product.html");
    }

    /**
     * 處理Http Method請求PUT方法（更新）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doPut(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
         /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
    	 JsonReader jsr = new JsonReader(request);
    	 String email = jsr.getParameter("email");
         JSONObject jso = jsr.getObject();
         response.setContentType("text/html;charset=UTF-8");
    	 HttpSession session = request.getSession();
    	 
    	 
    	String flag = jso.getString("flag");
        if(!flag.isEmpty()) {
        	 String webmail = jso.getString("webmail");
        	 System.out.println("lo");
             SendEmail sm = new SendEmail();
             String code = sm.getRandom();
           //call the send email method
             String subject = "修改密碼信箱";
             String message = "請在網站輸入以下的新密碼來登入: "+code;
             //call the send email method
             boolean test = sm.sendEmail(webmail, subject, message);
             System.out.println(test);
         	//改密碼
             int i = mh.changePassword(webmail, code);
             if(i == 1) {
            	 /** 新建一個JSONObject用於將回傳之資料進行封裝 */
                 JSONObject resp = new JSONObject();
                 resp.put("status", "200");
                 resp.put("message", "成功修改密碼");
                 resp.put("success", "1");
                 System.out.println("lo");
                 jsr.response(resp, response);
                 System.out.println("lo");
             }else if(i == 0) {
            	 /** 新建一個JSONObject用於將回傳之資料進行封裝 */
                 JSONObject resp = new JSONObject();
                 resp.put("status", "200");
                 resp.put("message", "修改密碼失敗");
                 resp.put("success", "0");
                 System.out.println("lo");
                 jsr.response(resp, response);
                 System.out.println("lo");
             }
             
        }
        else if(email.isEmpty()) {
            int member_id = jso.getInt("id");
            mh.updateStatus(member_id);
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "成功!");
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
        else {
            int member_id = jso.getInt("id");
        	if("webmail".equals((String) session.getAttribute("verifyType"))){
        		 System.out.println("yo");
        		 String authCode = (String) session.getAttribute("authCode");
                 String code = jso.getString("code");
                 if(code.equals(authCode)){
                	 mh.updateStatus(member_id);
                     JSONObject resp = new JSONObject();
                     resp.put("status", "200");
                     resp.put("message", "成功!");
                     /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
                     jsr.response(resp, response);
                 }
                 else {
                	 String resp = "{\"status\": \'400\', \"message\": \'驗證帳號失敗，驗證碼錯誤或已過期\', \'response\': \'\'}";
                     /** 透過JsonReader物件回傳到前端（以字串方式） */
                     jsr.response(resp, response);
                 }
        	 }
        	 else {
        		 System.out.println("yo");
        		 String resp = "{\"status\": \'400\', \"message\": \'非選擇信箱驗證\', \'response\': \'\'}";
                 /** 透過JsonReader物件回傳到前端（以字串方式） */
                 jsr.response(resp, response);
        	 }
         }
     }
}
