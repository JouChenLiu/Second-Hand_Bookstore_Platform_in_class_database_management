package ncu.im3069.demo.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.Bargain;
import ncu.im3069.demo.app.BargainHelper;
import ncu.im3069.demo.app.Photo;
import ncu.im3069.demo.app.PhotoHelper;
import ncu.im3069.demo.app.Product;
import ncu.im3069.demo.app.ProductHelper;
import ncu.im3069.tools.JsonReader;


import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

@WebServlet("/api/product.do")
@MultipartConfig
public class ProductController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ProductHelper ph =  ProductHelper.getHelper();
	private PhotoHelper poh =  PhotoHelper.getHelper();
	private BargainHelper bh =  BargainHelper.getHelper();

    public ProductController() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//fixHeaders(response);
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String id_list = jsr.getParameter("id_list");
        String id = jsr.getParameter("id"); // product_id
        String customer_id = jsr.getParameter("customer_id"); // customer_id
        
        JSONObject resp = new JSONObject();
        /** 判斷該字串是否存在，若存在代表要取回購物車內產品之資料，否則代表要取回全部資料庫內產品之資料 */
        if (!id_list.isEmpty()) {
          JSONObject query = ph.getByIdList(id_list);
          resp.put("status", "200");
          resp.put("message", "所有購物車之商品資料取得成功");
          resp.put("response", query);
        }
        else if (!id.isEmpty()){
        	/** 判斷id是否存在，若存在代表取回單一商品 */
        	int product_id = Integer.parseInt(id);
        
        	JSONObject query = ph.getById(id).getData(); // ph.取Product.取product info
        	//get photo array
        	ArrayList<Object> srcs = new ArrayList<Object>(); // array，用來存商品的photos
        	JSONObject photos = poh.getByProductId(product_id); // 取product 的 photos, by id
			JSONArray photoData = photos.getJSONArray("data"); // 取photo的data
			for (int i = 0; i < photoData.length(); i++) {
				srcs.add("/NCU_MIS_SA/statics/files/" + photoData.getJSONObject(i).get("imgName"));
			}
        	query.put("src", srcs);
        	
        	//get is_bargained
        	if(bh.checkByCIandPI(customer_id, id)) {
        		//System.out.println('1');
        		resp.put("is_bargained", "y");
        		resp.put("bargain_info", bh.getByCIandPI(customer_id, id).getData());
        	} else {
        		resp.put("is_bargained", "n");
        	}
        	
            resp.put("status", "200");
            resp.put("message", "所有購物車之商品資料取得成功");
            resp.put("response", query);
        }
        else {
          JSONObject query = ph.getAll();

          resp.put("status", "200");
          resp.put("message", "所有商品資料取得成功");
          resp.put("response", query);
        }

        jsr.response(resp, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        //JsonReader jsr = new JsonReader(request);
        //JSONObject jso = jsr.getObject();
        
        int supplier_id = Integer.parseInt(request.getParameter("supplier_id"));
        String name = request.getParameter("name");
        String author = request.getParameter("author");
        int price = Integer.parseInt(request.getParameter("price"));
        int stock_num = Integer.parseInt(request.getParameter("stock"));
        int condition = Integer.parseInt(request.getParameter("condition"));
        String is_bargain = request.getParameter("bargain");
        System.out.println("name: "+name+"\nauthor: "+author+"\nprice: "+ price+"\nstock: "+ stock_num+"\ncondition: "+ condition+"\nbargain: "+ is_bargain);
		response.setContentType("text/html;charset=UTF-8");
		List<Part> fileParts = request.getParts().stream().filter(part -> "files".equals(part.getName()) && part.getSize() > 0).collect(Collectors.toList());
        // Retrieves <input type="file" name="files" multiple="true">
        //product
		Product p = new Product(supplier_id, name, price, stock_num, author, condition, is_bargain);
		
		//photo
		
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
            
            //int foreignId = 3;  //也是自己拿到值進行設定
            p.addProductPhoto(fileName);
            //Photo po = new Photo(true, foreignId, fileName);
    		//JSONObject resp = poh.create(po);
    		//System.out.println(resp);
            //response.sendRedirect("/NCU_MIS_SA/index.html");
		}
		/** 透過 photoHelper 物件的 create() 方法新建一筆訂單至資料庫 */
		JSONObject result = ph.create(p);
		/** 設定回傳回來的商品與照片編號 */
        p.setProductId((int) result.getLong("product_id"));
        p.setProductPhotoId(result.getJSONArray("product_photo_id"));

        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "訂單新增成功！");
        resp.put("response", p.getProductAllInfo());

        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
        //jsr.response(resp, response);
        response.sendRedirect("/NCU_MIS_SA/product.html");
	}
}

//	    /**
//	        	System.out.println(f.getName() + " deleted");   //getting and printing the file name  
//	        }  
//	        JSONObject query = poh.deleteById(id);
//
//
//
//        /** 取出經解析到 JSONObject 之 Request 參數 */
//        int supplier_id = jso.getInt("supplier_id");
//        String name = jso.getString("name");
//        int price = jso.getInt("price");
//        int stock_num = jso.getInt("stock_num");
//        String author = jso.getString("author");
//        String condition = jso.getString("condition");
//        String is_bargain = jso.getString("is_bargain");
//        JSONArray photos = jso.getJSONArray("photo");
//        // JSONArray quantity = jso.getJSONArray("quantity");
//
//        /** 建立一個新的議價物件 */
//        Product p = new Product(supplier_id, name, price, stock_num, author, condition, is_bargain);
//  
//        /** 將每一筆照片取得出來 */
//        for(int i=0 ; i < photos.length() ; i++) {
//            String product_id = photos.getString(i);
//
//            /** 透過 ProductHelper 物件之 getById()，取得產品的資料並加進議價物件裡 */
//            Product pd = ph.getById(product_id);
//            pd.addProductPhoto(pd, );
//        }
//
//        /** 透過 orderHelper 物件的 create() 方法新建一筆訂單至資料庫 */
//        JSONObject result = oh.create(od);
//
//        /** 設定回傳回來的訂單編號與訂單細項編號 */
//        od.setOrderId((int) result.getLong("order_id"));
//        od.setOrderProductId(result.getJSONArray("order_product_id"));
//
//        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
//        JSONObject resp = new JSONObject();
//        resp.put("status", "200");
//        resp.put("message", "訂單新增成功！");
//        resp.put("response", od.getOrderAllInfo());
//
//        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
//        jsr.response(resp, response);
//	}
//
//}
