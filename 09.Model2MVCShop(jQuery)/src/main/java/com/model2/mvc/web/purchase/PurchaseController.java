package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;

@Controller
@RequestMapping("/purchase/*")
public class PurchaseController {

	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	public PurchaseController() {
		System.out.println(this.getClass());
	}
	
	@RequestMapping(value="addPurchase", method = RequestMethod.GET)
	public ModelAndView addPurchaseView(HttpSession session, @RequestParam("prod_no") int prodNo) throws Exception{
		
		System.out.println("/purchase/addPurchase : GET");
		
		ModelAndView modelAndView = new ModelAndView();
		
		String userId = ((User)session.getAttribute("user")).getUserId();
		User user = userService.getUser(userId);
		
		Product product = productService.getProduct(prodNo);
		
		modelAndView.setViewName("/purchase/addPurchaseView.jsp");
		modelAndView.addObject("product", product);
		modelAndView.addObject("user", user);
		
		return modelAndView;
	}
	
	@RequestMapping(value="addPurchase", method = RequestMethod.POST)
	public ModelAndView addPurchase(Product product, User user, Purchase purchase ) throws Exception{
		
		System.out.println("/purchase/addPurchase : POST");
		
		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);
		
		ModelAndView modelAndView = new ModelAndView();
		
		purchaseService.addPurchase(purchase);
		
		modelAndView.setViewName("/purchase/addPurchase.jsp");
		
		return modelAndView;
		
	}
	
	@RequestMapping(value="listPurchase")
	public ModelAndView listPurchase(@ModelAttribute("search") Search search, HttpSession session) throws Exception{
		
		System.out.println("/purchase/listPurchase : GET, POST");
		
		if(search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		String buyerId = ((User)session.getAttribute("user")).getUserId();
		
		Map<String, Object> map = purchaseService.getPurchaseList(search, buyerId);
		
		Page resultPage = new Page(search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/purchase/listPurchase.jsp");
		modelAndView.addObject("list", map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);
		
		
		return modelAndView;
	}
	
	@RequestMapping(value="getPurchase", method = RequestMethod.GET)
	public ModelAndView getPurchase(@RequestParam("tranNo") int tranNo) throws Exception{
		
		System.out.println("/purchase/getPurchase : GET");
		
		ModelAndView modelAndView = new ModelAndView();
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		modelAndView.setViewName("/purchase/getPurchase.jsp");
		modelAndView.addObject("purchase", purchase);
		
		return modelAndView;
	}
	
	@RequestMapping(value="updatePurchase", method = RequestMethod.GET)
	public ModelAndView updatePurchaseView(@RequestParam("tranNo") int tranNo) throws Exception{
		//View가 post로 오는데? addPurchase.jsp , 근대 전송버튼이없긴함
		System.out.println("/purchase/updatePurchase : GET");
		
		ModelAndView modelAndView = new ModelAndView();
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		modelAndView.setViewName("/purchase/updatePurchaseView.jsp");
		modelAndView.addObject("purchase", purchase);
		
		
		return modelAndView;
	}
	
	@RequestMapping(value="updatePurchase", method = RequestMethod.POST)
	public ModelAndView updatePurchase(Product product, User user, Purchase purchase, @RequestParam("tranNo") int tranNo) throws Exception{
		
		System.out.println("/purchase/updatePurchase : POST");
		
		ModelAndView modelAndView = new ModelAndView();
		
		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);
		
		purchaseService.updatePurcahse(purchase);
		
		purchase = purchaseService.getPurchase(tranNo);
		
		modelAndView.setViewName("/purchase/updatePurchase.jsp");
		modelAndView.addObject("purchase", purchase);
		
		return modelAndView;
	}
	
	
	
//	@RequestMapping(value="listSale")
//	public ModelAndView listSale() throws Exception{
//		
//		System.out.println("/purchase/listSale");
//		
//		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.setViewName("/purchase/getPurchase.jsp");
//		
//		return modelAndView;
//	}
	
	@RequestMapping(value="updateTranCode", method = RequestMethod.GET)
	public ModelAndView updateTranCode(@ModelAttribute("purchase") Purchase purchase) throws Exception{
		
		System.out.println("/purchase/updateTranCode : GET");
		
		ModelAndView modelAndView = new ModelAndView();
		
		purchaseService.updateTranCode(purchase);
		
		modelAndView.setViewName("redirect:/purchase/listPurchase");
		
		return modelAndView;
	}
	
	@RequestMapping(value="updateTranCodeByProd", method = RequestMethod.GET)
	public ModelAndView updateTranCodeByProd(Product product, Purchase purchase, Search search, HttpSession session) throws Exception{
		
		System.out.println("/purchase/updateTranCodeByProd : GET");
		
		purchase.setPurchaseProd(product);
		
		purchaseService.updateTranCode(purchase);
		
		if(search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String, Object> map = productService.getProductList(search);
		
		Page resultPage = new Page(search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/purchase/updateTranCodeByProd.jsp");
		modelAndView.addObject("list", map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);		
		
		return modelAndView;
	}
	
}
