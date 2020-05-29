package com.model2.mvc.web.product;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

@Controller
@RequestMapping("/product/*")
public class ProductController {
	
	@Autowired
	@Qualifier("productServiceImpl")
	ProductService productService;
	
	@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;

	public ProductController() {
		System.out.println(this.getClass());
	}

	@RequestMapping(value="addProduct", method=RequestMethod.GET)
	public String addProductView() throws Exception{
		
		System.out.println("/product/addProduct : GET");
		
		return "redirect:/product/addProductView.jsp";
	}
	
	@RequestMapping(value="addProduct", method=RequestMethod.POST)
	public String addProduct(@ModelAttribute("product") Product product) throws Exception{
		
		System.out.println("/product/addProduct : POST");
		
		productService.addProduct(product);
		
		return "/product/addProduct.jsp";
	}
	
	@RequestMapping(value="getProduct", method=RequestMethod.GET)
	public String getProduct(@RequestParam("prodNo") int prodNo, @RequestParam(value="menu", required=false) String menu, Model model) throws Exception{
		
		System.out.println("/product/getProduct : GET");
		
		Product product = productService.getProduct(prodNo);
		
		model.addAttribute("product", product);
		
		
		if(menu != null && menu.equals("manage")) {
			return "/product/updateProductView.jsp";
		}else {
			return "/product/getProduct.jsp";			
		}
		
	}
	
	@RequestMapping(value="listProduct")
	public String listProduct(@ModelAttribute("search") Search search,@RequestParam(value="menu", required=false) String menu, Model model) throws Exception{
		
		System.out.println("/product/listProduct : GET, POST");
		
		System.out.println("menu : "+menu);
		
		if(search.getCurrentPage() == 0 ) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		
		Map<String, Object> map = productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("search", search);
		model.addAttribute("menu", menu);
		
		
		return "/product/listProduct.jsp";
	}
	
	@RequestMapping(value="/product/updateProduct", method=RequestMethod.POST)
	public String updateProduct(@ModelAttribute("product") Product product) throws Exception{
		
		System.out.println("/product/updateProduct : POST");
		
		productService.updateProduct(product);
		
		return "redirect:/product/getProduct?prodNo="+product.getProdNo();
	}
	
//	@RequestMapping(value="/product/updateProduct", method=RequestMethod.GET)
//	public String updateProduct()throws Exception{
//		
//		System.out.println("/product/updateProduct : GET");
//		
//		return "redirect:/listProduct.do?menu=manage";
//	}
	
}
